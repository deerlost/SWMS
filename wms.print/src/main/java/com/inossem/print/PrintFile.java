package com.inossem.print;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inossem.core.PrintFileUtil;

/**
 * @author sw
 * @version 2018-06-27
 *
 */
@Controller
public class PrintFile {
	
	@RequestMapping("/test")
    public void print(){
		PrintFileUtil.printFileAction("C:/Users/sw/Desktop/包装物计量单位.xls");	
    }
}