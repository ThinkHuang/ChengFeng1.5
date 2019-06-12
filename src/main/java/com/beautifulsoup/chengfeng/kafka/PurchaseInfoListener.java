package com.beautifulsoup.chengfeng.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.beautifulsoup.chengfeng.repository.PurchaseInfoRepository;
import com.beautifulsoup.chengfeng.service.dto.PurchaseInfoDto;

public class PurchaseInfoListener {

    @Autowired
    private PurchaseInfoRepository purchaseInfoRepository;

    @KafkaListener(id = "topic-demo",topics = {"topic-demo"})
    public void demoTopicListen(ConsumerRecord<String, PurchaseInfoDto> record){
        purchaseInfoRepository.save(record.value());
    }
}
