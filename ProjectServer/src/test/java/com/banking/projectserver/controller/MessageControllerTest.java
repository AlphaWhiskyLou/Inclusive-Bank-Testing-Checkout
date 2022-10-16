package com.banking.projectserver.controller;

import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.HMacJWTSigner;
import cn.hutool.jwt.signers.JWTSigner;
import com.banking.projectserver.activeMQ.Consumer;
import com.banking.projectserver.activeMQ.Producer;
import com.banking.projectserver.response.Response;
import com.banking.projectserver.serviceImpl.AssistanceServiceImpl;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.databene.contiperf.timer.RandomTimer;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
class MessageControllerTest {
    MockHttpServletRequest mockedRequest;
    Producer producerStub;

    @BeforeEach
    void setUp() {
        System.out.println("-- Initializing variables for UT_TC_004 Test --");

        //Class Initialization
        MessageController messageController = new MessageController();
        producerStub = mock(Producer.class);
        mockedRequest = new MockHttpServletRequest();
        String userID;

        //Token Signature Initialization
        byte[] TOKEN_SECRET = "UT_TC_003_002".getBytes(StandardCharsets.UTF_8);
        JWTSigner jwtSigner = new HMacJWTSigner("HmacSHA256", TOKEN_SECRET);

        //Token Header Initialization
        Map<String, Object> mockedHeader = new HashMap<>(2);
        mockedHeader.put("typ", "JWT");
        mockedHeader.put("alg", "HS256");

        //Token Payload Initialization
        Map<String, Object> mockedPayload = new HashMap<>(1);
        //这里模拟登陆到系统的用户userid
        userID = "alreadyLoginUserID";
        //此处的userID被加密到token中了
        mockedPayload.put("uid", userID);

        String TEST_TOKEN = JWTUtil.createToken(mockedHeader, mockedPayload, jwtSigner);
        mockedRequest.addHeader("token", TEST_TOKEN);

        System.out.println("-- FINISHED Initializing --");
    }

    @AfterEach
    void tearDown() {
    }

    @Rule
    public ContiPerfRule rule = new ContiPerfRule();


    //@Test
    @PerfTest(invocations = 10, threads = 10, timer = RandomTimer.class, timerParams = {30, 80})
    @Test
    void UT_TC_004_001_001_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String msg = "";
        String queueName = "testQueue";
        MessageController messageController = new MessageController();
        doNothing().when(producerStub).sendMessage(msg, queueName);

        messageController = new MessageController();
        messageController.setProducer(producerStub);
        assertEquals(messageController.send(msg, queueName), Response.invalidService());

        System.out.println("UT_TC_004_001_001_001 "+" -- PASSED");
    }

    @Test
    @PerfTest(invocations = 100,threads = 10)
    void UT_TC_004_001_001_002() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String msg = "testQueue";
        String queueName = "";
        MessageController messageController = new MessageController();
        doNothing().when(producerStub).sendMessage(msg, queueName);

        messageController = new MessageController();
        messageController.setProducer(producerStub);
        assertEquals(messageController.send(msg, queueName), Response.invalidService());

        System.out.println("UT_TC_004_001_001_002 "+" -- PASSED");
    }

    @Test
    void UT_TC_004_001_001_003() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String msg = "";
        String queueName = "";
        MessageController messageController = new MessageController();
        doNothing().when(producerStub).sendMessage(msg, queueName);

        messageController = new MessageController();
        messageController.setProducer(producerStub);
        assertEquals(messageController.send(msg, queueName), Response.invalidService());

        System.out.println("UT_TC_004_001_001_003 "+" -- PASSED");
    }



    @Test
    void UT_TC_004_001_002_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String msg = "testQueue";
        String queueName = "testQueue";
        MessageController messageController = new MessageController();
        doNothing().when(producerStub).sendMessage(msg, queueName);

        messageController = new MessageController();
        messageController.setProducer(producerStub);
        assertEquals(messageController.send(msg, queueName), Response.OK());

        System.out.println("UT_TC_004_001_002_001 "+" -- PASSED");
    }

    @Test
    void UT_TC_004_002_001_001() throws JMSException, InterruptedException {
        MessageController messageController = new MessageController();
        Consumer consumerStub = mock(Consumer.class);
        when(consumerStub.getMessage("alreadyLoginUserID")).thenReturn("");

        messageController = new MessageController();
        messageController.setProducer(producerStub);
        messageController.setConsumer(consumerStub);
        assertEquals(messageController.getMessage(mockedRequest).getMessage(), "No message received");

        System.out.println("UT_TC_004_002_001_001 "+" -- PASSED");

    }

    @Test
    void UT_TC_004_002_002_001() throws JMSException, InterruptedException {
        MessageController messageController = new MessageController();
        Consumer consumerStub = mock(Consumer.class);
        when(consumerStub.getMessage("alreadyLoginUserID")).thenReturn("alreadyLoginUserID");

        messageController = new MessageController();
        messageController.setProducer(producerStub);
        messageController.setConsumer(consumerStub);
        assertEquals(messageController.getMessage(mockedRequest).getMessage(), "成功");

        System.out.println("UT_TC_004_002_002_001 "+" -- PASSED");
    }

    @Test
    void UT_TC_004_002_003_001() throws JMSException, InterruptedException {
        MessageController messageController = new MessageController();
        Consumer consumerStub = mock(Consumer.class);
        when(consumerStub.getMessage("alreadyLoginUserID")).thenReturn("");

        messageController = new MessageController();
        messageController.setProducer(producerStub);
        messageController.setConsumer(consumerStub);
        assertEquals(messageController.getMessage(mockedRequest).getMessage(), "No message received");

        System.out.println("UT_TC_004_002_003_001 "+" -- PASSED");
    }

    @Test
    void IT_TD_001_001_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String msg = "";
        String queueName = "testQueue";
        MessageController messageController = new MessageController();
        Producer producer = new Producer();
        messageController.setProducer(producer);
        assertEquals(messageController.send(msg, queueName), Response.invalidService());

        System.out.println("IT_TD_001_001_001 "+" -- PASSED");
    }

    @Test
    void IT_TD_001_001_002() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String msg = "testQueue";
        String queueName = "";
        MessageController messageController = new MessageController();
        Producer producer = new Producer();
        messageController.setProducer(producer);
        assertEquals(messageController.send(msg, queueName), Response.invalidService());

        System.out.println("IT_TD_001_001_002 "+" -- PASSED");
    }

    @Test
    void IT_TD_001_001_003() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String msg = "";
        String queueName = "";
        MessageController messageController = new MessageController();
        Producer producer = new Producer();
        messageController.setProducer(producer);
        assertEquals(messageController.send(msg, queueName), Response.invalidService());

        System.out.println("IT_TD_001_001_003 "+" -- PASSED");
    }

    @Test
    void IT_TD_001_002_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String msg = "testQueue";
        String queueName = "testQueue";
        MessageController messageController = new MessageController();
        Producer producer = new Producer();
        messageController.setProducer(producer);
        assertEquals(messageController.send(msg, queueName), Response.OK());

        System.out.println("IT_TD_001_002_001 "+" -- PASSED");
    }

    @Test
    void IT_TD_002_001_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        MessageController messageController = new MessageController();
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        messageController.setConsumer(consumer);
        assertEquals(messageController.getMessage(mockedRequest).getMessage(), "No message received");

        System.out.println("IT_TD_002_001_001 "+" -- PASSED");
    }

    @Test
    void IT_TD_002_002_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        MessageController messageController = new MessageController();
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        messageController.setProducer(producer);
        messageController.setConsumer(consumer);
        messageController.send("alreadyLoginUserID", "alreadyLoginUserID");
        assertEquals(messageController.getMessage(mockedRequest).getMessage(), "成功");

        System.out.println("IT_TD_002_002_001 "+" -- PASSED");
    }

    @Test
    void IT_TD_002_003_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        MessageController messageController = new MessageController();
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        messageController.setProducer(producer);
        messageController.setConsumer(consumer);
        messageController.send("alreadyLoginUserID", "whateverID");
        assertEquals(messageController.getMessage(mockedRequest).getMessage(), "No message received");

        System.out.println("IT_TD_002_003_001 "+" -- PASSED");
    }


}