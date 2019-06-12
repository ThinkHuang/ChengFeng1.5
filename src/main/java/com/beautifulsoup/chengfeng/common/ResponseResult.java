package com.beautifulsoup.chengfeng.common;

import java.io.Serializable;

import com.beautifulsoup.chengfeng.enums.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

/**
 * <p>
 * Title: ResponseResult
 * </p>
 * <p>
 * Description: 统一响应封装
 * </p>
 * <p>
 * Author: BeautifulSoup
 * </p>
 * <p>
 * Time: 2019年2月12日 下午3:17:25
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
public class ResponseResult<T> implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -4892431012830978600L;
    
    private int status;
    
    private String message;
    
    private transient T data;
    
    private ResponseResult(int status)
    {
        this.status = status;
    }
    
    private ResponseResult(int status, String message)
    {
        this.message = message;
        this.status = status;
    }
    
    private ResponseResult(int status, String message, T data)
    {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    @JsonIgnore
    public boolean isSuccess()
    {
        return this.status == ResponseCode.SUCCESS.getCode();
    }
    
    public static <T> ResponseResult<T> createBySuccess()
    {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode());
    }
    
    public static <T> ResponseResult<T> createBySuccess(T data)
    {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(), data);
    }
    
    public static <T> ResponseResult<T> createBySuccess(String msg)
    {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), msg);
    }
    
    public static <T> ResponseResult<T> createBySuccess(String msg, T data)
    {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }
    
    public static <T> ResponseResult<T> createBySuccess(int code, String message, T data)
    {
        return new ResponseResult<>(code, message, data);
    }
    
    public static <T> ResponseResult<T> createByError()
    {
        return new ResponseResult<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }
    
    public static <T> ResponseResult<T> createByErrorMessage(String errorMessage)
    {
        return new ResponseResult<>(ResponseCode.ERROR.getCode(), errorMessage);
    }
    
    public static <T> ResponseResult<T> createByError(int errorCode, String errorMessage)
    {
        return new ResponseResult<>(errorCode, errorMessage);
    }
    
    public static <T> ResponseResult<T> createByError(int errorCode, String errorMessage, T data)
    {
        return new ResponseResult<>(errorCode, errorMessage, data);
    }
}
