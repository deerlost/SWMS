package com.inossem.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;  

/** 
* @author 高海涛 
* @version 2017年9月21日
* 二维码生成
*/
public class QRcodeUtil {
	
	
  
    /** 
     * <span style="font-size:18px;font-weight:blod;">ZXing 方式生成二维码</span> 
     * @param text    <a href="javascript:void();">二维码内容</a> 
     * @param width    二维码宽 
     * @param height    二维码高 
     * @param outPutPath    二维码生成保存路径 
     * @param imageType        二维码生成格式 
     */  
    public static void zxingCodeCreate(String text, int width, int height, String outPutPath, String imageType){  
        Map<EncodeHintType, Object> his = new HashMap<EncodeHintType, Object>();  
        //设置编码字符集  
        his.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
        his.put(EncodeHintType.MARGIN,1);
        try {  
            //生成二维码  
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);  
              
            //获取二维码宽高  
            int codeWidth = encode.getWidth();  
            int codeHeight = encode.getHeight();  
              
            //将二维码放入缓冲流  
            BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);  
            for (int i = 0; i < codeWidth; i++) {  
                for (int j = 0; j < codeHeight; j++) {  
                    //循环将二维码内容定入图片  
                    image.setRGB(i, j, encode.get(i, j) ? Constant.BLACK : Constant.WHITE);  
                }  
            }  
            File outPutImage = new File(outPutPath);  
            //如果图片不存在创建图片  
            if(!outPutImage.exists())  
                outPutImage.createNewFile();  
            //将二维码写入图片  
            ImageIO.write(image, imageType, outPutImage);  
        } catch (WriterException e) {  
            e.printStackTrace();  
            System.out.println("二维码生成失败");  
        } catch (IOException e) {  
            e.printStackTrace();  
            System.out.println("生成二维码图片失败");  
        }  
    }  
      
    /** 
     * <span style="font-size:18px;font-weight:blod;">二维码解析</span> 
     * @param analyzePath    二维码路径 
     * @return 
     * @throws IOException 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    public static Object zxingCodeAnalyze(String analyzePath) throws Exception{  
        MultiFormatReader formatReader = new MultiFormatReader();  
        Object result = null;  
        try {  
            File file = new File(analyzePath);  
            if (!file.exists())  
            {  
                return "二维码不存在";  
            }  
            BufferedImage image = ImageIO.read(file);  
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            Binarizer binarizer = new HybridBinarizer(source);    
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);  
            Map hints = new HashMap();  
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
            result = formatReader.decode(binaryBitmap, hints);  
        } catch (NotFoundException e) {  
            e.printStackTrace();  
        }    
        return result;  
    }  
}
