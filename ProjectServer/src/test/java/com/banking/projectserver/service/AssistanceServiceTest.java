package com.banking.projectserver.service;

import com.banking.projectserver.controller.AssistanceController;
import com.banking.projectserver.entity.AssistanceRequest;
import com.banking.projectserver.entity.AssistanceRequestDisp;
import com.banking.projectserver.mapper.AssistanceRequestMapper;
import com.banking.projectserver.serviceImpl.AssistanceServiceImpl;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


class AssistanceServiceTest {


    @DisplayName("IT_TD_015")
    @Nested
    @RunWith(SpringRunner.class)
    @SpringBootTest()
    class IT_TD_015 {
        @Autowired
        private AssistanceServiceImpl assistanceService;

        @DisplayName("IT_TD_015_001_001@UpdateAssistanceRequest")
        @Test
        void IT_TD_015_001_001() throws Exception {
            System.out.println("IT_TD_015_001_001: TRANSFER MONEY SUCCESSFULLY");

            //
            assertTrue(assistanceService.updateAssistanceRequestStatus("52aa6af2-fff5-48a4-9082-b6ee89029f8e", 1));


            System.out.println("IT_TD_015_001_001: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_015_001_002@UpdateAssistanceRequest")
        @Test
        void IT_TD_015_001_002() throws Exception {
            System.out.println("IT_TD_015_001_002: TRANSFER MONEY SUCCESSFULLY");

            //
            assertTrue(assistanceService.updateAssistanceRequestStatus("dd0f5a2d-7310-4ebf-93e5-70da099f669e", 1));


            System.out.println("IT_TD_015_001_002: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        //742de88f-8a80-4a8f-a411-48497e5eaba8
        @DisplayName("IT_TD_015_001_003@UpdateAssistanceRequest")
        @Test
        void IT_TD_015_001_003() throws Exception {
            System.out.println("IT_TD_015_001_003: TRANSFER MONEY SUCCESSFULLY");

            //
            assertTrue(assistanceService.updateAssistanceRequestStatus("742de88f-8a80-4a8f-a411-48497e5eaba8", 1));


            System.out.println("IT_TD_015_001_003: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_015_002_001@UpdateAssistanceRequest")
        @Test
        void IT_TD_015_002_001() throws Exception {
            System.out.println("IT_TD_015_002_001: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("", 1);
                    });

            System.out.println("IT_TD_015_002_001: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_015_003_001@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_015_003_001() throws Exception {
            System.out.println("IT_TD_015_003_001: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("", 1);
                    });

            System.out.println("IT_TD_015_003_001: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_015_003_002@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_015_003_002() throws Exception {
            System.out.println("IT_TD_015_003_002: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("IT_TD_015_003_002", 1);
                    });

            System.out.println("IT_TD_015_003_002: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_015_003_003@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_015_003_003() throws Exception {
            System.out.println("IT_TD_015_003_003: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(Exception.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("IT_TD_015_003_003", 1);
                    });

            System.out.println("IT_TD_015_003_003: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_015_003_004@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_015_003_004() throws Exception {
            System.out.println("IT_TD_015_003_004: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("", 1);
                    });

            System.out.println("IT_TD_015_003_004: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_015_004_001@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_015_004_001() throws Exception {
            System.out.println("IT_TD_015_004_001: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("IT_TD_015_004_00", 1);
                    });

            System.out.println("IT_TD_015_004_001: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }


    }

    @DisplayName("IT_TD_016")
    @Nested
    @RunWith(SpringRunner.class)
    @SpringBootTest()
    class IT_TD_016 {
        @Autowired
        private AssistanceServiceImpl assistanceService;


        @DisplayName("IT_TD_016_001_001@UpdateAssistanceRequest")
        @Test
        @Rollback
        void IT_TD_016_001_001() throws Exception {
            System.out.println("IT_TD_016_001_001: TRANSFER MONEY SUCCESSFULLY");

            //
            assertTrue(assistanceService.updateAssistanceRequestStatus("IT_TD_016_001_001", 1));


            System.out.println("IT_TD_016_001_001: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_016_002_001@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_016_002_001() throws Exception {
            System.out.println("IT_TD_016_002_001: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("", 1);
                    });

            System.out.println("IT_TD_016_002_001: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_016_002_002@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_016_002_002() throws Exception {
            System.out.println("IT_TD_016_002_002: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("", 1);
                    });

            System.out.println("IT_TD_016_002_002: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }


    }

    @DisplayName("IT_TD_017")
    @Nested
    @RunWith(SpringRunner.class)
    @SpringBootTest()
    class IT_TD_017 {
        @Autowired
        private AssistanceServiceImpl assistanceService;


        @DisplayName("IT_TD_017_001_001@UpdateAssistanceRequest")
        @Test
        @Rollback
        void IT_TD_017_001_001() throws Exception {
            System.out.println("IT_TD_017_001_001: TRANSFER MONEY SUCCESSFULLY");

            //
            assertTrue(assistanceService.updateAssistanceRequestStatus("IT_TD_017_001_001", 1));


            System.out.println("IT_TD_017_001_001: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_017_001_002@UpdateAssistanceRequest")
        @Test
        @Rollback
        void IT_TD_017_001_002() throws Exception {
            System.out.println("IT_TD_017_001_002: TRANSFER MONEY SUCCESSFULLY");

            //
            assertTrue(assistanceService.updateAssistanceRequestStatus("IT_TD_017_001_002", 1));


            System.out.println("IT_TD_017_001_002: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }


        @DisplayName("IT_TD_017_001_003@UpdateAssistanceRequest")
        @Test
        @Rollback
        void IT_TD_017_001_003() throws Exception {
            System.out.println("IT_TD_017_001_003: TRANSFER MONEY SUCCESSFULLY");

            //
            assertTrue(assistanceService.updateAssistanceRequestStatus("IT_TD_017_001_003", 1));


            System.out.println("IT_TD_017_001_003: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }




        @DisplayName("IT_TD_017_002_001@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_017_002_001() throws Exception {
            System.out.println("IT_TD_016_002_001: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("IT_TD_017_002_002", 1);
                    });

            System.out.println("IT_TD_017_002_001: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_017_002_002@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_017_002_002() throws Exception {
            System.out.println("IT_TD_017_002_002: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("IT_TD_017_002_002", 1);
                    });

            System.out.println("IT_TD_017_002_002: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }

        @DisplayName("IT_TD_017_002_003@UpdateAssistanceRequest")
        @Test
        @Rollback()
        void IT_TD_017_002_003() throws Exception {
            System.out.println("IT_TD_017_002_003: TRANSFER MONEY SUCCESSFULLY");

            assertThrows(RuntimeException.class,
                    () -> {
                        assistanceService.updateAssistanceRequestStatus("IT_TD_017_002_002", 1);
                    });

            System.out.println("IT_TD_017_002_003: TRANSFER MONEY SUCCESSFULLY -- PASSED");
        }


    }
}