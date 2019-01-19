package kiva.exercise.com.service;

import com.github.benmanes.caffeine.cache.LoadingCache;
import kiva.exercise.com.dao.ILenderPaymentDao;
import kiva.exercise.com.dao.IPaymentDao;
import kiva.exercise.com.dao.impl.LenderPaymentDaoImpl;
import kiva.exercise.com.pojo.LenderPayment;
import kiva.exercise.com.pojo.Payment;
import kiva.exercise.com.service.impl.LoanServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl unit;
    @Mock
    private ILenderPaymentDao mockLenderPaymentDao;
    @Mock
    private IPaymentDao mockPaymentDao;
    @Mock
    private Validator mockvalidator;
    @Mock
    private ITransactionalHelper mockTxHelper;
    @Mock
    private LoadingCache<String, Object> mockCache;

    private String loanId;
    private String lenderId;
    private Payment payment;
    private List<Payment> paymentList;
    private LenderPayment lenderPayment;
    private String loanResponse;
    private String lenderResponse;

    @Before
    public void setUp() {
        loanId = "loanId";
        lenderId = "lenderId";
        lenderPayment = new LenderPayment.Builder()
                .setLenderId(lenderId).setAmount(25.00)
                .setLoanId(loanId).build();
        lenderPayment.setInstallments(7);
        lenderPayment.setFirstinstallmentDate("01/10/2019");
        lenderPayment.setOutstandingAmount(25.00);
        lenderPayment.setOutstandingInstallments(7);
        paymentList = new ArrayList<>();
        payment = new Payment.Builder().setAmount(2.00)
                .setPaymentScheduleDate("12/12/2018").build();
        paymentList.add(payment);
        when(mockLenderPaymentDao.findLenderPaymentsByLoanIdAndLenderId(loanId, lenderId))
                .thenReturn(lenderPayment);
        loanResponse="{\"loans\":[{\"id\":1681633,\"name\":\"Grace\",\"description\":{\"languages\":[\"en\"],\"texts\":{\"en\":\"Grace is a married woman. She has four children. She describes herself to be industrious. She operates a farm where she sells milk from her dairy farm. She has been involved in this business for 10 years. Her business is located in a good area and her primary customers are locals. She describes her biggest business challenge to be inadequate working capital. <br /><br />She will use the KES 11,000 loan to buy another dairy cow. Her business goal is to have a bigger dairy farm within five years. She hopes that in the future, she will be financially stable to provide for her family's financial needs. This is her second loan with SMEP Microfinance Bank. The previous loan was repaid successfully. She will use the anticipated profits to educate her children.\"}},\"status\":\"funded\",\"funded_amount\":125,\"image\":{\"id\":3045338,\"template_id\":1},\"activity\":\"Dairy\",\"sector\":\"Agriculture\",\"themes\":[\"Rural Exclusion\"],\"use\":\"to buy another dairy cow.\",\"location\":{\"country_code\":\"KE\",\"country\":\"Kenya\",\"town\":\"Kitale\",\"geo\":{\"level\":\"town\",\"pairs\":\"1.019089 35.002305\",\"type\":\"point\"}},\"partner_id\":138,\"posted_date\":\"2019-01-17T19:50:04Z\",\"planned_expiration_date\":\"2019-02-16T19:50:04Z\",\"loan_amount\":125,\"lender_count\":5,\"bonus_credit_eligibility\":false,\"tags\":[{\"name\":\"user_favorite\"},{\"name\":\"#Animals\"}],\"borrowers\":[{\"first_name\":\"Grace\",\"last_name\":\"\",\"gender\":\"F\",\"pictured\":true}],\"terms\":{\"disbursal_date\":\"2018-12-27T08:00:00Z\",\"disbursal_currency\":\"KES\",\"disbursal_amount\":11000,\"repayment_interval\":\"Monthly\",\"repayment_term\":8,\"loan_amount\":125,\"local_payments\":[],\"scheduled_payments\":[],\"loss_liability\":{\"nonpayment\":\"lender\",\"currency_exchange\":\"shared\",\"currency_exchange_coverage_rate\":0.1}},\"payments\":[],\"funded_date\":\"2019-01-17T21:53:54Z\",\"journal_totals\":{\"entries\":0,\"bulkEntries\":0},\"translator\":{\"byline\":\"Lauri Fried-Lee\",\"image\":2210835}}]}";
        lenderResponse="{\"paging\":{\"page\":1,\"total\":5,\"page_size\":50,\"pages\":1},\"lenders\":[{\"lender_id\":\"timothy7934\",\"name\":\"Timothy\",\"image\":{\"id\":726677,\"template_id\":1},\"whereabouts\":\"\",\"uid\":\"timothy7934\"},{\"lender_id\":\"elizabeth6595\",\"name\":\"Elizabeth\",\"image\":{\"id\":1748433,\"template_id\":1},\"whereabouts\":\"\",\"uid\":\"elizabeth6595\"},{\"lender_id\":\"david1820\",\"name\":\"David\",\"image\":{\"id\":947478,\"template_id\":1},\"whereabouts\":\"Morphett Vale SA\",\"country_code\":\"AU\",\"uid\":\"david1820\"},{\"lender_id\":\"andrea9724\",\"name\":\"Andrea\",\"image\":{\"id\":726677,\"template_id\":1},\"whereabouts\":\"\",\"uid\":\"andrea9724\"},{\"lender_id\":\"claudio9326\",\"name\":\"Claudio Screpanti\",\"image\":{\"id\":839844,\"template_id\":1},\"whereabouts\":\"Basel\",\"country_code\":\"CH\",\"uid\":\"claudio9326\"}]}";
    }

    @Test
    public void testLoanDesperse() throws Exception {
        Integer totalLoaners = 4;
        double totalAmount = 100;
        ;
        List<Double> returnValue = unit.calculateLoanDisperseAmount(totalAmount, totalLoaners);
        assertEquals(returnValue.size(), totalLoaners, 0);
        checkTotalAmountEquals(returnValue, totalAmount);

        totalAmount = 25;
        totalLoaners = 7;
        returnValue = unit.calculateLoanDisperseAmount(totalAmount, totalLoaners);
        assertEquals(returnValue.size(), totalLoaners, 0);
        checkTotalAmountEquals(returnValue, totalAmount);


        totalAmount = 28;
        totalLoaners = 8;
        returnValue = unit.calculateLoanDisperseAmount(totalAmount, totalLoaners);
        assertEquals(returnValue.size(), totalLoaners, 0);
        checkTotalAmountEquals(returnValue, totalAmount);


    }

    private void checkTotalAmountEquals(List<Double> list, double totalAmount) {
        double total = 0;
        for (Double eachVal : list) {
            total += eachVal;
        }
        assertEquals(totalAmount, total, 0);
    }

    @Test
    public void testGetSchedule() throws Exception {

        CompletableFuture<List<Payment>> cf = unit.getSchedule(loanId, lenderId);
        assertTrue(cf.isDone());
        assertEquals(cf.get().size(), 7);
    }

    @Test
    public void testGetPayment() {
        when(mockPaymentDao.getByLenderPaymentId(anyString())).thenReturn(paymentList);
        CompletableFuture<List<Payment>> cf = unit.getPayment(loanId, lenderId);
        assertTrue(cf.isDone());
        assertEquals(1, paymentList.size());
    }

    @Test
    public void testMakePayment() throws Exception {
        when(mockvalidator.validate(any(LenderPayment.class))).thenReturn(new HashSet<>());
        when(mockvalidator.validate(any(Payment.class))).thenReturn(new HashSet<>());
        doNothing().when(mockTxHelper).addPaymentUpdateLenderPayment(any(LenderPayment.class), any(Payment.class));
        doNothing().when(mockCache).invalidate(anyString());
        CompletableFuture<LenderPayment> cf = unit.makePayment(loanId, lenderId, lenderPayment);
        assertTrue(cf.isDone());
        assertTrue(0 == cf.get().getOutstandingAmount());
        assertTrue(6 == cf.get().getOutstandingInstallments());

    }

    @Test
    public void testCalculateLoanRepayments() throws  Exception{
        doNothing().when(mockTxHelper).addMultipleLenderPayments(any());
        doNothing().when(mockCache).put(anyString(),any());
        CompletableFuture<List<LenderPayment>> cf=unit.calculateLoanRepayments(loanResponse,lenderResponse,loanId,lenderPayment);
        assertTrue(cf.isDone());
        assertTrue(5==cf.get().size());
    }


}
