package com.banking.projectserver.controller;


import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.HMacJWTSigner;
import cn.hutool.jwt.signers.JWTSigner;
import com.banking.projectserver.entity.AssistanceRequestDisp;
import com.banking.projectserver.entity.BankAccount;
import com.banking.projectserver.entity.SystemFunction;
import com.banking.projectserver.entity.unityUser;
import com.banking.projectserver.mapper.BankAccountMapper;
import com.banking.projectserver.service.BankAccountService;
import com.banking.projectserver.service.SystemFunctionService;
import com.banking.projectserver.serviceImpl.BankAccountServiceImpl;
import com.banking.projectserver.serviceImpl.SystemFunctionServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import com.banking.projectserver.response.Response;
import com.banking.projectserver.serviceImpl.AssistanceServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
class AssistanceControllerTest {


    @BeforeAll
    public static void setupAll() {
        System.out.println("-- BEGINNING All UT_TC_003 TESTS --");
    }

    @BeforeEach
    public void setup() {
        System.out.println("\n-- BEGINNING NEXT UNIT TEST --");
    }

    public enum SFunction {
        SFunction1(1, "转账", "用于账户之间的转账"), SFunction2(4, "水费支付", "用于支付水费"), SFunction3(5, "电费支付", "用于支付电费"), SFunction4(6, "煤气费支付", "用于支付煤气费"), SFunction5(7, "开户", "用于开设新的银行账户");
        private final int functionId;
        private final String functionName;
        private final String functionDesc;

        SFunction(int functionId, String functionName, String functionDesc) {
            this.functionId = functionId;
            this.functionName = functionName;
            this.functionDesc = functionDesc;
        }
    }
    public static Stream<Arguments> assistanceRequestProvider01() {
        /*
         *  for addAssistanceRequest test
         */
        return Stream.of(arguments("Test123456", "23", "34", 7, 0.23, 0, "123456"), arguments("Test123456", "23", "34", 7, 0.23, 0, "123456"), arguments("Test123456", "1523", "98934", 7, 0.23, 0, "123456"));
    }

    public static Stream<Arguments> assistanceRequestProvider02() {
        return Stream.of(arguments("Test123456", "23", "34", 1, 0.23, 0, "123456"), arguments("Test123456", "23", "34", 1, 0.23, 0, "123456"), arguments("Test123456", "1523", "98934", 1, 0.23, 0, "123456"));
    }

    public static Stream<Arguments> assistanceRequestProvider03() {
        return Stream.of(arguments("Test123456", "23", "34", 2, 0.23, 0, "123456"), arguments("Test123456", "23", "34", 3, 0.23, 0, "123456"), arguments("Test123456", "1523", "98934", 4, 0.23, 0, "123456"));
    }

    public static Stream<Arguments> assistanceRequestProvider04() {
        return Stream.of(arguments("Test123456", "23", "34", 2, 0.23, 0, "123456"), arguments("Test123456", "23", "34", 2, 0.23, 0, "123456"), arguments("Test123456", "1523", "98934", 5, 0.23, 0, "123456"));
    }
    //v2
    public static Stream<Arguments> assistanceRequestProvider11() {

        return Stream.of(arguments("310104200101036099", "1234452800243479", "6666652800473480", 7, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "1234452800243479", "6666652800473480", 7, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "1234452800243479", "6666652800473480", 7, 99.89, 0, "wrong_password"));
    }

    public static Stream<Arguments> assistanceRequestProvider12() {
        return Stream.of(arguments("310104200101036099", "1234452800243479", "6666652800473480", 1, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "1234452800243479", "6666652800473480", 1, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "1234452800243479", "6666652800473480", 1, 0, 0, "QEwd/DWmy/4yGncCqBofQQ=="));
    }

    public static Stream<Arguments> assistanceRequestProvider13() {
        return Stream.of(arguments("310104200101036099", "1234452800243479", "6666652800473480", 2, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "1234452800243479", "6666652800473480", 3, 99.89, 0, "wrong_password"),
                arguments("310104200101036099", "1234452800243479", "6666652800473480", 4, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="));
    }

    public static Stream<Arguments> assistanceRequestProvider14() {
        return Stream.of(arguments("310104200101036099", "1234452800243479", "6666652800473480", 2, 0, 0, "QEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "1234452800243479", "6666652800473480", 2, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "1234452800243479", "6666652800473480", 5, 99.89, 0, "wrong_password"));
    }

    @DisplayName("GetFunctionId.AssistanceController.controller")
    @Nested
    class UT_TC_003_001 {
        @DisplayName("GetFunctionId_001")
        @ParameterizedTest
        @ValueSource(ints = {1, 4, 5, 6, 7})
        public void UT_TC_003_001_001_001(int functionId) {
            System.out.println("UT_TC_003_001_001_001_CASE" + functionId);

            AssistanceController assistanceController = new AssistanceController();
            SystemFunction targetSystemFunction = new SystemFunction();

            switch (functionId) {
                case 1:
                    targetSystemFunction.setFunctionID(SFunction.SFunction1.functionId);
                    targetSystemFunction.setFunctionName(SFunction.SFunction1.functionName);
                    targetSystemFunction.setFunctionDescription(SFunction.SFunction1.functionDesc);
                    break;
                case 4:
                    targetSystemFunction.setFunctionID(SFunction.SFunction2.functionId);
                    targetSystemFunction.setFunctionName(SFunction.SFunction2.functionName);
                    targetSystemFunction.setFunctionDescription(SFunction.SFunction2.functionDesc);
                    break;
                case 5:
                    targetSystemFunction.setFunctionID(SFunction.SFunction3.functionId);
                    targetSystemFunction.setFunctionName(SFunction.SFunction3.functionName);
                    targetSystemFunction.setFunctionDescription(SFunction.SFunction3.functionDesc);
                    break;
                case 6:
                    targetSystemFunction.setFunctionID(SFunction.SFunction4.functionId);
                    targetSystemFunction.setFunctionName(SFunction.SFunction4.functionName);
                    targetSystemFunction.setFunctionDescription(SFunction.SFunction4.functionDesc);
                    break;
                case 7:
                    targetSystemFunction.setFunctionID(SFunction.SFunction5.functionId);
                    targetSystemFunction.setFunctionName(SFunction.SFunction5.functionName);
                    targetSystemFunction.setFunctionDescription(SFunction.SFunction5.functionDesc);
                    break;
                default:
                    break;
            }

            System.out.println("Target System Function ID:" + targetSystemFunction.getFunctionID());

            SystemFunctionServiceImpl mock = mock(SystemFunctionServiceImpl.class);
            when(mock.getSystemFunctionById(functionId)).thenReturn(targetSystemFunction);
            assistanceController.setSystemFunctionService(mock);

            assertEquals(Response.OK().data("systemFunction", targetSystemFunction), assistanceController.getSystemFunctionById(functionId));

            System.out.println("UT_TC_003_001_001_001_CASE" + functionId + " -- PASSED");
        }


        @DisplayName("GetFunctionId_002")
        @ParameterizedTest
        @ValueSource(ints = {0, 2, 3, 8, 10, 100, 1000, 65535, -1, -65535})
        public void UT_TC_003_001_002_001(int functionId) {
            System.out.println("UT_TC_003_001_002_001_CASE" + functionId);

            AssistanceController assistanceController = new AssistanceController();
            SystemFunctionServiceImpl mock = mock(SystemFunctionServiceImpl.class);
            when(mock.getSystemFunctionById(functionId)).thenReturn(null);
            assistanceController.setSystemFunctionService(mock);
            assertEquals(Response.unknownError().message("功能不存在"), assistanceController.getSystemFunctionById(functionId));
            System.out.println("UT_TC_003_001_001_001_" + functionId + " -- PASSED");
        }

    }


    @DisplayName("GetUnityIDByUserID.AssistanceController.controller")
    @Nested
    class UT_TC_003_002 {

        private AssistanceController assistanceController;
        private MockHttpServletRequest mockedRequest;
        private AssistanceServiceImpl mockedAssistanceService;
        private String userID;

        @BeforeEach
        void UT_TC_003_002_INIT() {

            System.out.println("-- Initializing variables for current UT_TC_003_002 Test --");

            //Class Initialization
            assistanceController = new AssistanceController();
            mockedRequest = new MockHttpServletRequest();
            mockedAssistanceService = mock(AssistanceServiceImpl.class);

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


            System.out.println("-- FINISHED INITIALIZATION --");

        }


        @ParameterizedTest
        @ValueSource(strings = {"United and Authed"})
        void UT_TC_003_002_001_001(String unityID) {

            System.out.println("UT_TC_003_002_001_001: " + unityID);

            unityUser UnityID = new unityUser();
            UnityID.setUnityID(unityID);
            UnityID.setAuthorization(1);

            //让下层函数查出来的UnityID为预设值
            when(mockedAssistanceService.getUnityUserByUserID(userID)).thenReturn(UnityID);
            assistanceController.setAssistanceService(mockedAssistanceService);

            Response actualResponse = assistanceController.getUnityIDByUserID(mockedRequest, unityID);
            assertEquals(Response.OK().data("UnityID", UnityID), actualResponse);

            System.out.println("UT_TC_003_002_001_001: " + unityID + " -- PASSED");
        }

        @ParameterizedTest
        @ValueSource(strings = {"United but NOT Authed"})
        void UT_TC_003_002_002_001(String unityID) {

            System.out.println("UT_TC_003_002_002_001: " + unityID);

            unityUser UnityID = new unityUser();
            UnityID.setUnityID(unityID);
            UnityID.setAuthorization(0);

            //让下层函数查出来的UnityID为预设值
            when(mockedAssistanceService.getUnityUserByUserID(userID)).thenReturn(UnityID);
            assistanceController.setAssistanceService(mockedAssistanceService);

            Response actualResponse = assistanceController.getUnityIDByUserID(mockedRequest, unityID);
            assertEquals(Response.invalidUser().message("用户不存在"), actualResponse);

            System.out.println("UT_TC_003_002_002_001: " + unityID + " -- PASSED");
        }

        @ParameterizedTest
        @ValueSource(strings = {"NOT United but Authed"})
        void UT_TC_003_002_003_001(String unityID) {

            System.out.println("UT_TC_003_002_003_001: " + unityID);

            unityUser actualReturnUnityID = new unityUser();
            actualReturnUnityID.setUnityID("");
            actualReturnUnityID.setAuthorization(1);

            //让下层函数查出来的UnityID为预设值
            when(mockedAssistanceService.getUnityUserByUserID(userID)).thenReturn(actualReturnUnityID);
            assistanceController.setAssistanceService(mockedAssistanceService);

            Response actualResponse = assistanceController.getUnityIDByUserID(mockedRequest, unityID);
            assertEquals(Response.invalidUser().message("用户不存在"), actualResponse);

            System.out.println("UT_TC_003_002_003_001: " + unityID + " -- PASSED");

        }

        @ParameterizedTest
        @ValueSource(strings = {"NOT United and NOT Authed"})
        void UT_TC_003_002_004_001(String unityID) {

            System.out.println("UT_TC_003_002_004_001: " + unityID);

            unityUser actualReturnUnityID = new unityUser();
            actualReturnUnityID.setUnityID("");
            actualReturnUnityID.setAuthorization(0);

            //让下层函数查出来的UnityID为预设值
            when(mockedAssistanceService.getUnityUserByUserID(userID)).thenReturn(actualReturnUnityID);
            assistanceController.setAssistanceService(mockedAssistanceService);

            Response actualResponse = assistanceController.getUnityIDByUserID(mockedRequest, unityID);
            assertEquals(Response.invalidUser().message("用户不存在"), actualResponse);

            System.out.println("UT_TC_003_002_004_001: " + unityID + " -- PASSED");

        }
    }


    @DisplayName("UpdateAssistanceRequestStatus.AssistanceController.controller")
    @Nested
    class UT_TC_003_003 {
        private AssistanceController assistanceController;
        private AssistanceServiceImpl mockedAssistanceService = mock(AssistanceServiceImpl.class);

        @BeforeEach
        void UT_TC_003_003_INIT() {

            System.out.println("-- Initializing variables for UT_TC_003_003 --");
            assistanceController = new AssistanceController();
            mockedAssistanceService = mock(AssistanceServiceImpl.class);
            System.out.println("FINISHED Initialization.");
        }

        @Test
        void UT_TC_003_003_001_001() throws Exception {
            System.out.println("UT_TC_003_003_001_001: Test case for SUCCESSFUL Update");
            when(mockedAssistanceService.updateAssistanceRequestStatus("assistanceID", 1)).thenReturn(true);
            assistanceController.setAssistanceService(mockedAssistanceService);

            assertEquals(Response.OK().message("更新成功"), assistanceController.updateAssistanceRequestStatus("assistanceID", 1));
            System.out.println("UT_TC_003_003_001_001: Test case for SUCCESSFUL Update -- PASSED");

        }

        @Test
        void UT_TC_003_003_002_001() throws Exception {
            System.out.println("UT_TC_003_003_001_001: Test case for FAILURE Update");

            when(mockedAssistanceService.updateAssistanceRequestStatus("assistanceID", 1)).thenReturn(false);
            assistanceController.setAssistanceService(mockedAssistanceService);
            assertEquals(Response.unknownError().message("更新失败"), assistanceController.updateAssistanceRequestStatus("assistanceID", 1));
            System.out.println("UT_TC_003_003_001_001: Test case for FAILURE Update -- PASSED");

        }
    }

    @DisplayName("AddAssistanceRequest.AssistanceController.controller")
    @Nested
    class UT_TC_003_004 {

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider01")
        void UT_TC_003_004_001_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //请求开户
            System.out.println("UT_TC_003_004_001_001: CASE SUCCESSFULLY ADDED");
            AssistanceServiceImpl mock = mock(AssistanceServiceImpl.class);
            when(mock.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished)).thenReturn(true);
            BankAccountServiceImpl bankMock = mock(BankAccountServiceImpl.class);
            when(bankMock.getAccountPassword(accountID)).thenReturn(password);

            AssistanceController assistanceController = new AssistanceController();
            assistanceController.setAssistanceService(mock);
            assistanceController.setBankAccountService(bankMock);
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.OK().message("请求开户成功"));
            System.out.println("UT_TC_003_004_001_001: CASE SUCCESSFULLY ADDED -- PASSED");

        }

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider02")
        void UT_TC_003_004_002_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //账户不存在
            System.out.println("UT_TC_003_004_002_001: CASE ACCOUNT DOES NOT EXIST");

            AssistanceServiceImpl mock = mock(AssistanceServiceImpl.class);
            when(mock.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished)).thenReturn(true);
            BankAccountServiceImpl bankMock = mock(BankAccountServiceImpl.class);
            when(bankMock.getAccountPassword(accountID)).thenReturn(password);
            when(bankMock.getBankAccountInfoById(accountID)).thenReturn(null);

            AssistanceController assistanceController = new AssistanceController();
            assistanceController.setAssistanceService(mock);
            assistanceController.setBankAccountService(bankMock);
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.invalidUser().message("账户不存在-转账"));
            System.out.println("UT_TC_003_004_002_001: CASE ACCOUNT DOES NOT EXIST -- PASSED");


        }

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider03")
        void UT_TC_003_004_003_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //密码错误
            System.out.println("UT_TC_003_004_003_001: CASE WRONG PASSWORD");

            AssistanceServiceImpl mock = mock(AssistanceServiceImpl.class);
            when(mock.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished)).thenReturn(true);
            BankAccountServiceImpl bankMock = mock(BankAccountServiceImpl.class);
            when(bankMock.getAccountPassword(accountID)).thenReturn("password");
            when(bankMock.getBankAccountInfoById(accountID)).thenReturn(new BankAccount());

            AssistanceController assistanceController = new AssistanceController();
            assistanceController.setAssistanceService(mock);
            assistanceController.setBankAccountService(bankMock);
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.invalidService().message("添加失败"));
            System.out.println("UT_TC_003_004_003_001: CASE WRONG PASSWORD -- PASSED");

        }

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider04")
        void UT_TC_003_004_004_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //成功添加
            System.out.println("UT_TC_003_004_004_001: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED");

            AssistanceServiceImpl mock = mock(AssistanceServiceImpl.class);
            when(mock.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished)).thenReturn(true);
            BankAccountServiceImpl bankMock = mock(BankAccountServiceImpl.class);
            when(bankMock.getAccountPassword(accountID)).thenReturn(password);
            when(bankMock.getBankAccountInfoById(accountID)).thenReturn(new BankAccount());

            AssistanceController assistanceController = new AssistanceController();
            assistanceController.setAssistanceService(mock);
            assistanceController.setBankAccountService(bankMock);
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.OK().message("添加成功"));
            System.out.println("UT_TC_003_004_004_001: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED -- PASSED");

        }
    }

    @DisplayName("AddAssistanceRequest.AssistanceController.controller")
    @Nested
    class UT_TC_003_004_v2 {


        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider11")
        void UT_TC_003_004_001_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //请求开户
            System.out.println("UT_TC_003_004_001_001: CASE SUCCESSFULLY ADDED");
            AssistanceServiceImpl mock = mock(AssistanceServiceImpl.class);
            when(mock.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished)).thenReturn(true);
            BankAccountServiceImpl bankMock = mock(BankAccountServiceImpl.class);
            when(bankMock.getAccountPassword(accountID)).thenReturn(password);

            AssistanceController assistanceController = new AssistanceController();
            assistanceController.setAssistanceService(mock);
            assistanceController.setBankAccountService(bankMock);
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.OK().message("请求开户成功"));
            System.out.println("UT_TC_003_004_001_001: CASE SUCCESSFULLY ADDED -- PASSED");

        }

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider12")
        void UT_TC_003_004_002_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //账户不存在
            System.out.println("UT_TC_003_004_002_001: CASE ACCOUNT DOES NOT EXIST");

            AssistanceServiceImpl mock = mock(AssistanceServiceImpl.class);
            when(mock.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished)).thenReturn(true);
            BankAccountServiceImpl bankMock = mock(BankAccountServiceImpl.class);
            when(bankMock.getAccountPassword(accountID)).thenReturn(password);
            when(bankMock.getBankAccountInfoById(accountID)).thenReturn(null);

            AssistanceController assistanceController = new AssistanceController();
            assistanceController.setAssistanceService(mock);
            assistanceController.setBankAccountService(bankMock);
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.invalidUser().message("账户不存在-转账"));
            System.out.println("UT_TC_003_004_002_001: CASE ACCOUNT DOES NOT EXIST -- PASSED");


        }

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider13")
        void UT_TC_003_004_003_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //密码错误
            System.out.println("UT_TC_003_004_003_001: CASE WRONG PASSWORD");

            AssistanceServiceImpl mock = mock(AssistanceServiceImpl.class);
            when(mock.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished)).thenReturn(true);
            BankAccountServiceImpl bankMock = mock(BankAccountServiceImpl.class);
            when(bankMock.getAccountPassword(accountID)).thenReturn("password");
            when(bankMock.getBankAccountInfoById(accountID)).thenReturn(new BankAccount());

            AssistanceController assistanceController = new AssistanceController();
            assistanceController.setAssistanceService(mock);
            assistanceController.setBankAccountService(bankMock);
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.invalidService().message("添加失败"));
            System.out.println("UT_TC_003_004_003_001: CASE WRONG PASSWORD -- PASSED");

        }

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider14")
        void UT_TC_003_004_004_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //成功添加
            System.out.println("UT_TC_003_004_004_001: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED");

            AssistanceServiceImpl mock = mock(AssistanceServiceImpl.class);
            when(mock.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished)).thenReturn(true);
            BankAccountServiceImpl bankMock = mock(BankAccountServiceImpl.class);
            when(bankMock.getAccountPassword(accountID)).thenReturn(password);
            when(bankMock.getBankAccountInfoById(accountID)).thenReturn(new BankAccount());

            AssistanceController assistanceController = new AssistanceController();
            assistanceController.setAssistanceService(mock);
            assistanceController.setBankAccountService(bankMock);
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.OK().message("添加成功"));
            System.out.println("UT_TC_003_004_004_001: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED -- PASSED");

        }
    }

    @DisplayName("GetAssistanceRequestByUserID.AssistanceController.controller")
    @Nested
    class UT_TC_003_005 {

        private AssistanceController assistanceController;
        private MockHttpServletRequest mockedRequest;
        private AssistanceServiceImpl mockedAssistanceService;
        private String userID;
        private List emptyList;

        @BeforeEach
        void UT_TC_003_005_INIT() {

            System.out.println("Initializing variables for current UT_TC_003_004 Test");

            //Class Initialization
            assistanceController = new AssistanceController();
            mockedRequest = new MockHttpServletRequest();

            //Token Signature Initialization
            byte[] TOKEN_SECRET = "UT_TC_003_005".getBytes(StandardCharsets.UTF_8);
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

            emptyList = new ArrayList<AssistanceRequestDisp>();

            System.out.println("-- FINISHED INITIALIZATION --");

        }

        @Test
        void UT_TC_003_005_001_001() {
            System.out.println("UT_TC_003_005_001_001: CASE SUCCESS RESPONSE");
            mockedAssistanceService = mock(AssistanceServiceImpl.class);
            when(mockedAssistanceService.getAssistanceRequestByUserID(userID)).thenReturn(emptyList);
            assistanceController.setAssistanceService(mockedAssistanceService);
            Response actualResponse = assistanceController.getAssistanceRequestByUserID(mockedRequest);
            verify(mockedAssistanceService).getAssistanceRequestByUserID(userID);
            assertEquals(Response.OK().data("assistanceRequestDisp", emptyList), actualResponse);
            System.out.println("UT_TC_003_005_001_001: CASE SUCCESS RESPONSE -- PASSED");
        }

        @Test
        void UT_TC_003_005_002_001() {
            System.out.println("UT_TC_003_005_002_001: CASE FAILURE RESPONSE");
            mockedAssistanceService = mock(AssistanceServiceImpl.class);
            when(mockedAssistanceService.getAssistanceRequestByUserID(userID)).thenReturn(null);
            assistanceController.setAssistanceService(mockedAssistanceService);
            Response actualResponse = assistanceController.getAssistanceRequestByUserID(mockedRequest);
            verify(mockedAssistanceService).getAssistanceRequestByUserID(userID);
            assertEquals(Response.unknownError().message("记录不存在"), actualResponse);
            System.out.println("UT_TC_003_005_002_001: CASE FAILURE RESPONSE -- PASSED");
        }
    }

    @DisplayName("GetUnfinishedAssistanceRequestByUserID.AssistanceController.controller")
    @Nested
    class UT_TC_003_006 {

        private AssistanceController assistanceController;
        private MockHttpServletRequest mockedRequest;
        private AssistanceServiceImpl mockedAssistanceService;
        private String userID;
        private List emptyList;

        @BeforeEach
        void UT_TC_003_006_INIT() {

            System.out.println("-- Initializing variables for current UT_TC_003_004 Test --");

            //Class Initialization
            assistanceController = new AssistanceController();
            mockedRequest = new MockHttpServletRequest();

            //Token Signature Initialization
            byte[] TOKEN_SECRET = "UT_TC_003_006".getBytes(StandardCharsets.UTF_8);
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

            emptyList = new ArrayList<AssistanceRequestDisp>();

            System.out.println("FINISHED INITIALIZATION");

        }

        @Test
        void UT_TC_003_006_001_001() {
            System.out.println("UT_TC_003_006_001_001: CASE SUCCESS RESPONSE");
            mockedAssistanceService = mock(AssistanceServiceImpl.class);
            when(mockedAssistanceService.getUnfinishedAssistanceRequestByUserID(userID)).thenReturn(emptyList);
            assistanceController.setAssistanceService(mockedAssistanceService);
            Response actualResponse = assistanceController.getUnfinishedAssistanceRequestByUserID(mockedRequest);
            verify(mockedAssistanceService).getUnfinishedAssistanceRequestByUserID(userID);
            assertEquals(Response.OK().data("assistanceRequestDisp", emptyList), actualResponse);
            System.out.println("UT_TC_003_006_001_001: CASE SUCCESS RESPONSE -- PASSED");
        }

        @Test
        void UT_TC_003_006_002_001() {
            System.out.println("UT_TC_003_006_002_001: CASE FAILURE RESPONSE");
            mockedAssistanceService = mock(AssistanceServiceImpl.class);
            when(mockedAssistanceService.getUnfinishedAssistanceRequestByUserID(userID)).thenReturn(null);
            assistanceController.setAssistanceService(mockedAssistanceService);
            Response actualResponse = assistanceController.getUnfinishedAssistanceRequestByUserID(mockedRequest);
            verify(mockedAssistanceService).getUnfinishedAssistanceRequestByUserID(userID);
            assertEquals(Response.unknownError().message("记录不存在"), actualResponse);
            System.out.println("UT_TC_003_006_002_001: CASE FAILURE RESPONSE -- PASSED");
        }
    }

    //以下为集成测试

    //集成测试数据源
    public static Stream<Arguments> assistanceRequestProvider05() {
        //String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password
        return Stream.of(
                //密码用密文形式
                arguments("310104200101036099", "6666652800243479", "6666652800473480", 1, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="), arguments("310104200101036099", "6666652800243479", "6666652800473480", 1, 99.79, 0, "QEwd/DWmy/4yGncCqBofQQ=="), arguments("310104200101036099", "6666652800243479", "6666652800473480", 1, 99.99, 0, "QEwd/DWmy/4yGncCqBofQQ=="));
    }

    public static Stream<Arguments> assistanceRequestProvider06() {
        //String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password
        return Stream.of(
                //密码用密文形式
                arguments("123456789098765432", "1234452800243479", "6666652800473480", 3, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ=="), arguments("123456789098765432", "1234652200243479", "6666652800473480", 3, 99.99, 0, "QEwd/DWmy/4yGncCqBofQQ=="), arguments("123456789098765432", "4592121300243479", "6666652800473480", 3, 99.89, 0, "QEwd/DWmy/4yGncCqBofQQ==")

        );
    }

    public static Stream<Arguments> assistanceRequestProvider07() {
        //String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password
        return Stream.of(
                //密码用密文形式
                arguments("310104200101036099", "6666652800243479", "6666652800473480", 1, 99.89, 0, "AEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "6666652800243479", "6666652800473480", 1, 99.79, 0, "BEwd/DWmy/4yGncCqBofQQ=="),
                arguments("310104200101036099", "6666652800243479", "6666652800473480", 1, 99.99, 0, "CEwd/DWmy/4yGncCqBofQQ==")
        );
    }

    @DisplayName("AddAssistanceRequest.AssistanceController.controller")
    @Nested
    @SpringBootTest
    class IT_TD_011 {

        @Autowired
        private AssistanceController assistanceController;


        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider05")
        void IT_TD_011_001_001(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //成功添加请求
            System.out.println("IT_TD_011_001_001: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED");


            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.OK().message("添加成功"));
            System.out.println("IT_TD_011_001_001: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED -- PASSED");

        }

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider06")
        void IT_TD_011_001_002(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //accountID 不存在
            System.out.println("IT_TD_011_001_001: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED");

            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password), Response.invalidUser().message("账户不存在"));
            System.out.println("IT_TD_011_001_001: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED -- PASSED");

        }

        @ParameterizedTest
        @MethodSource("com.banking.projectserver.controller.AssistanceControllerTest#assistanceRequestProvider07")
        void IT_TD_011_001_003(String userID, String accountID, String payeeAccountID, int functionID, double amount, int isFinished, String password) {
            //密码错误
            System.out.println("IT_TD_011_001_003: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED");
            assertEquals(assistanceController.addAssistanceRequest(userID, accountID, payeeAccountID, functionID, amount, isFinished, password),
                    Response.invalidService().message("添加失败"));
            System.out.println("IT_TD_011_001_003: CASE NON ASSISTANCE REQUEST SUCCESSFULLY ADDED -- PASSED");
        }


    }



    @DisplayName("UpdateAssistanceRequestStatus.AssistanceController.controller")
    @Nested
    @SpringBootTest
    class IT_TD_012 {

        @Autowired
        private AssistanceController assistanceController;

        private final String existedAssistanceID = "fa3defab-b37d-402d-ab68-2ddf5ea54a1c";

        @Test
        void IT_TD_012_001_001() throws Exception {
            //成功更新状态
            System.out.println("IT_TD_012_001_001: CASE ID VALID isFinished = 1");

            assertEquals(assistanceController.updateAssistanceRequestStatus(existedAssistanceID, 1), Response.OK().message("更新成功"));
            System.out.println("IT_TD_012_001_001: CASE ID VALID isFinished = 1 -- PASSED");

        }

        @Test
        void IT_TD_012_001_002() throws Exception {
            //成功更新状态
            System.out.println("IT_TD_012_001_002: CASE ID VALID isFinished = 0");

            assertEquals(assistanceController.updateAssistanceRequestStatus(existedAssistanceID, 0), Response.OK().message("更新成功"));

            System.out.println("IT_TD_012_001_002: CASE ID VALID isFinished = 0 -- PASSED");

        }


    }

    @DisplayName("GetAssistanceRequestByUserID.AssistanceController.controller")
    @Nested
    @SpringBootTest
    class IT_TD_013 {

        private String userID;
        private MockHttpServletRequest mockedRequest;

        private List emptyList = new ArrayList<AssistanceRequestDisp>();

        @Autowired
        private AssistanceController assistanceController;

        @Test
        void IT_TD_013_001_001() {

            mockedRequest = new MockHttpServletRequest();

            //Token Signature Initialization
            byte[] TOKEN_SECRET = "IT_TD_013".getBytes(StandardCharsets.UTF_8);
            JWTSigner jwtSigner = new HMacJWTSigner("HmacSHA256", TOKEN_SECRET);

            //Token Header Initialization
            Map<String, Object> mockedHeader = new HashMap<>(2);
            mockedHeader.put("typ", "JWT");
            mockedHeader.put("alg", "HS256");

            //Token Payload Initialization
            Map<String, Object> mockedPayload = new HashMap<>(1);
            //这里模拟登陆到系统的用户userid
            userID = "31010420010103609";
            //此处的userID被加密到token中了
            mockedPayload.put("uid", userID);

            String TEST_TOKEN = JWTUtil.createToken(mockedHeader, mockedPayload, jwtSigner);
            mockedRequest.addHeader("token", TEST_TOKEN);

            System.out.println("IT_TD_013_001_001: CASE SUCCESS");

            System.out.println("IT_TD_013_001_001: CASE SUCCESS -- PASSED");

        }

        @Test
        void IT_TD_013_002_001() {

            mockedRequest = new MockHttpServletRequest();

            //Token Signature Initialization
            byte[] TOKEN_SECRET = "IT_TD_013".getBytes(StandardCharsets.UTF_8);
            JWTSigner jwtSigner = new HMacJWTSigner("HmacSHA256", TOKEN_SECRET);

            //Token Header Initialization
            Map<String, Object> mockedHeader = new HashMap<>(2);
            mockedHeader.put("typ", "JWT");
            mockedHeader.put("alg", "HS256");

            //Token Payload Initialization
            Map<String, Object> mockedPayload = new HashMap<>(1);
            //这里模拟登陆到系统的用户userid
            userID = "440500200106282222";
            //此处的userID被加密到token中了
            mockedPayload.put("uid", userID);

            String TEST_TOKEN = JWTUtil.createToken(mockedHeader, mockedPayload, jwtSigner);
            mockedRequest.addHeader("token", TEST_TOKEN);

            System.out.println("IT_TD_013_001_001_001: CASE NULL LIST");

            assertEquals(Response.OK().data("assistanceRequestDisp", emptyList), assistanceController.getAssistanceRequestByUserID(mockedRequest));

            System.out.println("IT_TD_013_001_001_001: CASE NULL LIST -- PASSED");

        }

    }

    @DisplayName("GetUnfinishedAssistanceRequestByUserID.AssistanceController.controller")
    @Nested
    @SpringBootTest
    class IT_TD_014 {

        private String userID;
        private MockHttpServletRequest mockedRequest;

        private List emptyList = new ArrayList<AssistanceRequestDisp>();

        @Autowired
        private AssistanceController assistanceController;

        @Test
        void IT_TD_014_001_001() {

            mockedRequest = new MockHttpServletRequest();

            //Token Signature Initialization
            byte[] TOKEN_SECRET = "IT_TD_014".getBytes(StandardCharsets.UTF_8);
            JWTSigner jwtSigner = new HMacJWTSigner("HmacSHA256", TOKEN_SECRET);

            //Token Header Initialization
            Map<String, Object> mockedHeader = new HashMap<>(2);
            mockedHeader.put("typ", "JWT");
            mockedHeader.put("alg", "HS256");

            //Token Payload Initialization
            Map<String, Object> mockedPayload = new HashMap<>(1);
            //这里模拟登陆到系统的用户userid
            userID = "310104200101036666";
            //此处的userID被加密到token中了
            mockedPayload.put("uid", userID);

            String TEST_TOKEN = JWTUtil.createToken(mockedHeader, mockedPayload, jwtSigner);
            mockedRequest.addHeader("token", TEST_TOKEN);

            System.out.println("IT_TD_014_001_001: CASE SUCCESS");

            assertEquals(Response.OK().data("assistanceRequestDisp", emptyList), assistanceController.getUnfinishedAssistanceRequestByUserID(mockedRequest));

            System.out.println("IT_TD_014_001_001: CASE SUCCESS -- PASSED");

        }

        @Test
        void IT_TD_014_002_001() {

            mockedRequest = new MockHttpServletRequest();

            //Token Signature Initialization
            byte[] TOKEN_SECRET = "IT_TD_013".getBytes(StandardCharsets.UTF_8);
            JWTSigner jwtSigner = new HMacJWTSigner("HmacSHA256", TOKEN_SECRET);

            //Token Header Initialization
            Map<String, Object> mockedHeader = new HashMap<>(2);
            mockedHeader.put("typ", "JWT");
            mockedHeader.put("alg", "HS256");

            //Token Payload Initialization
            Map<String, Object> mockedPayload = new HashMap<>(1);
            //这里模拟登陆到系统的用户userid
            userID = "440500200106282222";
            //此处的userID被加密到token中了
            mockedPayload.put("uid", userID);

            String TEST_TOKEN = JWTUtil.createToken(mockedHeader, mockedPayload, jwtSigner);
            mockedRequest.addHeader("token", TEST_TOKEN);

            System.out.println("IT_TD_014_001_001_001: CASE NULL LIST");

            assertEquals(Response.OK().data("assistanceRequestDisp", emptyList), assistanceController.getUnfinishedAssistanceRequestByUserID(mockedRequest));

            System.out.println("IT_TD_014_001_001_001: CASE NULL LIST -- PASSED");

        }

    }



}