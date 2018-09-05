package com.inossem.core;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.File;

//import org.junit.Test;

public class PrintFileUtil {
	
    //@Test
    public void run () {
    	printFileAction("‪D:"+File.separator+"WMS"+File.separator+"excel"+File.separator+"excel"+File.separator+"20180623"+File.separator+"outandinstock_20180623103551.xls","HP LaserJet MFP M129-M134 PCLm-S");
    }

    /**
     * 打印Excel文件
     * @param filePath  文件路径
     */
    public static boolean printFileAction(String filePath){
        boolean returnFlg = false;
        try {
            ComThread.InitSTA();
            ActiveXComponent xl = new ActiveXComponent("Excel.Application");

            // 不打开文档
            Dispatch.put(xl, "Visible", new Variant(false));
            Dispatch workbooks = xl.getProperty("Workbooks").toDispatch();

            // win下路径处理(把根目录前的斜杠删掉)
           // filePath = filePath.replace("/F:/","F:/");

            // 判断文件是否存在
            boolean fileExistFlg = fileExist(filePath);
            if (fileExistFlg) {
                Dispatch excel=Dispatch.call(workbooks,"Open",filePath).toDispatch();
                // 开始打印
                Dispatch.get(excel,"PrintOut");
                returnFlg = true;
            }else {
            	System.out.println("文件不存在 file: "+filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 始终释放资源
            ComThread.Release();
        }
        return returnFlg;
    }



    /**
     * 打印Excel文件
     * @param filePath  文件路径
     */
    public static boolean printFileAction(String filePath,String printerName){
        boolean returnFlg = false;

        try {
            ComThread.InitSTA();
            ActiveXComponent xl = new ActiveXComponent("Excel.Application");

            // 不打开文档
            Dispatch.put(xl, "Visible", new Variant(false));
            Dispatch workbooks = xl.getProperty("Workbooks").toDispatch();
       
            // win下路径处理(把根目录前的斜杠删掉)
            //filePath = filePath.replace("/F:/","F:/");
            
            Object[] object = new Object[8];
            object[0] = Variant.VT_MISSING;
            object[1] = Variant.VT_MISSING;
            object[2] = Variant.VT_MISSING;
            object[3] = new Boolean(false);
            object[4] = printerName;
            object[5] = new Boolean(false);
            object[6] = Variant.VT_MISSING;
            object[7] = Variant.VT_MISSING;
            
            // 判断文件是否存在
            boolean fileExistFlg = fileExist(filePath);
            
            if (fileExistFlg) {
                Dispatch excel=Dispatch.call(workbooks,"Open",filePath).toDispatch();
                // 开始打印
                Dispatch.callN(excel,"PrintOut",object);
                returnFlg = true;
            }else {
            	System.out.println("文件不存在 file: "+filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 始终释放资源
            ComThread.Release();
        }
        return returnFlg;
    }


    /**
     * 判断文件是否存在.
     * @param filePath  文件路径
     * @return
     */
    private static boolean fileExist(String filePath){

        boolean flag = false;
        try {
            File file = new File(filePath);
            flag = file.exists();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
}