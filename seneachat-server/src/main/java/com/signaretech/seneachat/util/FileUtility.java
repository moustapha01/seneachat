package com.signaretech.seneachat.util;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileUtility {

    private static final Integer IMAGE_DB_WIDTH_LANDSCAPE = 512;
    private static final Integer IMAGE_DB_HEIGHT_LANDSCAPE = 384;
    private static final Integer IMAGE_DB_WIDTH_PORTRAIT = 384;
    private static final Integer IMAGE_DB_HEIGHT_PORTRAIT = 512;
    private static final Integer IMAGE_DB_WIDTH_SQUARE = 512;
    private static final Integer IMAGE_DB_HEIGHT_SQUARE = 512;

    private static final Integer IMAGE_FIXED_WIDTH_LANDSCAPE = 128;
    private static final Integer IMAGE_FIXED_HEIGHT_LANDSCAPE = 96;
    private static final Integer IMAGE_FIXED_WIDTH_PORTRAIT = 96;
    private static final Integer IMAGE_FIXED_HEIGHT_PORTRAIT = 128;
    private static final Integer IMAGE_FIXED_WIDTH_SQUARE = 128;
    private static final Integer IMAGE_FIXED_HEIGHT_SQUARE = 128;

    public static byte[] convertToBytes(File file){

        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            fis.read(bytes);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bytes;

    }

    public static byte[] resizeForDb(byte[] image) throws IOException {

        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image));
        Integer width = originalImage.getWidth();
        Integer height = originalImage.getHeight();


        ByteArrayOutputStream bos = null;
        InputStream is = null;
        byte[] resizedImage;
        try {
            bos = new ByteArrayOutputStream();
            is = new ByteArrayInputStream(image);

            if(width > height){
                Thumbnails.of(is).size(IMAGE_DB_WIDTH_LANDSCAPE, IMAGE_DB_HEIGHT_LANDSCAPE)
                        .keepAspectRatio(true).toOutputStream(bos);
            }else if(width < height){
                Thumbnails.of(is).size(IMAGE_DB_WIDTH_PORTRAIT, IMAGE_DB_HEIGHT_PORTRAIT)
                        .keepAspectRatio(true).toOutputStream(bos);
            }else{
                Thumbnails.of(is).size(IMAGE_DB_WIDTH_SQUARE, IMAGE_DB_HEIGHT_SQUARE)
                        .keepAspectRatio(true).toOutputStream(bos);
            }

            resizedImage = bos.toByteArray();
        } finally {
            if(bos != null){
                bos.close();
            }

            if(is != null){
                is.close();
            }
        }
        return resizedImage;
    }

    public static byte[] resizeThumbnail(byte[] image, String scalingFactor) throws IOException {

        Integer scale = 3;
        if(scalingFactor != null && scalingFactor.equals("small")){
            scale = 4;
        } else if(scalingFactor != null && scalingFactor.equals("large")){
            scale = 2;
        }  else if(scalingFactor != null && scalingFactor.equals("xlarge")){
            scale = 1;
        }
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image));
        Integer width = originalImage.getWidth();
        Integer height = originalImage.getHeight();


        ByteArrayOutputStream bos = null;
        InputStream is = null;
        byte[] resizedImage;
        try {
            bos = new ByteArrayOutputStream();
            is = new ByteArrayInputStream(image);

            if(scalingFactor != null && scalingFactor.equals("fixed")){
                if(width > height){
                    Thumbnails.of(is).forceSize(IMAGE_FIXED_WIDTH_LANDSCAPE, IMAGE_FIXED_HEIGHT_LANDSCAPE)
                            .toOutputStream(bos);
                }else if(width < height){
                    Thumbnails.of(is).forceSize(IMAGE_FIXED_WIDTH_PORTRAIT, IMAGE_FIXED_HEIGHT_PORTRAIT)
                            .toOutputStream(bos);
                }else{
                    Thumbnails.of(is).forceSize(IMAGE_FIXED_WIDTH_SQUARE, IMAGE_FIXED_HEIGHT_SQUARE)
                            .toOutputStream(bos);
                }
            }else{
                if(width > height){
                    Thumbnails.of(is).size(IMAGE_DB_WIDTH_LANDSCAPE/scale, IMAGE_DB_HEIGHT_LANDSCAPE/scale)
                            .keepAspectRatio(true).toOutputStream(bos);
                }else if(width < height){
                    Thumbnails.of(is).size(IMAGE_DB_WIDTH_PORTRAIT/scale, IMAGE_DB_HEIGHT_PORTRAIT/scale)
                            .keepAspectRatio(true).toOutputStream(bos);
                }else{
                    Thumbnails.of(is).size(IMAGE_DB_WIDTH_SQUARE/scale, IMAGE_DB_HEIGHT_SQUARE/scale)
                            .keepAspectRatio(true).toOutputStream(bos);
                }
            }


            resizedImage = bos.toByteArray();
        } finally {
            if(bos != null){
                bos.close();
            }

            if(is != null){
                is.close();
            }
        }
        return resizedImage;
    }

    public static byte[] scaleDownSmall(byte[] image, ImageScalingFactor scalingFactor) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream fis = new ByteArrayInputStream(image);
        Thumbnails.of(fis).size(scalingFactor.getWidth(), scalingFactor.getHeight()).
                keepAspectRatio(true).toOutputStream(bos);
        return bos.toByteArray();
    }

    public File byteToFile(byte[] bytes){
        return null;
    }
}
