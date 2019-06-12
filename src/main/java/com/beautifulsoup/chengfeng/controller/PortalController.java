package com.beautifulsoup.chengfeng.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beautifulsoup.chengfeng.common.ResponseResult;
import com.beautifulsoup.chengfeng.controller.vo.CommunityNoticeVo;
import com.beautifulsoup.chengfeng.controller.vo.PortalVo;
import com.beautifulsoup.chengfeng.controller.vo.ProductSimpleVo;
import com.beautifulsoup.chengfeng.controller.vo.ProperNoticeVo;
import com.beautifulsoup.chengfeng.controller.vo.RepairBookVo;
import com.beautifulsoup.chengfeng.controller.vo.SecretaryBookVo;
import com.beautifulsoup.chengfeng.controller.vo.WaterBookVo;
import com.beautifulsoup.chengfeng.controller.vo.WaterBrandVo;
import com.beautifulsoup.chengfeng.pojo.BannerImage;
import com.beautifulsoup.chengfeng.service.PortalService;
import com.beautifulsoup.chengfeng.service.dto.RepairBookDto;
import com.beautifulsoup.chengfeng.service.dto.SecretaryBookDto;
import com.beautifulsoup.chengfeng.service.dto.WatersuplyDto;

import io.swagger.annotations.Api;

@Api(value = "系统首页", tags = {"系统首页Controller"}, description = "系统首页", protocols = "http")
@RestController
@RequestMapping("/portal")
public class PortalController
{
    
    @Autowired
    private PortalService portalService;
    
    @GetMapping(value = "/notice/community/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CommunityNoticeVo>> getAllCommunityNotices(@RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize)
    {
        List<CommunityNoticeVo> communityNoticeVos = portalService.findAllCommunityNoticeVos(pageNum, pageSize);
        return ResponseResult.createBySuccess(communityNoticeVos);
    }
    
    @GetMapping(value = "/notice/proper/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<ProperNoticeVo>> getAllProperNotices(@RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize)
    {
        List<ProperNoticeVo> properNoticeVos = portalService.findAllProperNoticeVos(pageNum, pageSize);
        return ResponseResult.createBySuccess(properNoticeVos);
    }
    
    @GetMapping(value = "/notice/community/latest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<CommunityNoticeVo>> getLatestCommunityNotices(@RequestParam(value = "limit", required = false, defaultValue = "3") Integer limit)
    {
        List<CommunityNoticeVo> latestCommunityNoticeVos = portalService.findLatestCommunityNoticeVos(limit);
        return ResponseResult.createBySuccess(latestCommunityNoticeVos);
    }
    
    @GetMapping(value = "/notice/proper/latest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<ProperNoticeVo>> getLatestProperNotices(@RequestParam(value = "limit", required = false, defaultValue = "3") Integer limit)
    {
        List<ProperNoticeVo> latestProperNoticeVos = portalService.findLatestProperNoticeVos(limit);
        return ResponseResult.createBySuccess(latestProperNoticeVos);
    }
    
    @PostMapping(value = "/book/repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<String> submitRepairInfo(@Valid @RequestBody RepairBookDto bookDto, BindingResult bindingResult)
    {
        String result = portalService.submitRepairInfo(bookDto, bindingResult);
        if (!StringUtils.isBlank(result))
        {
            return ResponseResult.createBySuccess(result);
        }
        return ResponseResult.createByError();
    }
    
    @GetMapping(value = "/get/repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<RepairBookVo>> getRepairBookInfo(@RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize)
    {
        List<RepairBookVo> repairBookVos = portalService.findAllRepairBookInfo(pageNum, pageSize);
        return ResponseResult.createBySuccess("报修信息获取成功", repairBookVos);
    }
    
    @GetMapping(value = "/get/water", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<WaterBookVo>> getWaterBookInfo(@RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize)
    {
        List<WaterBookVo> waterBookVos = portalService.findAllWaterBookInfo(pageNum, pageSize);
        return ResponseResult.createBySuccess("送水信息获取成功", waterBookVos);
    }
    
    @GetMapping(value = "/get/secretary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<SecretaryBookVo>> getSecretaryBookInfo(@RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize)
    {
        List<SecretaryBookVo> secretaryBookVos = portalService.findAllSecretaryBookInfo(pageNum, pageSize);
        return ResponseResult.createBySuccess("找书记信息获取成功", secretaryBookVos);
    }
    
    // 找书记
    @PostMapping(value = "/book/secretary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<String> searchSecretary(@Valid @RequestBody SecretaryBookDto bookDto, BindingResult bindingResult)
    {
        String result = portalService.searchSecretary(bookDto, bindingResult);
        if (!StringUtils.isBlank(result))
        {
            return ResponseResult.createBySuccess(result);
        }
        return ResponseResult.createByError();
    }
    
    @GetMapping(value = "/water/brands", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<WaterBrandVo>> getWaterBrandVos()
    {
        List<WaterBrandVo> waterBrandVos = portalService.findAllWaterBrands();
        return ResponseResult.createBySuccess(waterBrandVos);
    }
    
    @PostMapping(value = "/book/water", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<WaterBookVo> bookWatersuplyInfo(@Valid @RequestBody WatersuplyDto watersuplyDto, BindingResult result)
    {
        WaterBookVo waterBookVo = portalService.bookWaterSuply(watersuplyDto, result);
        return ResponseResult.createBySuccess(waterBookVo);
    }
    
    @GetMapping(value = "/carousal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<BannerImage>> getCarousalImage()
    {
        List<BannerImage> bannerImages = portalService.findCarousalImages();
        return ResponseResult.createBySuccess("首页轮播图获取成功", bannerImages);
    }
    
    @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PortalVo> getAllInformation()
    {
        return ResponseResult.createBySuccess("首页信息获取成功", portalService.getAllInformation());
    }
    
    @GetMapping(value = "/recommend", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<ProductSimpleVo>> getRecommendProducts(@RequestParam(value = "limit", defaultValue = "9", required = false) Integer limit)
    {
        List<ProductSimpleVo> productSimpleVos = portalService.getRecommendProducts(limit);
        return ResponseResult.createBySuccess(productSimpleVos);
    }
}
