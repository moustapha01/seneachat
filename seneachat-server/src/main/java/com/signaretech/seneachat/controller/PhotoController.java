package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.persistence.entity.EntAdvertisement;
import com.signaretech.seneachat.persistence.entity.EntPhoto;
import com.signaretech.seneachat.service.IPhotoService;
import com.signaretech.seneachat.util.FileUtility;
import com.signaretech.seneachat.util.ImageScalingFactor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class PhotoController {


    @Autowired
    private ServletContext context;


    private IPhotoService photoService;

    @Autowired
    public PhotoController(IPhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/web/adphotos/{photoUuid}")
    public void loadPhoto(@PathVariable String photoUuid, HttpServletRequest req, HttpServletResponse resp) {

        UUID photoId = UUID.fromString(photoUuid);
        EntPhoto photo = photoService.findPhotoById(photoId);

        try{
            writeImage(photo,resp, ImageScalingFactor.SMALL);
        }catch (IOException ie) {
            ie.printStackTrace();
        }

    }

    private void writeImage(EntPhoto photo, HttpServletResponse resp,
                            ImageScalingFactor scalingFactor) throws IOException {

        String mimeType = context.getMimeType(photo.getName());
        resp.setContentType(mimeType);

        byte[] thumbnail = null;
        switch (scalingFactor) {
            case XLARGE:
                thumbnail = FileUtility.resizeThumbnail(photo.getImageBytes(), "xlarge");
                break;
            case LARGE:
                thumbnail = FileUtility.resizeThumbnail(photo.getImageBytes(), "large");
                break;
            case MEDIUM:
                thumbnail = FileUtility.resizeThumbnail(photo.getImageBytes(), "medium");
                break;
            case SMALL:
                thumbnail = FileUtility.resizeThumbnail(photo.getImageBytes(), "small");
                break;
            case FIXED:
                thumbnail = FileUtility.resizeThumbnail(photo.getImageBytes(), "fixed");
                break;
            default:
                break;
        }
        resp.getOutputStream().write(thumbnail);
    }

}

