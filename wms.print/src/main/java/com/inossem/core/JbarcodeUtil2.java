package com.inossem.core;
import java.io.File;     
    
import com.google.zxing.BarcodeFormat;     
import com.google.zxing.MultiFormatWriter;     
import com.google.zxing.client.j2se.MatrixToImageWriter;     
import com.google.zxing.common.BitMatrix;     
    
/**
 * 条形码   
 * @author 高海涛
 *
 */
public class JbarcodeUtil2 {     
    
    /**   
     * 编码   
     * @param contents   
     * @param width   
     * @param height   
     * @param imgPath   
     */    
    @SuppressWarnings("deprecation")
	public static void encode(String contents, int width, int height, String imgPath) {     
    	
        try {     
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,     
                    BarcodeFormat.CODE_128, width, height, null);     
    
            MatrixToImageWriter.writeToFile(bitMatrix, "png", new File(imgPath));     
    
        } catch (Exception e) {     
            e.printStackTrace();     
        }     
    }       
}    