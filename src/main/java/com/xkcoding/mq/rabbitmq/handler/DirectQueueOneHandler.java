package com.xkcoding.mq.rabbitmq.handler;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.xkcoding.mq.rabbitmq.constants.RabbitConsts;
import com.xkcoding.mq.rabbitmq.message.MessageStruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * 直接队列1 处理器
 * </p>
 *
 * @package: com.xkcoding.mq.rabbitmq.handler
 * @description: 直接队列1 处理器
 * @author: yangkai.shen
 * @date: Created in 2019-01-04 15:42
 * @copyright: Copyright (c) 2019
 * @version: V1.0
 * @modified: yangkai.shen
 */
@Slf4j
@RabbitListener(queues = "direct.queue.1")
@Component
public class DirectQueueOneHandler {

    /**
     * 如果 spring.rabbitmq.listener.direct.acknowledge-mode: auto，则可以用这个方式，会自动ack
     */
     @RabbitHandler
    public void directHandlerAutoAck(MessageStruct messageStruct, Message message, Channel channel) {
         try {
             //<editor-fold desc="Description">
             int i = 1 / 0;//抛出异常进入死信对列
             log.info("直接队列处理器1，接收消息：{}", JSONUtil.toJsonStr(messageStruct));
             //</editor-fold>
         } catch (Exception e) {
             e.printStackTrace();

             try {

                 /**
                  * deliveryTag:该消息的index
                  * multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
                  * requeue：被拒绝的是否重新入队列
                  *
                  *
                  * 不写这句消息不会快速发送到死信队列===
                  */
                 channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
             } catch (IOException e1) {
                 e1.printStackTrace();
             }
         }
     }

//    @RabbitHandler
    public void directHandlerManualAck(MessageStruct messageStruct, Message message, Channel channel) {
        //  如果手动ACK,消息会被监听消费,但是消息在队列中依旧存在,如果 未配置 acknowledge-mode 默认是会在消费完毕后自动ACK掉
//        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        try {
//            log.info("直接队列1，手动ACK，接收消息：{}", JSONUtil.toJsonStr(messageStruct));
//            // 通知 MQ 消息已被成功消费,可以ACK了
//            channel.basicAck(deliveryTag, false);
//        } catch (IOException e) {
//            try {
//                // 处理失败,重新压入MQ
//                channel.basicRecover();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
    }
}
