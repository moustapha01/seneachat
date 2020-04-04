package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.persistence.entity.EntPhoto;
import com.signaretech.seneachat.service.IPhotoService;
import com.signaretech.seneachat.util.FileUtility;
import com.signaretech.seneachat.util.ImageScalingFactor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
public class PhotoController {

    private ServletContext context;
    private IPhotoService photoService;

    @Autowired
    public PhotoController(IPhotoService photoService, ServletContext context) {
        this.photoService = photoService;
        this.context = context;
    }

    @GetMapping("/web/dashboard/adphotos/{photoUuid}/{size}")
    public void loadPhoto(@PathVariable String photoUuid,
                               @PathVariable String size,
                               HttpServletResponse resp) {

        UUID photoId = UUID.fromString(photoUuid);
        EntPhoto photo = photoService.findPhotoById(photoId);

        try{
            writeImage(photo,resp, size);
        }catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    @GetMapping("/web/advertisements/adphotos/{photoUuid}/{size}")
    public void viewPhoto(@PathVariable String photoUuid,
                          @PathVariable String size,
                          HttpServletResponse resp) {

        UUID photoId = UUID.fromString(photoUuid);
        EntPhoto photo = photoService.findPhotoById(photoId);

        try{
            writeImage(photo,resp, size);
        }catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Writes the image bytes from photo to the HttpServletResponse output stream.
     * @param photo, {@link EntPhoto} object that contains the image bytes
     * @param resp, {@link HttpServletResponse} object
     * @param photoSize, determines the size of the image
     * @throws IOException
     */
    private synchronized void writeImage(EntPhoto photo,
                                         HttpServletResponse resp,
                                         String photoSize) throws IOException {

        String mimeType = context.getMimeType(photo.getName());
        resp.setContentType(mimeType);
        ImageScalingFactor scalingFactor = ImageScalingFactor.SMALL;
        switch (photoSize){
            case "S": scalingFactor = ImageScalingFactor.SMALL; break;
            case "M": scalingFactor = ImageScalingFactor.MEDIUM; break;
            case "L": scalingFactor = ImageScalingFactor.LARGE; break;
            case "XL": scalingFactor = ImageScalingFactor.XLARGE; break;
        }
        byte[] thumbnail = FileUtility.resizeThumbnail(photo.getImageBytes(), scalingFactor);
        resp.getOutputStream().write(thumbnail);
    }

}

