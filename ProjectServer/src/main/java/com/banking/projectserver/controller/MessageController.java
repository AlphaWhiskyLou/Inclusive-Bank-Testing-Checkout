package com.banking.projectserver.controller;

import cn.hutool.jwt.JWTUtil;
import com.banking.projectserver.activeMQ.Consumer;
import com.banking.projectserver.activeMQ.Producer;
import com.banking.projectserver.aop.log.MessageLog;
import com.banking.projectserver.entity.MessageRecv;
import com.banking.projectserver.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @ program: Bank
 * @ description:
 * @ author: Tianyu Lou
 * @ date: 2021-12-18 14:35:17
 */
@RestController
@RequestMapping("MessageCenter")
public class MessageController {

    @Autowired
    //创建一个生产者，消费者在系统运行时已经创建
    Producer producer;

    @Autowired
    Consumer consumer;

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    @MessageLog
    @GetMapping( "SendMessageByQueue/{msg}/{queueName}")
    public Response send(@PathVariable String msg, @PathVariable String queueName) throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        if(msg.equals("") || queueName.equals("")){
            return Response.invalidService();
        }
        else {
            producer.sendMessage(msg, queueName);
            return Response.OK();
        }
    }

    @GetMapping("ReceiveMessageByQueue")
    public Response getMessage(HttpServletRequest request) throws JMSException, InterruptedException {
        String token = request.getHeader("token");
        String queueName = (String) JWTUtil.parseToken(token).getPayload("uid");
        MessageRecv msgRecv = null;
        String msg = consumer.getMessage(queueName);
        if(queueName.contentEquals(msg)){
            msgRecv = new MessageRecv();
            msgRecv.setMessage(msg);
        }
        if (msgRecv == null) {
            return Response.invalidService().message("No message received");
        }
        else{
            return Response.OK().data("msgRecv",msgRecv);
        }
    }


}