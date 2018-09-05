package com.inossem.wms.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.security.authentication.BadCredentialsException ;  

import com.inossem.wms.util.UtilToken;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean
{

   private final UserDetailsService userService;
   private String rsType = "1";

   public AuthenticationTokenProcessingFilter(UserDetailsService userService)
   {
      this.userService = userService;
   }


   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
         ServletException
   {
      HttpServletRequest httpRequest = this.getAsHttpRequest(request);
      HttpServletResponse httpResponse = (HttpServletResponse)response;
      String authToken = this.extractAuthTokenFromRequest(httpRequest);
      String userName = UtilToken.getUserNameFromToken(authToken);
      if (userName != null) {

         UserDetails userDetails= this.userService.loadUserByUsername(userName);

         if (UtilToken.validateToken(authToken, userDetails)) {
        	 //判断是否需要新建token
        	 
        	 //if(UtilToken.validateTokenForNewYn(authToken)){
    		 if(rsType.equals("1")){
    		 //请求为安卓端 10分钟 token存入header
    			 String newToken = UtilToken.createToken(userDetails,1000L*60*20);
    			 httpResponse.setHeader("X-Auth-Token", newToken);
    		 }else if(rsType.equals("2")){
    	     //请求为web端	60分钟 token存入cookie
    			 String newToken = UtilToken.createToken(userDetails,1000L*60*60);
    			 
    			 Cookie cookie = new Cookie("X-Auth-Token", newToken);  
                 cookie.setMaxAge(60 * 60);// 设置为60min  
                 cookie.setPath("/");  
                 httpResponse.addCookie(cookie); 
                 
    		 }
        		 
            //}
        	 
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
         }
         else{
        	  UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null);
              authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
              SecurityContextHolder.getContext().setAuthentication(authentication);
        	 //throw new BadCredentialsException ("InvalidateToken");
         }
      }else{
    	  UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null);
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
          SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      chain.doFilter(request, response);
   }


   private HttpServletRequest getAsHttpRequest(ServletRequest request)
   {
      if (!(request instanceof HttpServletRequest)) {
         throw new RuntimeException("Expecting an HTTP request");
      }

      return (HttpServletRequest) request;
   }


   private String extractAuthTokenFromRequest(HttpServletRequest httpRequest)
   {
      /* Get token from header */
      String authToken = httpRequest.getHeader("X-Auth-Token");

          
      if (authToken == null) {
    	  rsType = "2";
    	  Cookie[] cookies = httpRequest.getCookies();
    	  if (null!=cookies) {  
    		  for(Cookie cookie : cookies){
    			  if("X-Auth-Token".equals(cookie.getName())){
    				  authToken=cookie.getValue();
    			  }
              }  
          }
      }
      /* If token not found get it from request parameter */
      if (authToken == null) {
          authToken = httpRequest.getParameter("token");
      }

      return authToken;
   }
}
