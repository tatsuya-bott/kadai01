package com.japan.compass.annotation.service.admin.impl;

import com.japan.compass.annotation.domain.ImageList;
import com.japan.compass.annotation.domain.ImageModel;
import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.domain.entity.Project;
import com.japan.compass.annotation.domain.entity.Question;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.exception.SystemException;
import com.japan.compass.annotation.infrastructure.ImageFileComponent;
import com.japan.compass.annotation.repository.ImageRepository;
import com.japan.compass.annotation.service.admin.ImageManagementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@AllArgsConstructor(onConstructor_ ={@Lazy})
@Service
public class ImageManagementServiceImpl implements ImageManagementService {

    private final Clock clock;
    private final ImageRepository imageRepository;
    private final ImageFileComponent imageFileComponent;
    // 同じクラスのメソッド呼び出しでのTransactionは動かないため、別で読み込んで対象のメソッドを呼び出す
    // 循環参照のためLazyでの初期化は必須 @AllArgsConstructorで設定してますがフィールドにも明示的につけておく
    @Lazy
    private final ImageManagementServiceImpl self;

    @Value("${image.allowed-file-format:jpg, jpeg, png}")
    private List<String> allowedFileFormat;

    @Override
    public Image getImage(int id) {
        return imageRepository.find(id);
    }

    @Override
    public ImageList getImageList(int questionId, int limit, int offset) {
        int count = imageRepository.countByQuestionId(questionId);
        List<Image> images = imageRepository.findAllByQuestionId(questionId, limit, offset);
        return new ImageList(images, count);
    }

    @Override
    public ImageList getImageList(int limit, int offset) {
        int count = imageRepository.count();
        List<Image> images = imageRepository.findAll(limit, offset);
        return new ImageList(images, count);
    }

    @Transactional
    @Override
    public void register(MultipartFile file, int questionId) {
        String filename = file.getOriginalFilename();

        imageRepository.create(Image.builder()
                // question_idのみ登録に利用する
                .question(Question.builder()
                        .id(questionId)
                        .project(Project.builder()
                                .name("")
                                .description("")
                                .build())
                        .text("")
                        .created(LocalDateTime.now(clock))
                        .build())
                .filePath(filename)
                .created(LocalDateTime.now(clock))
                .enabled(true)
                .build());

        if (imageFileComponent.exist(String.valueOf(questionId), filename)) {
            throw new SystemException(Errors.FILE_ALREADY_EXIST_ERROR);
        }

        try (InputStream inputStream = file.getInputStream()) {
            imageFileComponent.put(inputStream, String.valueOf(questionId), filename);
        } catch (IOException e) {
            throw new SystemException(Errors.FILE_IO_ERROR, e);
        }
    }

    @Override
    public void registerZip(MultipartFile file, int questionId) {
        Map<String, Boolean> resultMap = new HashMap<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream())) {

            for (ZipEntry entry; (entry = zipInputStream.getNextEntry()) != null; ) {
                if (entry.isDirectory()) {
                    log.info("entry is directory. {}", entry.getName());
                    zipInputStream.closeEntry();
                    continue;
                }
                String entryName = entry.getName();
                // macでのzip生成で含まれる余分なファイルは除外する
                if (entryName.contains("__MACOSX") || entryName.contains(".DS_Store")) {
                    log.info("mac os file. {}", entryName);
                    zipInputStream.closeEntry();
                    continue;
                }

                resultMap.put(entryName, false);

                String filename = entryName.substring(entryName.lastIndexOf("/") + 1);
                if (allowedFileFormat.stream().noneMatch(filename.toLowerCase()::endsWith)) {
                    log.info("not allowed file format. {}", filename);
                    zipInputStream.closeEntry();
                    continue;
                }
                try {
                    self.registerOne(zipInputStream, questionId, filename);
                    resultMap.put(entryName, true);
                    log.info("success register one. {}", entryName);
                } catch (Exception e) {
                    log.warn("register zip exception.", e);
                }
                zipInputStream.closeEntry();
            }

        } catch (IOException e) {
            throw new SystemException(Errors.FILE_IO_ERROR, e);
        } finally {
            RequestContextHolder.getRequestAttributes()
                    .setAttribute("resultMap", resultMap, RequestAttributes.SCOPE_REQUEST);
        }
    }

    @Transactional
    public void registerOne(ZipInputStream zipInputStream, int questionId, String filename) {
        imageRepository.create(Image.builder()
                // question_idのみ登録に利用する
                .question(Question.builder()
                        .id(questionId)
                        .project(Project.builder()
                                .name("")
                                .description("")
                                .build())
                        .text("")
                        .created(LocalDateTime.now(clock))
                        .build())
                .filePath(filename)
                .created(LocalDateTime.now(clock))
                .enabled(true)
                .build());
        imageFileComponent.put(zipInputStream, String.valueOf(questionId), filename);
    }

    @Override
    public void updateImage(ImageModel imageModel) {
        imageRepository.updateEnabled(imageModel.getId(), imageModel.isEnabled());
    }

    @Transactional
    @Override
    public void deleteImage(int imageId, int questionId, String filename) {
        imageRepository.delete(imageId);
        imageFileComponent.delete(String.valueOf(questionId), filename);
    }
}
