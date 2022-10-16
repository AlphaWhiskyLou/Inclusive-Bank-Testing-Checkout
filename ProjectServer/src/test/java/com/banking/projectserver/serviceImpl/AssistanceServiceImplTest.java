package com.banking.projectserver.serviceImpl;

//import org.aspectj.lang.annotation.Before;
import com.banking.projectserver.entity.AssistanceRequest;
import com.banking.projectserver.entity.AssistanceRequestDisp;
import com.banking.projectserver.entity.unityUser;
import com.banking.projectserver.mapper.AssistanceRequestMapper;
import com.banking.projectserver.service.BankAccountService;
import com.banking.projectserver.service.UtilityService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
class  AssistanceServiceImplTest {

    @BeforeAll
    public static void setupAll(){
        System.out.println("\n-- BEGINNING UT_TC_005 TESTS --");
    }

    @BeforeEach
    void setup(){
        System.out.println("\n-- BEGINNING NEXT UNIT TEST --");
    }

    public static Stream<Arguments> UT_TC_005_002_002_001_ARGS() {
//        String assistanceID, int isFinished, String accountID, String payeeID, String userID, int functionID, BigDecimal amount
        return Stream.of(arguments("assistanceID", 1, "accountID", "payeeID", "userID", 1, new BigDecimal(1)));
    }

    public static Stream<Arguments> UT_TC_005_002_002_002_ARGS() {
//        String assistanceID, int isFinished, String accountID, String payeeID, String userID, int functionID, BigDecimal amount
        return Stream.of(arguments("assistanceID", 1, "accountID", "payeeID", "userID", 7, new BigDecimal(1)));
    }

    public static Stream<Arguments> UT_TC_005_002_002_003_ARGS() {
//        String assistanceID, int isFinished, String accountID, String payeeID, String userID, int functionID, BigDecimal amount
        return Stream.of(arguments("assistanceID", 1, "accountID", "payeeID", "userID", 4, new BigDecimal(1)), arguments("assistanceID", 1, "accountID", "payeeID", "userID", 5, new BigDecimal(1)), arguments("assistanceID", 1, "accountID", "payeeID", "userID", 6, new BigDecimal(1)));
    }

    @DisplayName("UT_TC_005_001_001_001")
    @Test
    void UT_TC_005_001_001_001() {
        System.out.println("UT_TC_005_001_001_001: Get Unity ID");

        //getUnityIDByUserID
        AssistanceServiceImpl assistanceService = new AssistanceServiceImpl();
        AssistanceRequestMapper mockedAssistanceRequestMapper = mock(AssistanceRequestMapper.class);
        unityUser u = new unityUser();
        when(mockedAssistanceRequestMapper.getUnityIDByUserID("user")).thenReturn(u);

        assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);

        assertEquals(u,assistanceService.getUnityUserByUserID("user"));
        verify(mockedAssistanceRequestMapper).getUnityIDByUserID("user");
        System.out.println("UT_TC_005_001_001_001: Get Unity ID -- PASSED");


    }

    @DisplayName("UT_TC_005_002: Logic Coverage Testing")
    @Nested
    class UT_TC_005_002 {

        private AssistanceServiceImpl assistanceService;
        private AssistanceRequestMapper mockedAssistanceRequestMapper;
        private BankAccountService mockedBankAccountService;
        private UtilityService mockedUtilityService;

        @BeforeEach
        void UT_TC_005_002_INIT(){
            assistanceService = new AssistanceServiceImpl();
            mockedAssistanceRequestMapper = mock(AssistanceRequestMapper.class);
        }

        @DisplayName("UT_TC_005_002_002 - L2")
        @Test
        void UT_TC_005_002_001_001() throws Exception {

            System.out.println("UT_TC_005_002_001_001: CASE ISFINISHED = 0");

            doNothing().when(mockedAssistanceRequestMapper).updateAssistanceRequestStatus("Placeholder",0);
            assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);
            assertTrue(assistanceService.updateAssistanceRequestStatus("Placeholder", 0));
            verify(mockedAssistanceRequestMapper,times(1)).updateAssistanceRequestStatus("Placeholder",0);

            System.out.println("UT_TC_005_002_001_001: CASE ISFINISHED = 0 -- PASSED");

        }

        @DisplayName("UT_TC_005_002_001 - L1")
        @ParameterizedTest
        @MethodSource("com.banking.projectserver.serviceImpl.AssistanceServiceImplTest#UT_TC_005_002_002_001_ARGS")
        void UT_TC_005_002_002_001(String assistanceID, int isFinished, String accountID, String payeeID, String userID, int functionID, BigDecimal amount) throws Exception {
            System.out.println("UT_TC_005_002_002_001: CASE ISFINISHED = 1");
            System.out.println("assistanceID - " +assistanceID);
            System.out.println("isFinished - " +isFinished);
            System.out.println("accountID - " +accountID);
            System.out.println("payeeID - " +payeeID);
            System.out.println("userID - " +userID);
            System.out.println("functionID - " +functionID);
            System.out.println("amount - " +amount);

            doNothing().when(mockedAssistanceRequestMapper).updateAssistanceRequestStatus(assistanceID, isFinished);

            AssistanceRequest expectedAssistanceRequest = new AssistanceRequest();

            expectedAssistanceRequest.setFunctionID(functionID);
            expectedAssistanceRequest.setAssistanceID(assistanceID);
            expectedAssistanceRequest.setAccountID(accountID);
            expectedAssistanceRequest.setPayeeAccountID(payeeID);
            expectedAssistanceRequest.setAmount(amount);
            expectedAssistanceRequest.setUserID(userID);

            when(mockedAssistanceRequestMapper.getAssistanceRequestByAssistanceID(assistanceID)).thenReturn(expectedAssistanceRequest);

            mockedBankAccountService = mock(BankAccountService.class);
            mockedUtilityService = mock(UtilityService.class);

            doNothing().when(mockedBankAccountService).transferMoney(expectedAssistanceRequest.getAccountID(),expectedAssistanceRequest.getPayeeAccountID(),expectedAssistanceRequest.getAmount());

            when(mockedBankAccountService.openAccount(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getPayeeAccountID())).thenReturn("TRUE");

            when(mockedUtilityService.utilityPayment(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getAmount(), expectedAssistanceRequest.getFunctionID(), expectedAssistanceRequest.getAccountID())).thenReturn(true);

            assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);
            assistanceService.setBankAccountService(mockedBankAccountService);
            assistanceService.setUtilityService(mockedUtilityService);


            assertTrue(assistanceService.updateAssistanceRequestStatus(assistanceID,isFinished));
            verify(mockedBankAccountService, times(1)).transferMoney(expectedAssistanceRequest.getAccountID(),expectedAssistanceRequest.getPayeeAccountID(),expectedAssistanceRequest.getAmount());
            verify(mockedBankAccountService, times(0)).openAccount(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getPayeeAccountID());
            verify(mockedUtilityService, times(0)).utilityPayment(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getAmount(), expectedAssistanceRequest.getFunctionID(), expectedAssistanceRequest.getAccountID());


            System.out.println("UT_TC_005_002_002_001: CASE ISFINISHED = 1 -- PASSED");
        }

        @DisplayName("UT_TC_005_002_003 - L3")
        @ParameterizedTest
        @MethodSource("com.banking.projectserver.serviceImpl.AssistanceServiceImplTest#UT_TC_005_002_002_002_ARGS")
        void UT_TC_005_002_002_002(String assistanceID, int isFinished, String accountID, String payeeID, String userID, int functionID, BigDecimal amount) throws Exception {
            System.out.println("UT_TC_005_002_002_002: CASE ISFINISHED = 1");
            System.out.println("assistanceID - " +assistanceID);
            System.out.println("isFinished - " +isFinished);
            System.out.println("accountID - " +accountID);
            System.out.println("payeeID - " +payeeID);
            System.out.println("userID - " +userID);
            System.out.println("functionID - " +functionID);
            System.out.println("amount - " +amount);

            doNothing().when(mockedAssistanceRequestMapper).updateAssistanceRequestStatus(assistanceID, isFinished);

            AssistanceRequest expectedAssistanceRequest = new AssistanceRequest();

            expectedAssistanceRequest.setFunctionID(functionID);
            expectedAssistanceRequest.setAssistanceID(assistanceID);
            expectedAssistanceRequest.setAccountID(accountID);
            expectedAssistanceRequest.setPayeeAccountID(payeeID);
            expectedAssistanceRequest.setAmount(amount);
            expectedAssistanceRequest.setUserID(userID);

            when(mockedAssistanceRequestMapper.getAssistanceRequestByAssistanceID(assistanceID)).thenReturn(expectedAssistanceRequest);

            mockedBankAccountService = mock(BankAccountService.class);
            mockedUtilityService = mock(UtilityService.class);

            doNothing().when(mockedBankAccountService).transferMoney(expectedAssistanceRequest.getAccountID(),expectedAssistanceRequest.getPayeeAccountID(),expectedAssistanceRequest.getAmount());

            when(mockedBankAccountService.openAccount(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getPayeeAccountID())).thenReturn("TRUE");

            when(mockedUtilityService.utilityPayment(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getAmount(), expectedAssistanceRequest.getFunctionID(), expectedAssistanceRequest.getAccountID())).thenReturn(true);

            assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);
            assistanceService.setBankAccountService(mockedBankAccountService);
            assistanceService.setUtilityService(mockedUtilityService);


            assertTrue(assistanceService.updateAssistanceRequestStatus(assistanceID,isFinished));
            verify(mockedBankAccountService, times(0)).transferMoney(expectedAssistanceRequest.getAccountID(),expectedAssistanceRequest.getPayeeAccountID(),expectedAssistanceRequest.getAmount());
            verify(mockedBankAccountService, times(1)).openAccount(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getPayeeAccountID());
            verify(mockedUtilityService, times(0)).utilityPayment(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getAmount(), expectedAssistanceRequest.getFunctionID(), expectedAssistanceRequest.getAccountID());


            System.out.println("UT_TC_005_002_002_002: CASE ISFINISHED = 1 -- PASSED");
        }

        @DisplayName("UT_TC_005_002_004 - L4")
        @ParameterizedTest
        @MethodSource("com.banking.projectserver.serviceImpl.AssistanceServiceImplTest#UT_TC_005_002_002_003_ARGS")
        void UT_TC_005_002_002_003(String assistanceID, int isFinished, String accountID, String payeeID, String userID, int functionID, BigDecimal amount) throws Exception {
            System.out.println("UT_TC_005_002_002_003: CASE ISFINISHED = 1");
            System.out.println("assistanceID - " +assistanceID);
            System.out.println("isFinished - " +isFinished);
            System.out.println("accountID - " +accountID);
            System.out.println("payeeID - " +payeeID);
            System.out.println("userID - " +userID);
            System.out.println("functionID - " +functionID);
            System.out.println("amount - " +amount);

            doNothing().when(mockedAssistanceRequestMapper).updateAssistanceRequestStatus(assistanceID, isFinished);

            AssistanceRequest expectedAssistanceRequest = new AssistanceRequest();

            expectedAssistanceRequest.setFunctionID(functionID);
            expectedAssistanceRequest.setAssistanceID(assistanceID);
            expectedAssistanceRequest.setAccountID(accountID);
            expectedAssistanceRequest.setPayeeAccountID(payeeID);
            expectedAssistanceRequest.setAmount(amount);
            expectedAssistanceRequest.setUserID(userID);

            when(mockedAssistanceRequestMapper.getAssistanceRequestByAssistanceID(assistanceID)).thenReturn(expectedAssistanceRequest);

            mockedBankAccountService = mock(BankAccountService.class);
            mockedUtilityService = mock(UtilityService.class);

            doNothing().when(mockedBankAccountService).transferMoney(expectedAssistanceRequest.getAccountID(),expectedAssistanceRequest.getPayeeAccountID(),expectedAssistanceRequest.getAmount());

            when(mockedBankAccountService.openAccount(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getPayeeAccountID())).thenReturn("TRUE");

            when(mockedUtilityService.utilityPayment(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getAmount(), expectedAssistanceRequest.getFunctionID(), expectedAssistanceRequest.getAccountID())).thenReturn(true);

            assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);
            assistanceService.setBankAccountService(mockedBankAccountService);
            assistanceService.setUtilityService(mockedUtilityService);


            assertTrue(assistanceService.updateAssistanceRequestStatus(assistanceID,isFinished));
            verify(mockedBankAccountService, times(0)).transferMoney(expectedAssistanceRequest.getAccountID(),expectedAssistanceRequest.getPayeeAccountID(),expectedAssistanceRequest.getAmount());
            verify(mockedBankAccountService, times(0)).openAccount(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getPayeeAccountID());
            verify(mockedUtilityService, times(1)).utilityPayment(expectedAssistanceRequest.getUserID(), expectedAssistanceRequest.getAmount(), expectedAssistanceRequest.getFunctionID(), expectedAssistanceRequest.getAccountID());


            System.out.println("UT_TC_005_002_002_003: CASE ISFINISHED = 1 -- PASSED");
        }
    }

    @DisplayName("UT_TC_005_003_001_001")
    @Test
    void UT_TC_005_003_001_001() {
        System.out.println("UT_TC_005_003_001_001: ADD");

        AssistanceServiceImpl assistanceService = new AssistanceServiceImpl();
        AssistanceRequestMapper mockedAssistanceMapper = mock(AssistanceRequestMapper.class);

        doNothing().when(mockedAssistanceMapper).addAssistanceRequest("assistanceID","userID", "accountID", "payeeAccountID", 1, 1.00, new Date(System.currentTimeMillis()), 1);

        assistanceService.setAssistanceRequestMapper(mockedAssistanceMapper);
        assertTrue(assistanceService.addAssistanceRequest("userID","accountID","payeeID",1,1.00,1));

        System.out.println("UT_TC_005_003_001_001: ADD -- PASSED");
    }

    @DisplayName("UT_TC_005_004_001_001")
    @Test
    void UT_TC_005_004_001_001() {
        System.out.println("UT_TC_005_004_001_001: NOT NULL LIST");

        //getAssistanceRequestByUserID  get list
        AssistanceServiceImpl assistanceService = new AssistanceServiceImpl();
        AssistanceRequestMapper mockedAssistanceRequestMapper = mock(AssistanceRequestMapper.class);
        List<AssistanceRequestDisp> list = emptyList();
        when(mockedAssistanceRequestMapper.getAssistanceRequestByUserID("user")).thenReturn(list);
        assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);
        assertEquals(list,assistanceService.getAssistanceRequestByUserID("user"));
        verify(mockedAssistanceRequestMapper).getAssistanceRequestByUserID("user");

        System.out.println("UT_TC_005_004_001_001: NOT NULL LIST -- PASSED");

    }

    @DisplayName("UT_TC_005_004_001_002")
    @Test
    void UT_TC_005_004_001_002() {
        System.out.println("UT_TC_005_004_001_002: NULL");

        //getAssistanceRequestByUserID  get null
        AssistanceServiceImpl assistanceService = new AssistanceServiceImpl();
        AssistanceRequestMapper mockedAssistanceRequestMapper = mock(AssistanceRequestMapper.class);

        when(mockedAssistanceRequestMapper.getAssistanceRequestByUserID("user")).thenReturn(null);
        assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);
        assertNull(assistanceService.getAssistanceRequestByUserID("user"));
        verify(mockedAssistanceRequestMapper).getAssistanceRequestByUserID("user");
        System.out.println("UT_TC_005_004_001_002: NULL -- PASSED");

    }

    @DisplayName("UT_TC_005_005_001_001")
    @Test
    void UT_TC_005_005_001_001() {
        System.out.println("UT_TC_005_005_001_001: NOT NULL LIST");

        //getUnfinishedAssistanceRequestByUserID  get list
        AssistanceServiceImpl assistanceService = new AssistanceServiceImpl();
        AssistanceRequestMapper mockedAssistanceRequestMapper = mock(AssistanceRequestMapper.class);
        List<AssistanceRequestDisp> list = emptyList();
        when(mockedAssistanceRequestMapper.getUnfinishedAssistanceRequestByUserID("user")).thenReturn(list);
        assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);
        assertEquals(list, assistanceService.getUnfinishedAssistanceRequestByUserID("user"));
        verify(mockedAssistanceRequestMapper).getUnfinishedAssistanceRequestByUserID("user");

        System.out.println("UT_TC_005_005_001_001: NOT NULL LIST -- PASSED");
    }

    @DisplayName("UT_TC_005_005_001_002")
    @Test
    void UT_TC_005_005_001_002() {
        System.out.println("UT_TC_005_005_001_002: NULL");

        //getUnfinishedAssistanceRequestByUserID  get null
        AssistanceServiceImpl assistanceService = new AssistanceServiceImpl();
        AssistanceRequestMapper mockedAssistanceRequestMapper = mock(AssistanceRequestMapper.class);

        when(mockedAssistanceRequestMapper.getUnfinishedAssistanceRequestByUserID("user")).thenReturn(null);
        assistanceService.setAssistanceRequestMapper(mockedAssistanceRequestMapper);
        assertNull(assistanceService.getUnfinishedAssistanceRequestByUserID("user"));
        verify(mockedAssistanceRequestMapper).getUnfinishedAssistanceRequestByUserID("user");

        System.out.println("UT_TC_005_005_001_002: NULL -- PASSED");

    }


}