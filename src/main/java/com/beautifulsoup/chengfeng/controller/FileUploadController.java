package com.beautifulsoup.chengfeng.controller;

import static com.beautifulsoup.chengfeng.utils.FastDfsClientUtil.saveFile;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.beautifulsoup.chengfeng.common.ResponseResult;
import com.beautifulsoup.chengfeng.constant.ChengfengConstant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "文件上传", tags = {"文件上传Controller"}, description = "文件上传", protocols = "http")
@RestController
@RequestMapping("/file")
public class FileUploadController
{
    
    @GetMapping("/")
    public String getUploadHtml()
    {
        return "upload";
    }
    
    @ApiOperation(value = "文件上传", notes = "上传文件", produces = "multipart/form-data", response = ResponseResult.class, httpMethod = "POST")
    @PostMapping("/upload")
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile file)
    {
        if (file.isEmpty())
        {
            throw new MultipartException(ChengfengConstant.File.UPLOAD_EMPTY_ERROR);
        }
        try
        {
            String path = saveFile(file);
            return ResponseResult.createBySuccess("图片上传成功", path);
        }
        catch (IOException e)
        {
            throw new MultipartException(ChengfengConstant.File.UPLOAD_FAILURE);
        }
    }
    
    @ApiOperation(value = "文件上传", notes = "批量上传文件", produces = "multipart/form-data", response = ResponseResult.class, httpMethod = "POST")
    @PostMapping("/uploads")
    public ResponseResult uploadFiles(@RequestParam("files") MultipartFile[] files)
    {
        if (files == null || files.length < 1)
        {
            throw new MultipartException(ChengfengConstant.File.UPLOAD_EMPTY_ERROR);
        }
        try
        {
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < files.length; i++)
            {
                if (i != files.length - 1)
                {
                    stringBuffer.append(saveFile(files[i])).append(",");
                }
                else
                {
                    stringBuffer.append(saveFile(files[i]));
                }
            }
            return ResponseResult.createBySuccess("图片上传成功", stringBuffer.toString());
        }
        catch (IOException e)
        {
            throw new MultipartException(ChengfengConstant.File.UPLOAD_FAILURE);
        }
    }
    
}
