package com.beautifulsoup.chengfeng.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.beautifulsoup.chengfeng.common.ResponseResult;
import com.beautifulsoup.chengfeng.security.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TokenClearLogoutHandler implements LogoutHandler {
	
	private UserInfoService userInfoService;
	
	public TokenClearLogoutHandler(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {


		try {
			if (authentication==null){
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=UTF-8");
				ResponseResult responseResult=ResponseResult.createByError(HttpStatus.OK.value(),"用户登出失败");

				ObjectMapper mapper=new ObjectMapper();
				mapper.writeValue(response.getWriter(),responseResult);
				response.getWriter().flush();
			}else{
				clearToken(authentication);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=UTF-8");
				ResponseResult responseResult=ResponseResult.createByError(HttpStatus.OK.value(),"用户登出成功");

				ObjectMapper mapper=new ObjectMapper();
				mapper.writeValue(response.getWriter(),responseResult);
			}
//			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	protected void clearToken(Authentication authentication) {
		if(authentication == null)
			return;
		SecurityContextHolder.getContext().setAuthentication(null);
		UserDetails user = (UserDetails)authentication.getPrincipal();
		if(user!=null && user.getUsername()!=null)
		    userInfoService.deleteUserLoginInfo(user.getUsername());

	}

}
