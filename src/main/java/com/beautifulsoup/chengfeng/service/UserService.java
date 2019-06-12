package com.beautifulsoup.chengfeng.service;

import org.springframework.validation.BindingResult;

import com.beautifulsoup.chengfeng.controller.vo.PosterVo;
import com.beautifulsoup.chengfeng.controller.vo.UserVo;
import com.beautifulsoup.chengfeng.service.dto.UserDto;

public interface UserService
{
    PosterVo findUserByNickname();
    
    UserVo registryUserInfo(UserDto userDto, BindingResult result);
    
    UserVo updateUserInfo(UserVo userVo, BindingResult result);
    
    String resetPassword(String nickname, String rawPassword, String newPassword, String validateCode);
    
    void sendEmail(String nickname, String email);
}
