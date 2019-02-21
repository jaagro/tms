package com.jaagro.tms.biz.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tony
 */
@Configuration
public class RabbitMqConfig {

    /**
     * APP插入实时位置队列
     */
    public static final String LOCATION_SEND_QUEUE = "location.send.queue";

    /**
     * 牧原图片识别自动创建运单队列
     */
    public static final String MUYUAN_OCR_QUEUE = "muyuan.ocr.queue";

    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    @Bean
    public Queue locationSendQueue() {
        return new Queue(LOCATION_SEND_QUEUE, true);
    }

    @Bean
    public Queue muYuanOcrQueue() {
        return new Queue(MUYUAN_OCR_QUEUE, true);
    }

    /**
     * Topic模式
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * Fanout模式
     * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding locationSendBindingTopic() {
        return BindingBuilder.bind(locationSendQueue()).to(topicExchange()).with("location.send");
    }

    @Bean
    public Binding muyuanOcrBindingTopic() {
        return BindingBuilder.bind(muYuanOcrQueue()).to(topicExchange()).with("muyuan.ocr");
    }
}
