package com.beautifulsoup.chengfeng.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beautifulsoup.chengfeng.controller.vo.PurchaseProductVo;
import com.beautifulsoup.chengfeng.repository.ProductRepository;
import com.beautifulsoup.chengfeng.service.AdvertiseService;
import com.google.common.collect.Lists;

@Service
public class AdvertiseServiceImpl implements AdvertiseService {

//    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<PurchaseProductVo> getAllProductsByInfo(String info) {
        List<PurchaseProductVo> productVos= Lists.newArrayList();
        /*List<PurchaseProduct> products=productRepository.findBySubtitle(info).stream().sorted(Comparator.comparing(PurchaseProduct::getSales).reversed())
                .collect(Collectors.toList());
        products.stream().forEach(product->{
            PurchaseProductVo vo=new PurchaseProductVo();
            BeanUtils.copyProperties(product,vo);
            productVos.add(vo);
        });*/
        return productVos;
    }
}
