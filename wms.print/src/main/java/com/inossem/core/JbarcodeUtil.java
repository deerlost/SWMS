package com.inossem.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

/** 
* @author 高海涛 
* @version 2017年9月22日
* 条形码生成
*/
public class JbarcodeUtil {
	
	public static void createBarcode(String path,String msg){
		try {
			File file = new File(path);
			OutputStream ous = new FileOutputStream(file);
			Code128Bean bean = new Code128Bean();
			
			// 精细度
	        final int dpi = 150;
	        
	        // 配置对象
	        bean.setBarHeight(30);
	        bean.setModuleWidth(1);
	        bean.doQuietZone(false);
	        
	        bean.setFontSize(0);
	        
	        // 输出到流
	        BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, "image/png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

	        // 生成条形码
	        bean.generateBarcode(canvas, msg);

	        // 结束绘制
	        canvas.finish();
	        ous.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
