package com.japan.compass.annotation.controller.admin;

import com.japan.compass.annotation.domain.ImageList;
import com.japan.compass.annotation.domain.ImageModel;
import com.japan.compass.annotation.domain.QuestionList;
import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.exception.ApplicationException;
import com.japan.compass.annotation.exception.Errors;
import com.japan.compass.annotation.service.admin.ImageManagementService;
import com.japan.compass.annotation.service.admin.QuestionManagementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
@Validated
@Controller
@RequestMapping("/admin/image")
public class ImageManagementController {

    private final ImageManagementService imageManagementService;
    private final QuestionManagementService questionManagementService;
    private final MultipartProperties multipartProperties;

    @Value("${image.allowed-file-format:jpg, jpeg, png}")
    private List<String> allowedFileFormat;

    /*
     * 画像管理画面
     */
    @GetMapping("")
    public String getImage(
            @Min(1) @RequestParam(value = "page", defaultValue = "1", required = true) int page,
            @Min(0) @RequestParam(value = "question_id", defaultValue = "0", required = true) int questionId,
            Model model
    ) {
        if (questionId == 0) {
            model.addAttribute("imageList", imageManagementService.getImageList(
                    ImageList.LIMIT.getValue(),
                    ImageList.LIMIT.getValue() * (page-1)
            ));
        } else {
            model.addAttribute("imageList", imageManagementService.getImageList(
                    questionId,
                    ImageList.LIMIT.getValue(),
                    ImageList.LIMIT.getValue() * (page-1)
            ));
        }
        QuestionList questionList = questionManagementService.getQuestionList();
        model.addAttribute("questionList", questionList);
        model.addAttribute("questionId", questionId);
        model.addAttribute("currentPage", page);

        return "admin/image";
    }

    @GetMapping("/new")
    public String getNewImage(
            Model model
    ) {
        QuestionList questionList = questionManagementService.getQuestionList();
        model.addAttribute("questionList", questionList);
        model.addAttribute("allowedFileFormat", String.join(", ", allowedFileFormat));
        model.addAttribute("uploadSizeLimit", multipartProperties.getMaxFileSize().toGigabytes());
        return "admin/image_new";
    }

    @PostMapping("/new")
    public String postNewImage(
            @Min(1) @RequestParam(value = "question_id", required = true) int questionId,
            @RequestParam(value = "file", required = true) MultipartFile file,
            RedirectAttributes redirectAttributes
    ) throws ApplicationException {
        List<String> whiteList = Stream.concat(allowedFileFormat.stream(), Stream.of("zip"))
                .collect(Collectors.toList());

        if (file.getOriginalFilename() == null) {
            log.info("original filename is null.");
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        String filename = file.getOriginalFilename().toLowerCase();
        if (whiteList.stream().noneMatch(filename::endsWith)) {
            log.info("filename is not allowed.");
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        if (filename.endsWith("zip")) {
            imageManagementService.registerZip(file, questionId);
            Object result = Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
                    .getAttribute("resultMap", RequestAttributes.SCOPE_REQUEST);
            if (result instanceof Map) {
                Map<String, Boolean> resultMap = (Map<String, Boolean>) result;
                redirectAttributes.addFlashAttribute("successList", resultMap.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toUnmodifiableList()));
                redirectAttributes.addFlashAttribute("errorList", resultMap.entrySet().stream()
                        .filter(entry -> !entry.getValue())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toUnmodifiableList()));
            }
        } else {
            imageManagementService.register(file, questionId);
            redirectAttributes.addFlashAttribute("successList", List.of(filename));
        }

        return "redirect:/admin/image/new";
    }

    @GetMapping("/edit/{id}")
    public String editImage(
            @Min(1) @PathVariable(value = "id", required = true) int id,
            Model model
    ) {
        Image image = imageManagementService.getImage(id);
        model.addAttribute("image", image);
        return "admin/image_edit";
    }

    @PostMapping("/update")
    public String updateImage(
            @Validated @ModelAttribute ImageModel imageModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) throws ApplicationException {

        if (bindingResult.hasErrors()) {
            log.info("binding error." + bindingResult.getAllErrors());
            throw new ApplicationException(Errors.INVALID_REQUEST);
        }

        imageManagementService.updateImage(imageModel);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/admin/image/edit/" + imageModel.getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteImage(
            @Min(1) @PathVariable(value = "id") int imageId,
            @Min(1) @RequestParam(value = "questionId") int questionId,
            @NotBlank @RequestParam(value = "filename") String filename
    ) {
        imageManagementService.deleteImage(imageId, questionId, filename);
        return "redirect:/admin/image?question_id=" + questionId;
    }
}
