package com.signaretech.seneachat.util;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileUtility {

    private static final Integer DB_WIDTH_LANDSCAPE = 512;
    private static final Integer DB_HEIGHT_LANDSCAPE = 384;
    private static final Integer DB_WIDTH_PORTRAIT = 384;
    private static final Integer DB_HEIGHT_PORTRAIT = 512;
    private static final Integer DB_WIDTH_SQUARE = 512;
    private static final Integer DB_HEIGHT_SQUARE = 512;

    private static final Integer WIDTH_LANDSCAPE = 128;
    private static final Integer FIXED_HEIGHT_LANDSCAPE = 96;
    private static final Integer FIXED_WIDTH_PORTRAIT = 96;
    private static final Integer FIXED_HEIGHT_PORTRAIT = 128;
    private static final Integer FIXED_WIDTH_SQUARE = 128;
    private static final Integer FIXED_HEIGHT_SQUARE = 128;

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

    /**
     * @param image, image bytes content
     * @param scalingFactor, size to scale the image
     * @return the resize image bytes scaled according to scalingFactor
     * @throws IOException
     */
    public static byte[] resizeThumbnail(byte[] image, ImageScalingFactor scalingFactor) throws IOException {

        Integer scale = 3;
        switch (scalingFactor) {
            case SMALL: scale = 4; break;
            case LARGE: scale = 2; break;
            case XLARGE: scale = 1; break;
        }

        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image));
        Integer width = originalImage.getWidth();
        Integer height = originalImage.getHeight();

        byte[] resizedImage;
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream is = new ByteArrayInputStream(image)){
            switch (scalingFactor){
                case FIXED:
                    if(width > height){
                        Thumbnails.of(is)
                                .forceSize(WIDTH_LANDSCAPE, FIXED_HEIGHT_LANDSCAPE)
                                .toOutputStream(bos);
                    }else if(width < height){
                        Thumbnails.of(is)
                                .forceSize(FIXED_WIDTH_PORTRAIT, FIXED_HEIGHT_PORTRAIT)
                                .toOutputStream(bos);
                    }else{
                        Thumbnails.of(is)
                                .forceSize(FIXED_WIDTH_SQUARE, FIXED_HEIGHT_SQUARE)
                                .toOutputStream(bos);
                    }
                    break;
                default:
                    if(width > height){
                        Thumbnails.of(is)
                                .size(DB_WIDTH_LANDSCAPE /scale, DB_HEIGHT_LANDSCAPE /scale)
                                .keepAspectRatio(true).toOutputStream(bos);
                    }else if(width < height){
                        Thumbnails.of(is)
                                .size(DB_WIDTH_PORTRAIT /scale, DB_HEIGHT_PORTRAIT /scale)
                                .keepAspectRatio(true).toOutputStream(bos);
                    }else{
                        Thumbnails.of(is)
                                .size(DB_WIDTH_SQUARE /scale, DB_HEIGHT_SQUARE /scale)
                                .keepAspectRatio(true).toOutputStream(bos);
                    }
            }
            resizedImage = bos.toByteArray();
        }
        return resizedImage;
    }
}
