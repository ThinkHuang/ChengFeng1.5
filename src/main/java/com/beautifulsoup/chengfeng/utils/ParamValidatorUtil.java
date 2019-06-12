package com.beautifulsoup.chengfeng.utils;

import org.springframework.validation.BindingResult;

import com.beautifulsoup.chengfeng.exception.ParamException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParamValidatorUtil {

    public static void validateBindingResult(BindingResult result){

        if (result.hasErrors()){
//            List<String> errors= Lists.newArrayList();
            result.getAllErrors().stream().forEach(error->{
                String errorMsg = error.getDefaultMessage();
                log.error(errorMsg);
                throw new ParamException(error.getDefaultMessage());
//                errors.add(errorMsg);
            });
//            if(!CollectionUtils.isEmpty(errors)) {
//
//            }
        }
    }
}
