package com.inossem.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * 报表新增生成打印基类
 * @author 高海涛
 */
public abstract class PrintBaseCreate {
	
	
	 /**
     * 创建头
     */
	public abstract int createHeader(Workbook workbook, List<Map<String, Object>> list, List<String> orderList);
    
	/**
     * 创建页尾
     */
	public abstract int createFooter(Workbook workbook, List<Map<String, Object>> list, List<String> orderList,int count);
	
	/**
     * 创建头List
     */
    public abstract Map<String, String> getHeaderList();
    
    /**
     * 创建头顺序List
     */
    public abstract List<String> getHeaderOrderList();
    
    /**
     * 创建页尾List
     */
    public abstract Map<String, String> getFooterList();
    
    /**
     * 创建页尾顺序List
     */
    public abstract List<String> getFooterOrderList();
    
    /**
     * 关系List
     */
    public abstract Map<String, String> getDataMappingList();
    
    /**
     * 排序List
     */
    public abstract List<String> getOrderList();
    
    /**
     * 设置列宽List
     */
    public abstract Map<String,Integer> getWidthList();
    
    /**
     * 获取Excel报表的URL
     * @param <T>
     * @param title
     * @param entityList
     * @param relateionList
     * @param orderList
     * @return
     * @throws Exception
     */
	public <T> String getExcelReportUrl(String title,
			List<Map<String,Object>> entityList, Map<String, String> relationMap, List<String> orderList) throws Exception {
			if(StringUtils.isBlank(title) || CollectionUtils.isEmpty(relationMap)) {
			throw new Exception("传入的参数存在空值");
			}
		return getExcelReportUrl(title,
				getTransformDataList(entityList, relationMap),orderList,
				null,null,
				null,null);
			}
    
	public <T> String getExcelReportUrl(String title,
    									List<Map<String,Object>> entityList, Map<String, String> relationMap, List<String> orderList, 
    									List<Map<String,Object>> headerEntityList, Map<String, String> headerRelationMap, List<String> headerOrderList) throws Exception {
    	if(StringUtils.isBlank(title) || CollectionUtils.isEmpty(relationMap)) {
    		throw new Exception("传入的参数存在空值");
    	}
		return getExcelReportUrl(title,
					    		getTransformDataList(entityList, relationMap),orderList,
					    		getTransformDataList(headerEntityList,headerRelationMap),headerOrderList,
					    		null,null);
    }
    
	public <T> String getExcelReportUrl(String title,
    									List<Map<String,Object>> entityList, Map<String, String> relationMap, List<String> orderList, 
    									List<Map<String,Object>> headerEntityList, Map<String, String> headerRelationMap, List<String> headerOrderList,
    									List<Map<String,Object>> footerEntityList, Map<String, String> footerRelationMap, List<String> footerOrderList) throws Exception {
    	if(StringUtils.isBlank(title) || CollectionUtils.isEmpty(relationMap)) {
    		throw new Exception("传入的参数存在空值");
    	}
		return getExcelReportUrl(title,
    							getTransformDataList(entityList, relationMap), orderList,
    							getTransformDataList(headerEntityList,headerRelationMap),headerOrderList,
    							getTransformDataList(footerEntityList,footerRelationMap),footerOrderList);
    }
    
    /**
     * 获取PDF报表的URL
     * @param <T>
     * @param list
     * @param title
     * @return
     * @throws Exception
     */
	public <T> String getPdfReportUrl(String title,
    								List<Map<String,Object>> entityList, Map<String, String> relationMap, List<String> orderList, 
    								List<Map<String,Object>> headerEntityList, Map<String, String> headerRelationMap, List<String> headerOrderList) throws Exception {
    	if(StringUtils.isBlank(title) || CollectionUtils.isEmpty(relationMap)) {
    		throw new Exception("传入的参数存在空值");
    	}
		String excelUrl = getExcelReportUrl(title,
							    			getTransformDataList(entityList, relationMap), orderList,
							    			getTransformDataList(headerEntityList,headerRelationMap),headerOrderList,
							    			null,null);
		return getPdfReportUrl(excelUrl);
    }
    
    
    /**
     * 获取Excel报表的URL 组装关系后
     * @param list
     * @param title
     * @param orderList  表头显示顺序的list
     * @return
     * @throws Exception
     */
	private String getExcelReportUrl(String title,
						    		List<Map<String, Object>> list,List<String> orderList,
						    		List<Map<String, Object>> headerList,List<String> headerOrderList,
						    		List<Map<String, Object>> footerList,List<String> footerOrderList) throws Exception {
    	if(StringUtils.isBlank(title)) {
    		throw new Exception("传入的参数存在空值");
    	}
    	Workbook workbook = wrapDataList2Workbook(title, list,orderList,headerList,headerOrderList,footerList,footerOrderList);
    	//创建文件，并获取路径
    	String path = getWorkbookFinalUrl(workbook);
    	return formatPath2Url(path);
    }
    
    /**
     * 通过Excel报表的URL获取到对应PDF报表的URL
     * @param excelUrl
     * @return
     * @throws Exception
     */
	protected String getPdfReportUrl(String excelUrl) throws Exception {
    	if(StringUtils.isBlank(excelUrl)) {
    		throw new Exception("传入的参数存在空值");
    	}
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    	String excelPath = request.getServletContext().getRealPath("") + formatUrl2Path(excelUrl);
    	String pdfUrl = excelUrl.substring(0, excelUrl.lastIndexOf(Constant.OUTPUTFILE_TYPE_XLS)) + Constant.OUTPUTFILE_TYPE_PDF;
    	String pdfPath = request.getServletContext().getRealPath("") + formatUrl2Path(pdfUrl);
		File inputFile = new File(excelPath);
		File outputFile = new File(pdfPath);
		// connect to an OpenOffice.org instance running on port 8100
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		connection.connect();
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		converter.convert(inputFile, outputFile);
		connection.disconnect();
		return pdfUrl;
    }
    
    /**
     * 把报表格式的数据封装成工作簿
     * @param list
     * @param title
     * @return
     */
    private Workbook wrapDataList2Workbook(String title, 
    										List<Map<String, Object>> list,List<String> orderList,
    										List<Map<String, Object>> headerList,List<String> headerOrderList,
    										List<Map<String, Object>> footerList,List<String> footerOrderList) {
		Workbook workbook = new HSSFWorkbook();
		//创建Excel文档
		createWorkbook(workbook, title);
		//创建一个工作页
		createSheet(workbook, title);

    	
    	int count = createHeader(workbook, headerList, headerOrderList);
    	
    	count ++;
		//创建标题行
    	count = createTitleRow(workbook, list, orderList,count);
    	
    	count ++;
		//创建数据行
    	count = createDataRow(workbook, list, orderList,count);
		
		count = createFooter(workbook, headerList, headerOrderList,count);
		//设置工作页样式
		setSheetStyle(workbook, list);
		//设置工作页打印设置
		setSheetPrintSetup(workbook, list);
		
		count++;
		//设置文档样式
		setWorkbookStyle(workbook, list, orderList,count);
		
		return workbook;
    }
    
    /**
     * 创建文档
     * @param workbook
     * @return
     */
    private void createWorkbook(Workbook workbook, String title) {
    	workbook.createName().setNameName(title);
    }
    
    /**
     * 创建工作页
     * @param workbook
     * @return
     */
    private void createSheet(Workbook workbook, String title) {
    	workbook.createSheet(title);
    }
    
   
    
    /**
     * 创建标题行
     * @param workbook
     * @param list
     * @return
     */
    private  int createTitleRow(Workbook workbook, List<Map<String, Object>> list, List<String> orderList,int count) {
    	Sheet sheet = workbook.getSheetAt(0);
    	Row titleRow = sheet.createRow(count);//下标为0的行开始
    	titleRow.setHeight((short)540);
		String[] titleArray = getTitleArray(list, orderList);
		CellStyle style = getTitleCellStyle(workbook);
		for(int i=0; i<titleArray.length; i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(new HSSFRichTextString(titleArray[i]));
		}
		
		return count;
    }
    
    /**
     * 创建数据行
     * @param workbook
     * @param list
     * @return
     */
    private int createDataRow(Workbook workbook, List<Map<String, Object>> list, List<String> orderList,int count) {
    	Sheet sheet = workbook.getSheetAt(0);
    	String[] titleArray = getTitleArray(list, orderList);
    	CellStyle style = getDataCellStyle(workbook);
    	Map<String,Integer> widthList = getWidthList();
    	
    	for(int i=0; i<list.size(); i++) {
			//创建一行
			Row dataRow = sheet.createRow(count+i);
			dataRow.setHeight((short)540);
			//得到要插入的每一条记录，在一行内循环创建
			for(int j=0; j<titleArray.length; j++) {
				sheet.setColumnWidth(j, widthList.get(titleArray[j])*300);
				
				String value = (String)list.get(i).get(titleArray[j]);
				value = value==null ? "" : value;
				Cell cell = dataRow.createCell(j);
				cell.setCellStyle(style);
				cell.setCellValue(value);
			}
		}
    	
    	return count+ list.size();
    }
    
    
    /**
     * 获取表头行样式
     * @param workbook
     * @return
     */
    @SuppressWarnings("deprecation")
    public CellStyle getHeaderCellStyle(Workbook workbook) {
    	// 生成一个样式
        CellStyle style = workbook.createCellStyle();
        
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex()); 
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        Font font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 16);
        font.setFontName("微软雅黑");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        
    	HSSFDataFormat format = (HSSFDataFormat) workbook.createDataFormat(); 
    	style.setDataFormat(format.getFormat("@"));
    	return style;
    }
    
    /**
     * 获取标题行样式
     * @param workbook
     * @return
     */
    @SuppressWarnings("deprecation")
	public CellStyle getTitleCellStyle(Workbook workbook) {
    	// 生成一个样式
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        // 生成一个字体
        Font font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        
    	HSSFDataFormat format = (HSSFDataFormat) workbook.createDataFormat(); 
    	style.setDataFormat(format.getFormat("@"));
    	return style;
    }
    
    /**
     * 获取数据行样式
     * @param workbook
     * @return
     */
    @SuppressWarnings("deprecation")
    public CellStyle getDataCellStyle(Workbook workbook) {
    	// 生成一个样式
    	CellStyle style = workbook.createCellStyle();
    	// 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
    	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setWrapText(true);
        // 生成另一个字体
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);
        
        HSSFDataFormat format = (HSSFDataFormat) workbook.createDataFormat(); 
    	style.setDataFormat(format.getFormat("@"));
    	
    	return style;
    }
    
    /**
     * 设置工作页样式
     * @param workbook
     * @param list
     * @return
     */
    private void setSheetStyle(Workbook workbook, List<Map<String, Object>> list) {
    	Sheet sheet = workbook.getSheetAt(0);
    	sheet.setDefaultColumnWidth(20);//默认列宽度
    	sheet.setAutobreaks(true);//自动翻页
    }
    
    /**
     * 设置工作页打印设置
     * @param workbook
     * @param list
     * @return
     */
    public void setSheetPrintSetup(Workbook workbook, List<Map<String, Object>> list) {
    	PrintSetup print = workbook.getSheetAt(0).getPrintSetup();
    	print.setLandscape(true);//横向
    	print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);//A4纸张
    	print.setFitWidth((short) 1);//将所有列调整为一页
    	print.setFitHeight((short) 0);//将所有列调整为一页（0表示不设置页数）
    	
    	HSSFFooter footer = (HSSFFooter )workbook.getSheetAt(0).getFooter() ;
    	footer.setCenter( "Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages() ); 
	}
    
    /**
     * 设置文档样式
     * @param workbook
     * @param list
     * @return
     */
    private void setWorkbookStyle(Workbook workbook, List<Map<String, Object>> list, List<String> orderList,int c) {
    	workbook.setPrintArea(0, 0, getTitleArray(list, orderList).length-1, 0, c);//设置打印区域
    }
    
    /**
     * 获取标题数组
     * @param list
     * @return
     */
    public String[] getTitleArray(List<Map<String, Object>> list, List<String> orderList) {
		String[] array =  null;
		if(!CollectionUtils.isEmpty(orderList)) {
			array = new String[orderList.size()];
			int i = 0;
			for(String key : orderList) {
				array[i] = key;i++;
			}
		} else {
			Map<String, Object> map = list.get(0);
			array = new String[map.size()];
			int i = 0;
			for (String key : map.keySet()) {
				array[i] = key;i++;
			}
		}
		return array;
	}
	
	/**
	 * 获取工作簿所在目录
	 * @param workbook
	 * @return
	 */
	private String getWorkbookDirectory() {
		return Constant.OUTPUTFILE_TABLE_PATH + getDateFolder() + File.separator;
	}
	
	/**
	 * 获取工作簿名称
	 * @param workbook
	 * @return
	 */
	private String getWorkbookName(Workbook workbook) {
		return workbook.getNameAt(0).getNameName() + "_" + getFormatedDate() + Constant.OUTPUTFILE_TYPE_XLS;
	}
	
	/**
	 * 获取工作簿URL
	 * @param workbook
	 * @return
	 */
	private String getWorkbookUrl(Workbook workbook) {
		return getWorkbookDirectory() + getWorkbookName(workbook);
	}
    
	/**
	 * 获取工作簿最终URL
	 * @param workbook
	 * @return
	 */
    private String getWorkbookFinalUrl(Workbook workbook) throws Exception {
		String url = getWorkbookUrl(workbook);
		createWorkbookFile(workbook, url);
		return url;
	}
    
    /**
     * 创建工作簿文件
     * @param workbook
     * @param url
     * @throws Exception
     */
    private void createWorkbookFile(Workbook workbook, String url) throws Exception {
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
    	String path = request.getServletContext().getRealPath("") + formatUrl2Path(url);
    	createWorkbookDirectory(workbook);
    	OutputStream out = new FileOutputStream(path);
		workbook.write(out);
		if(out!=null) {
			out.close();
			out = null;
		}
	}
    
    /**
     * 创建目录
     * @param workbook
     * @throws IOException
     */
	private void createWorkbookDirectory(Workbook workbook) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String directory = request.getServletContext().getRealPath("") + getWorkbookDirectory();
		File file = new File(directory);
		if(!file.isDirectory()) {
			FileUtils.forceMkdir(file);
		}
	}
	
	/**
	 * 获得"20150805"这种形式的日期
	 * @return
	 */
	private String getDateFolder() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	
	/**
	 * 获得"20150818173319"这种形式的日期
	 * @return
	 */
	private String getFormatedDate() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	/**
	 * 将真实路径分隔符转化成URL形式
	 * @param path
	 * @return
	 */
	private String formatPath2Url(String path) {
		return path.replace(File.separator, "/");
	}
	
	/**
	 * 将URL分隔符转化成真实路径形式
	 * @param url
	 * @return
	 */
	private String formatUrl2Path(String url) {
		return url.replace("/", File.separator);
	}
	
	/**
     * 转换map
     * @param <T>
     * @param entityList
     * @param relationMap
     * @return
     * @throws Exception
     */
    private <T> List<Map<String, Object>> getTransformDataList(List<Map<String,Object>> entityList, Map<String, String> relationMap) throws Exception {
		//1、遍历entityList
		//2、根据relateionList生成Map<String, String>
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		for(Map<String,Object> instance : entityList) {
			map = new HashMap<String,Object>();
			for(String key : instance.keySet()) {
				map.put(relationMap.get(key), instance.get(key));
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 根据真实存放文件的URL删除对应文件
	 * @return
	 * @throws Exception 
	 */
	public void doCleanTempFiles() throws Exception {
		//临时文件夹路径
		String path = getRootPath() + Constant.OUTPUTFILE_TABLE_PATH;
		File pathFile = new File(path);
		if(pathFile.isDirectory()) {
			//临时文件夹下当天文件夹路径
			String pathToday = path + File.separator + getDateFolder();
			//列出所有文件夹
			File[] fileArray = pathFile.listFiles();
			for(File file : fileArray) {
				if(!StringUtils.equals(file.getPath(), pathToday)) {
					FileUtils.deleteDirectory(file);
				}
			}
		}
	}
	
	/**
	 * 得到项目根目录
	 * @return
	 */
	private String getRootPath() {
		String path = this.getClass().getResource("/").getPath();
		path = path.substring(1, path.indexOf("WEB-INF"));
		path = path.replace("%20", " ");
		path = formatUrl2Path(path);
		return path;
	}
    
}
