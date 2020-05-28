package com.xkcoding.mq.rabbitmq.controller;

import com.xkcoding.mq.rabbitmq.constants.RabbitConsts;
import com.xkcoding.mq.rabbitmq.message.MessageStruct;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jt
 * @date 2020-3-29
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendFanout/{message}")
    public void sendFanout(@PathVariable("message")String message){

        MessageStruct messageStruct = new MessageStruct();
        messageStruct.setMessage("testest1");
        rabbitTemplate.convertAndSend("fanout.mode","",messageStruct);



    }



    @GetMapping("/sendDirect/{message}")
    public void sendDirect(@PathVariable("message")String message){



        MessageStruct messageStruct = new MessageStruct();
        messageStruct.setMessage("testest1");

        rabbitTemplate.convertAndSend("directExchange","",messageStruct);



    }



    @GetMapping("/sendTopic/{message}")
    public void sendTopic(@PathVariable("message")String message){

        MessageStruct messageStruct = new MessageStruct();
        messageStruct.setMessage("topic");
        rabbitTemplate.convertAndSend("TopicExchange","3.queue",messageStruct);



    }



    @GetMapping("/sendDelay/{message}")
    public void sendDelay(@PathVariable("message")String message, @RequestParam(name="time")String time) throws ParseException {

        MessageStruct messageStruct = new MessageStruct();
        messageStruct.setMessage("topic");
//        rabbitTemplate.convertAndSend("delayExchange","delay.queue",messageStruct);

//        String publishTime = "2020-03-29 21:03:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = sdf.parse(time);
        Date date = new Date();


        long l = parse.getTime() - date.getTime();
        System.out.println("消息发送时间:"+time);
        rabbitTemplate.convertAndSend("delayExchange", RabbitConsts.DELAY_QUEUE, messageStruct, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("x-delay",l);
                return message;
            }
        });


    }

}
