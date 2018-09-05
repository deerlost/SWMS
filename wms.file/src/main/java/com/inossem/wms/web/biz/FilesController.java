package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.csvreader.CsvReader;
import com.inossem.wms.config.ServerConfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/file")
public class FilesController {
	private static final Logger logger = LoggerFactory.getLogger(FilesController.class);
	@Autowired
	private ServerConfig serverConfig;

	private String fileFormatWithExt = "%s.%s";
	private String pathFormat = "%s%c%s%c";
	private static final String ENCODE = "UTF-8";
	// private String rootPathFormat = "%s%s";

	/**
	 * 删除附件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public @ResponseBody Object remove(String file_id) {
		boolean obj = false;
		// 服务器文件路径
		try {

			String filePath = this.getFilePathByFileID(file_id);
			String fileName = this.getFileNameByFileID(file_id);
			String fileRootPath = this.getFileRootPath();
			if (filePath != null && fileName != null && fileRootPath != null) {
				// 服务器端文件全路径

				File file = new File(fileRootPath + filePath, fileName);

				if (file.exists()) {
					file.delete();
				}

				obj = true;
			}

		} catch (Exception e) {
			logger.error("", e);
		} finally {

		}
		return obj;
	}

	/**
	 * 文件上传功能
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String upload(@RequestParam("file") MultipartFile fileInClient, HttpServletRequest request,
			HttpServletResponse response) {
		String ret = "";
		String fileId = "";
		String originalFilename = "";
		long fileSize = 0;

		File fileInServer = null;
		try {
			String ext = null;
			originalFilename = fileInClient.getOriginalFilename();
			fileSize = fileInClient.getSize();
			int extIndex = originalFilename.lastIndexOf('.');
			if (originalFilename.length() - extIndex < 10) {
				ext = originalFilename.substring(extIndex + 1);
			}

			String uuid = this.getUUID();

			if (uuid != null) {
				// 服务器端文件全路径
				String fileRootPath = this.getFileRootPath();
				String filePath = this.getFilePath(uuid);
				String fileName = this.getFileName(uuid, ext);
				fileInServer = new File(fileRootPath + filePath, fileName);

				if (fileInServer != null) {
					if (!fileInServer.exists()) {
						fileInServer.mkdirs();
					}

					// MultipartFile自带的解析方法
					fileInClient.transferTo(fileInServer);
					fileId = (filePath + fileName).replace(File.separatorChar, '_');
				}
			}
			String format = request.getParameter("format");
			String fileName = request.getParameter("file_name");
			if (StringUtils.hasText(format)) {
				// ret = String.format(format, file_id,
				// java.net.URLEncoder.encode(originalFilename, ENCODE),
				// file_size);
				// "http://192.168.1.47:8080/wms/web/html/common/fillrest.html?fileid=%s&filename=%s&filesize=%d"
				String url = String.format(format, fileId, fileName, fileSize);
				// <script>location.href='http://61.134.122.146/wms/web/html/common/fillrest.html?fileid=%s&filename=%s&filesize=%d'"+"</"+"script>
				response.sendRedirect(url);
				// ret = java.net.URLEncoder.encode(url, ENCODE);
				// http://192.168.1.47:8080/wms/web/html/common/fillrest.html
			} else {
				ret = fileId;
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return ret;
	}

	private String upload(MultipartFile fileInClient) {
		String ret = "";
		File fileInServer = null;
		try {
			String ext = null;
			String originalFilename = fileInClient.getOriginalFilename();
			int extIndex = originalFilename.lastIndexOf('.');
			if (originalFilename.length() - extIndex < 10) {
				ext = originalFilename.substring(extIndex + 1);
			}

			String uuid = this.getUUID();

			if (uuid != null) {
				// 服务器端文件全路径
				String fileRootPath = this.getFileRootPath();
				String filePath = this.getFilePath(uuid);
				String fileName = this.getFileName(uuid, ext);
				fileInServer = new File(fileRootPath + filePath, fileName);

				if (fileInServer != null) {
					if (!fileInServer.exists()) {
						fileInServer.mkdirs();
					}

					// MultipartFile自带的解析方法
					fileInClient.transferTo(fileInServer);
					ret = (filePath + fileName).replace(File.separatorChar, '_');
				}
			}

		} catch (Exception e) {
			logger.error("", e);
		}
		return ret;
	}

	/**
	 * 文件下载功能
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		try {
			// String ext = request.getParameter("ext");
			String file_id = request.getParameter("file_id");

			String filePath = this.getFilePathByFileID(file_id);
			String fileName = this.getFileNameByFileID(file_id);
			String fileRootPath = this.getFileRootPath();

			if (filePath != null && fileName != null && fileRootPath != null) {
				// 服务器端文件全路径

				fileInServer = new File(fileRootPath + filePath, fileName);
				// 获取输入流
				bis = new BufferedInputStream(new FileInputStream(fileInServer));

				// 转码，免得文件名中文乱码
				String fileNameForUTF8 = URLEncoder.encode(fileName, ENCODE);
				// 设置文件下载头
				response.addHeader("Content-Disposition", "attachment;filename=" + fileNameForUTF8);

				// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
				response.setContentType("multipart/form-data");
				out = new BufferedOutputStream(response.getOutputStream());
				int len = 0;
				while ((len = bis.read()) != -1) {
					out.write(len);
					out.flush();
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	private String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		// char[] cs = uuid.toCharArray();
		if (uuid != null && uuid.length() == 32) {
			return uuid;
		}
		return null;
	}

	private String getFileName(String uuid, String ext) {
		if (ext == null || ext.trim().length() == 0) {
			return uuid;
		} else {
			return String.format(this.fileFormatWithExt, uuid, ext);
		}
	}

	private String getFilePathByFileID(String fileID) {
		if (fileID != null && fileID.length() > 6) {
			String first = fileID.substring(0, 2);
			String second = fileID.substring(3, 5);
			return String.format(this.pathFormat, first, File.separatorChar, second, File.separatorChar);
		}
		return null;
	}

	private String getFileNameByFileID(String fileID) {
		if (fileID != null && fileID.length() > 6) {
			return fileID.substring(6);
		}
		return null;
	}

	private String getFileRootPath() {
		String path = serverConfig.getAttachmentPath();
		if (path.endsWith(File.separator)) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	private String getFilePath(String uuid) {

		int first = 0;
		for (int i = 0; i < 16; i++) {
			first += Integer.parseInt(uuid.substring(i, i + 1), 16);
		}
		int second = 0;

		for (int i = 16; i < 32; i++) {
			second += Integer.parseInt(uuid.substring(i, i + 1), 16);
		}

		if (first < 255 && first >= 0 && second < 255 && second >= 0) {
			return String.format(this.pathFormat, Integer.toHexString(first), File.separatorChar,
					Integer.toHexString(second), File.separatorChar);
		}

		return null;
	}

	@RequestMapping(value = "/addwllistforcsv/{file_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addWLListForCSV(@PathVariable("file_id") String file_id) {
		JSONArray body = new JSONArray();
		File file = null;
		FileInputStream fileInputStream = null;
		try {
			file = new File("D:\\WMS\\file\\98\\79\\ce208f43ae9f4fde8028774f33cbbbf4.xls");

			fileInputStream = new FileInputStream(file);

			CsvReader csvReader = new CsvReader(fileInputStream, Charset.forName("GB2312"));

			// 读表头
			csvReader.readHeaders();
			while (csvReader.readRecord()) {
				// 读一整行
				// System.out.println(csvReader.getRawRecord());
				// 读这行的某一列
				System.out.println(csvReader.get("WERKS"));// 工厂
				System.out.println(csvReader.get("LGORT"));// 库存地点
				System.out.println(csvReader.get("MATNR"));// 物料编码
				System.out.println(csvReader.get("MAKTX"));// 物料描述
				System.out.println(csvReader.get("MEINS"));// 单位
				System.out.println(csvReader.get("MSEHL"));// 单位描述
				System.out.println(csvReader.get("BDMNG"));// 数量
				System.out.println(csvReader.get("ZCKJG"));// 参考价格
				System.out.println(csvReader.get("ZBZSM"));// 备注

			}

			// JSONObject wlxx;
			// for (MCHB mchb : list) {
			// wlxx = new JSONObject();
			// wlxx.put("matnr", mchb.getMatnr());
			// wlxx.put("maktx", mchb.getMaktx());
			// wlxx.put("meins", mchb.getMeins());
			// wlxx.put("msehl", mchb.getMseht());
			// wlxx.put("dqkc", mchb.getClabs().toString());
			// wlxx.put("werks", mchb.getWerks());
			// wlxx.put("name1", mchb.getWerks_name());
			// wlxx.put("lgort", mchb.getLgort());
			// wlxx.put("lgobe", mchb.getLgort_name());
			// wlxx.put("charg", mchb.getCharg());
			// wlxx.put("anlkl", mchb.getZasseta());
			// wlxx.put("zckjg", 0);
			// wlxx.put("andec", mchb.getAndec());// 计量单位浮点数
			// body.add(wlxx);
			// }

		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		JSONObject head = new JSONObject();
		head.put("error_code", 0);
		head.put("error_string", "Success");
		head.put("page_index", 0);
		head.put("page_size", 1);
		head.put("total", 5);
		// JSONObject obj = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("head", head);
		obj.put("body", body);
		return obj;
	}
}
