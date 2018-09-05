package com.inossem.wms.auth.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang3.StringUtils;
import com.inossem.wms.util.UtilJSONConvert;

/**
 * 修改返回的JSON结构中的key，使key值变为snake形式返回给客户端的filter
 * @author wang.ligang
 */
public class ModifyJsonKeyFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
								FilterChain chain) throws IOException, ServletException {
		// 转换Request
		HttpServletRequest hrequest = (HttpServletRequest)request;
		// 判断请求的ContentType是否为application/json
    	if(hrequest.getContentType() != null && ("application/json; charset=utf-8".equalsIgnoreCase(hrequest.getContentType().trim())
				|| "application/json;charset=utf-8".equalsIgnoreCase(hrequest.getContentType().trim())
				|| "application/json".equalsIgnoreCase(hrequest.getContentType()))) {
    		// 
    		ByteResponseWrapper brw = new ByteResponseWrapper(
    				(HttpServletResponse) response);
    		// 继续其他的filter处理
    		chain.doFilter(request, brw);
    		// 通过response的包装类得到返回的字符串
    		String out = new String(brw.getBytes());
    		if (StringUtils.isBlank(out)) {
    			out = "";
    		}
    		
    		// 判断字符串是否以{}开头和结尾，是，则认为是json结构。注：不加在传递token时报错
    		if(out.length() > 0 && out.trim().startsWith("{") && out.endsWith("}")) {
    			response.setContentType("application/json; charset=utf-8");
    			// 将json字符结构的key转换为snake形式
    			Object newOut = UtilJSONConvert.convert(out);
        		String strOut = newOut.toString();
        		response.setContentLength(strOut.getBytes().length);
        		response.getOutputStream().write(strOut.getBytes());
    		} else {
//    			response.setContentType("application/json; charset=utf-8");
    			response.setContentLength(out.getBytes().length);
        		response.getOutputStream().write(out.getBytes());
    		}
    	} else {
    		chain.doFilter(request, response);
    	}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	/**
	 * Response的包装类，可以修改返回的流
	 * @author wang.ligang
	 *
	 */
	class ByteResponseWrapper extends HttpServletResponseWrapper {
		private PrintWriter writer;
		private ByteOutputStream output;
		// 请求返回的code
		private int code = 200;

		public byte[] getBytes() {
			writer.flush();
			return output.getBytes();
		}

		public ByteResponseWrapper(HttpServletResponse response) {
			super(response);
			output = new ByteOutputStream();
			writer = new PrintWriter(output);
		}

		@Override
		public PrintWriter getWriter() {
			return writer;
		}


		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return output;
		}

		public int getStatus() {
			return code;
		}


		@Override
		public void setStatus(int sc) {
			this.code = sc;
		}

		@Override
		public void setStatus(int sc, String message) {
			this.code = sc;
		}

		@Override
		public void sendError(int sc) throws IOException {
			this.code = sc;
		}

		@Override
		public void sendError(int sc, String msg) throws IOException {
			this.code = sc;
		}
	}

	/**
	 * Servlet的输出流封装类
	 * @author wang.ligang
	 *
	 */
	static class ByteOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream bos = new ByteArrayOutputStream();

		@Override
		public void write(int b) throws IOException {
			bos.write(b);
		}

		public byte[] getBytes() {
			return bos.toByteArray();
		}
	}
}