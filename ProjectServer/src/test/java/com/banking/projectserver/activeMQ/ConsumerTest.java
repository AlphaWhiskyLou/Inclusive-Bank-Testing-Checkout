package com.banking.projectserver.activeMQ;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class ConsumerTest {

    @BeforeEach
    void setUp() {
        System.out.println("-- BEGINNING NEXT UNIT TEST --");
    }

    @AfterEach
    void tearDown() {
        System.out.println("-- FINISHED CURRENT TEST UNIT FOR ActiveMQ Consumer --");
    }

    @Test
    void UT_TC_001_002_001_001() throws JMSException, InterruptedException {
        String queueName = "testQueue";
        Consumer consumer = new Consumer();
        assertThrows(RuntimeException.class, () -> consumer.getMessage(queueName));
        System.out.println("UT_TC_001_002_001_001" +" -- PASSED");
    }

    @Test
    void UT_TC_001_002_002_001() {
        String queueName = "";
        Consumer consumer = new Consumer();
        assertThrows(IllegalArgumentException.class, () -> consumer.getMessage(queueName));
        System.out.println("UT_TC_001_002_002_001" +" -- PASSED");
    }

    @Test
    void UT_TC_001_002_003_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String message = "Queue1";
        String queueName = "testQueue1";
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.sendMessage(message, queueName);
        assertEquals("nothing", consumer.getMessage(queueName));
        System.out.println("UT_TC_001_002_003_001" +" -- PASSED");
    }

    @Test
    void UT_TC_001_002_004_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String message = "testQueue";
        String queueName = "testQueue";
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.sendMessage(message, queueName);
        assertEquals("testQueue", consumer.getMessage(queueName));
        System.out.println("UT_TC_001_002_004_001" +" -- PASSED");
    }
}