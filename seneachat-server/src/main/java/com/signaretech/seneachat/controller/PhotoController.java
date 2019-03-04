package com.signaretech.seneachat.controller;

import com.signaretech.seneachat.model.AdvertisementDTO;
import com.signaretech.seneachat.model.PhotoDTO;
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


    @GetMapping("/web/adphotos/{photoUuid}")
    public void loadPhoto(@PathVariable String photoUuid, HttpServletRequest req, HttpServletResponse resp) {

        AdvertisementDTO ad = (AdvertisementDTO)req.getSession().getAttribute("currAd");

        List<PhotoDTO> photos = ad.getPhotos();

        //PhotoDTO photo = photos.stream().filter( p -> p.getId().equals(UUID.fromString(photoUuid))).findFirst().get();

        PhotoDTO photo = photos.get(Integer.parseInt(photoUuid));

        try{
            writeImage(photo,resp, ImageScalingFactor.SMALL);
        }catch (IOException ie) {
            ie.printStackTrace();
        }

    }

    private void writeImage(PhotoDTO photo, HttpServletResponse resp,
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

