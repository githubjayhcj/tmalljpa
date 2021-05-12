package com.taobao.tmalljpa.util;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    //写入图片(或覆盖)
    public static boolean saveImage(String staticClassPathFolder, String fileName, MultipartFile image){
        try {
            String staticResourcePath = ResourceUtils.getURL("classpath:"+staticClassPathFolder).getPath().replace("%20"," ").replace("/","\\");
            //判断是否为jpg（统一为该格式）
            String fileOriginName = image.getOriginalFilename();
            //将文件先写入（或者将file转换为multipartFile）
            File file = new File(staticResourcePath,fileName+".jpg");
            if(!file.exists()){
                file.mkdirs();
            }
            image.transferTo(file);
            //图片转换格式，并覆盖
            if(fileOriginName.substring(fileOriginName.lastIndexOf(".")+1).equals("jpg")){
                BufferedImage bufferedImage = ImageUtil.change2jpg(file);
                ImageIO.write(bufferedImage,"jpg",file);
                return true;
            }
        }catch (IOException io){
            io.getMessage();
        }
        return false;
    }

    //删除图片
    public static boolean deleteImage(String staticClassPathFolder, String fileName){
        try{
            String staticResourcePath = ResourceUtils.getURL("classpath:"+staticClassPathFolder).getPath().replace("%20"," ").replace("/","\\");
            File file = new File(staticResourcePath,fileName+".jpg");
            ToolClass.out("删除图片"+file.exists());
            if(file.exists()){
                boolean infer = file.delete();
                if (infer){
                    return true;
                }
            }
        }catch (IOException io){
            io.getMessage();
            io.printStackTrace();
        }
        return false;
    }

    //转换图片格式
    public static BufferedImage change2jpg(File f) {
        try {
            Image i = Toolkit.getDefaultToolkit().createImage(f.getAbsolutePath());
            PixelGrabber pg = new PixelGrabber(i, 0, 0, -1, -1, true);
            pg.grabPixels();
            int width = pg.getWidth(), height = pg.getHeight();
            final int[] RGB_MASKS = { 0xFF0000, 0xFF00, 0xFF };
            final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
            DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
            BufferedImage img = new BufferedImage(RGB_OPAQUE, raster, false, null);
            return img;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    //图片尺寸
    public static void resizeImage(File srcFile, int width,int height, File destFile) {
        try {
            if(!destFile.getParentFile().exists())
                destFile.getParentFile().mkdirs();
            Image i = ImageIO.read(srcFile);
            i = resizeImage(i, width, height);
            ImageIO.write((RenderedImage) i, "jpg", destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //图片尺寸
    public static Image resizeImage(Image srcImage, int width, int height) {
        try {

            BufferedImage buffImg = null;
            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            buffImg.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            return buffImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
