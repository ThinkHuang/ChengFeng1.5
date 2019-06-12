package com.beautifulsoup.chengfeng.config;


import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.EVALUATION_EXCHANGE;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.ORDER_EXCHANGE;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.QUEUE_NAME_ELASTICSEARCH;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.QUEUE_NAME_EVALUATION;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.QUEUE_NAME_MONGODB;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.QUEUE_NAME_ORDER;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.QUEUE_NAME_SPELL_ORDER;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.QUEUE_NAME_STOCK;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.QUEUE_NAME_UPDATE_ORDER;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.SPELL_ORDER_DELAY_EXCHANGE;
import static com.beautifulsoup.chengfeng.constant.ChengfengConstant.RabbitMQ.TOPIC_EXCHANGE;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.beautifulsoup.chengfeng.constant.ChengfengConstant;
import com.beautifulsoup.chengfeng.prop.RabbitmqProperties;

@EnableConfigurationProperties(value = {RabbitmqProperties.class})
public class RabbitmqConfig {

    @Autowired
    private RabbitmqProperties rabbitmqProperties;

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory=new CachingConnectionFactory(rabbitmqProperties.getAddresses(),rabbitmqProperties.getPort());
        connectionFactory.setUsername(rabbitmqProperties.getUsername());
        connectionFactory.setPassword(rabbitmqProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitmqProperties.getVirtualHost());
        connectionFactory.setPublisherConfirms(true);

        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause));
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }

   @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }


    @Bean
    public Queue chengfengQueue(){
        return new Queue(ChengfengConstant.RabbitMQ.QUEUE_NAME_CHENGFENG,true);
    }

    @Bean
    public Queue orderQueue(){
        return new Queue(QUEUE_NAME_ORDER,true);
    }

    @Bean
    public Queue mongodbQueue(){
        return new Queue(QUEUE_NAME_MONGODB,true);
    }

    @Bean
    public Queue elasticSearchQueue(){
        return new Queue(QUEUE_NAME_ELASTICSEARCH,true);
    }

    @Bean
    public Queue stockQueue(){
        return new Queue(QUEUE_NAME_STOCK,true);
    }

    @Bean
    public Queue updateOrderQueue(){
        return new Queue(QUEUE_NAME_UPDATE_ORDER,true);
    }

    @Bean
    public Queue productEvaluationQueue(){
        return new Queue(QUEUE_NAME_EVALUATION,true);
    }

    @Bean
    public TopicExchange chengfengExchange(){
        return new TopicExchange(TOPIC_EXCHANGE,true,false);
    }

    @Bean
    public FanoutExchange orderExchange(){return new FanoutExchange(ORDER_EXCHANGE,true,false);}

    @Bean
    public FanoutExchange evaluationExchange(){return new FanoutExchange(EVALUATION_EXCHANGE,true,false);}

    @Bean
    public CustomExchange spellOrderDelayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(SPELL_ORDER_DELAY_EXCHANGE, "x-delayed-message",true, false,args);
    }

    @Bean
    public Queue spellOrderQueue() {
        Queue queue = new Queue(QUEUE_NAME_SPELL_ORDER, true);
        return queue;
    }

    @Bean
    Binding bindingExchangeChengfeng(){
        return BindingBuilder.bind(chengfengQueue()).to(chengfengExchange()).with("topic.#");
    }

    @Bean
    Binding bindingExchangeOrder(){
        return BindingBuilder.bind(orderQueue()).to(orderExchange());
    }

    @Bean
    Binding bindingExchangeMongodb(){
        return BindingBuilder.bind(mongodbQueue()).to(chengfengExchange()).with("topic.mongodb");
    }

    @Bean
    Binding bindingExchangElasticSearch(){
        return BindingBuilder.bind(elasticSearchQueue()).to(chengfengExchange()).with("topic.elasticsearch");
    }

    @Bean
    Binding bindingExchangStock(){
        return BindingBuilder.bind(stockQueue()).to(chengfengExchange()).with("topic.stock");
    }


    @Bean
    Binding bindingSpellOrderDelay() {
        return BindingBuilder.bind(spellOrderQueue()).to(spellOrderDelayExchange()).with("spell_order_delay_queue").noargs();
    }

    @Bean
    Binding bindingUpdateOrderDelay() {
       return BindingBuilder.bind(updateOrderQueue()).to(spellOrderDelayExchange()).with("update_order_queue").noargs();
    }

    @Bean
    Binding bindingPurchaseEvaluation() {
        return BindingBuilder.bind(productEvaluationQueue()).to(evaluationExchange());
    }
}
