package com.banking.projectserver.activeMQ;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;
import java.beans.PropertyDescriptor;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class ProducerTest {
    @BeforeEach
    void setUp() {
        System.out.println("-- BEGINNING NEXT UNIT TEST --");
    }

    @AfterEach
    void tearDown() {
        System.out.println("-- FINISHED CURRENT TEST UNIT FOR ActiveMQ Producer --");
    }

    @Test
    void UT_TC_001_001_001_001() throws JMSException, InterruptedException {
        String message = "testQueue";
        String queueName = "testQueue";
        Producer producer = new Producer();
        assertThrows(JMSException.class, () -> producer.sendMessage(message, queueName));
        System.out.println("UT_TC_001_001_001_001" +" -- PASSED");

    }

    @Test
    void UT_TC_001_001_002_001() throws JMSException, InterruptedException {
        String message = "";
        String queueName = "testQueue";
        Producer producer = new Producer();
        assertDoesNotThrow(() -> producer.sendMessage(message, queueName));
        System.out.println("UT_TC_001_001_002_001" +" -- PASSED");

    }

    @Test
    void UT_TC_001_001_002_002() throws JMSException, InterruptedException {
        String message = "testQueue";
        String queueName = "";
        Producer producer = new Producer();
        assertThrows(IllegalArgumentException.class, () -> producer.sendMessage(message, queueName));
        System.out.println("UT_TC_001_002_002_002" +" -- PASSED");

    }

    @Test
    void UT_TC_001_001_002_003() throws JMSException, InterruptedException {
        String message = "";
        String queueName = "";
        Producer producer = new Producer();
        assertThrows(IllegalArgumentException.class, () -> producer.sendMessage(message, queueName));
        System.out.println("UT_TC_001_001_002_003" +" -- PASSED");

    }

    @Test
    void UT_TC_001_001_003_001() throws JMSException, InterruptedException, IllegalAccessException, InstantiationException {
        String message = "testQueue";
        String queueName = "testQueue";
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.sendMessage(message, queueName);
        assertEquals(message, consumer.getMessage(queueName));
        System.out.println("UT_TC_001_001_003_001" +" -- PASSED");

    }
}