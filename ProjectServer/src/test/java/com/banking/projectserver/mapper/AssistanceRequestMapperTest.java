package com.banking.projectserver.mapper;

import com.banking.projectserver.serviceImpl.AssistanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AssistanceRequestMapperTest {

    @Autowired
    private AssistanceRequestMapper assistanceRequestMapper;

    @Test
    void addAssistanceRequest() {
        String assistanceID = UUID.randomUUID().toString();
        Date assistanceDate = new Date(System.currentTimeMillis());
    }

    @Test
    void updateAssistanceRequestStatus() {
    }

    @Test
    void getAssistanceRequestByUserID() {
        //success
        assertNotNull(assistanceRequestMapper.getAssistanceRequestByUserID("420114200010030030"));
        //fail
//        assertNull(assistanceRequestMapper.getAssistanceRequestByUserID("420114200010030030"));

    }

    @Test
    void getUnfinishedAssistanceRequestByUserID() {
    }

    @Test
    void getUnityIDByUserID() {
    }
}