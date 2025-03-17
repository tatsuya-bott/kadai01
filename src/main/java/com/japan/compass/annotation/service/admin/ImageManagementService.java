package com.japan.compass.annotation.service.admin;

import com.japan.compass.annotation.domain.ImageList;
import com.japan.compass.annotation.domain.ImageModel;
import com.japan.compass.annotation.domain.entity.Image;
import com.japan.compass.annotation.exception.ApplicationException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageManagementService {

    Image getImage(int id);

    ImageList getImageList(int questionId, int limit, int offset);
    ImageList getImageList(int limit, int offset);

    void register(MultipartFile file, int questionId) throws ApplicationException;
    void registerZip(MultipartFile file, int questionId) throws ApplicationException;

    void updateImage(ImageModel imageModel);
    void deleteImage(int imageId, int questionId, String filename);
}
