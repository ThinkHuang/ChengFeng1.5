package com.beautifulsoup.chengfeng.initialize;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.beautifulsoup.chengfeng.constant.ChengfengConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化加载ElasticSearch数据
 */
@Slf4j
@Component
@Order(value = 2)
public class StartupLoadElasticSearch implements CommandLineRunner {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) throws Exception {

        this.rabbitTemplate.convertAndSend(ChengfengConstant.RabbitMQ.TOPIC_EXCHANGE,
                "topic.elasticsearch",
                ChengfengConstant.RabbitMQ.MESSAGE_ELASTICSEARCH_INIT);
        log.info(ChengfengConstant.RabbitMQ.MESSAGE_ELASTICSEARCH_INIT+"消息发送");
    }
}
