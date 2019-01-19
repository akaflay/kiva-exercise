package kiva.exercise.com.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.container.AsyncResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.benmanes.caffeine.cache.LoadingCache;
import kiva.exercise.com.constants.Constants;
import kiva.exercise.com.dao.ILenderPaymentDao;
import kiva.exercise.com.dao.IPaymentDao;
import kiva.exercise.com.pojo.LenderPayment;
import kiva.exercise.com.pojo.Payment;
import kiva.exercise.com.service.ILoan;
import kiva.exercise.com.service.IRestCallService;
import kiva.exercise.com.service.ITransactionalHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.CollectionUtils;

import static kiva.exercise.com.constants.Constants.*;
import static kiva.exercise.com.core.JsonUtils.fromJson;


@Service
public class LoanServiceImpl implements ILoan {

    @Autowired
    private IRestCallService restCall;

    @Autowired
    ILenderPaymentDao lenderPaymentDao;

    @Autowired
    IPaymentDao paymentDao;

    @Autowired
    Validator validator;

    @Autowired
    ITransactionalHelper txHelper;

    @Autowired
    LoadingCache<String,Object> cache;

    @Override
    public void get(final AsyncResponse response, final String id) {
        getLoan(id).thenApply(response::resume).exceptionally(response::resume);

    }

    @Override
    public void getLenders(final AsyncResponse response,final String id) {
        getLenders(id).thenApply(response::resume).exceptionally(response::resume);
    }

    @Override
    public void calculateRepayments(final AsyncResponse response,final String loanId,
                                    final LenderPayment lenderPayment) {
        if(cache.get(loanId) instanceof List){
            response.resume(cache.get(loanId));
            return;
        }
        CompletableFuture<String> loanResponse = getLoan(loanId);
        CompletableFuture<String> lenderResponse = getLenders(loanId);
        loanResponse.thenCombine(lenderResponse, (loanRes, lenderRes) -> calculateLoanRepayments(loanRes, lenderRes, loanId,lenderPayment))
                .thenCompose(result -> result)
                .thenApply(response::resume)
                .exceptionally(response::resume);
    }

    @Override
    public void makePayment(final AsyncResponse response,final String loanId,
                            final String lenderId,final LenderPayment lenderPayment) {
            makePayment(loanId, lenderId, lenderPayment)
                .thenApply(response::resume)
                .exceptionally(response::resume);

    }

    @Override
    public void getPayments(final AsyncResponse response,final String loanId,final String lenderId) {
        getPayment(loanId, lenderId)
                .thenApply(response::resume)
                .exceptionally(response::resume);
    }

    @Override
    public void getSchedule(final AsyncResponse response, final String loanId, final String lenderId) {
        getSchedule(loanId, lenderId)
                .thenApply(response::resume)
                .exceptionally(response::resume);

    }

    /**
     *
     * This function is used to get the payment scheduled for a particular loan and lender combination
     *
     * @param loanId The Id of the loam
     * @param lenderId The Id of the lender
     * @return List which contains payment schedule
     *
     */
    public CompletableFuture<List<Payment>> getSchedule(final String loanId, final String lenderId) {
        List<Payment> payments=new ArrayList<>();
        int installmentMonth=INTEGER_ZERO;
        LenderPayment dbLenderPayment = lenderPaymentDao.findLenderPaymentsByLoanIdAndLenderId(loanId, lenderId);
        if (dbLenderPayment != null && dbLenderPayment.getAmount() != DOUBLE_ZERO) {
            List<Double> amounts=calculateLoanDisperseAmount(dbLenderPayment.getAmount(),dbLenderPayment.getInstallments());
            for(Double amount:amounts){
                String scheduleDate=createScheduledDate(dbLenderPayment.getFirstinstallmentDate(),installmentMonth);
                installmentMonth++;
                Payment pay=new Payment.Builder().setAmount(getAmountPrecessionToTwoDecimal(amount)).setPaymentScheduleDate(
                        scheduleDate ).build();
                pay.setPaymentId(null);
                pay.setCreatedEpoc(null);
                payments.add(pay);

            }

        }
        return CompletableFuture.completedFuture(payments);
    }

    /**
     *
     * This function helps to generate the next payment schedule date based on the installation month
     * and the first Installation Date. Note we have not taken holiday into consideration.
     *
     * @param firstInstallmentDate This is the First Installment Date
     * @param installmentMonth This is the installation month like 1 for the second installment.
     * @return Return the date for the next payment schedule.
     *
     */
    private String createScheduledDate(final String firstInstallmentDate,final Integer installmentMonth){
        if(installmentMonth==INTEGER_ZERO) return firstInstallmentDate;
        String calculatedDate=firstInstallmentDate;
        try {
            SimpleDateFormat df=new SimpleDateFormat("MM/dd/yyyy");
            Date date = df.parse(firstInstallmentDate);
            Date newDate = DateUtils.addMonths(date, installmentMonth);
            calculatedDate=df.format(newDate);
        }catch (Exception e){
            //TODO Add Exception handeling
        }finally {
            return calculatedDate;
        }
    }

    /**
     *
     * This function will get the list of payment made for a combination of loan and lender
     *
     * @param loanId This is the loan id
     * @param lenderId This is the lender id
     * @return List of payment made by the lender for the loan
     *
     */
    public CompletableFuture<List<Payment>> getPayment(final String loanId, final String lenderId) {
        List<Payment> payments=new ArrayList<>();
        LenderPayment dbLenderPayment = lenderPaymentDao.findLenderPaymentsByLoanIdAndLenderId(loanId, lenderId);
        if (dbLenderPayment != null && dbLenderPayment.getAmount() != DOUBLE_ZERO) {
            payments=paymentDao.getByLenderPaymentId(dbLenderPayment.getLenderPaymentId());
        }
        return CompletableFuture.completedFuture(payments);
    }

    /**
     *
     * This function is used to make a payment against a particular loan and lender.
     *
     * @param loanId The id of the loan
     * @param lenderId The id of the lender
     * @param lenderPayment The LenderPayment object that contains the amount
     * @return This is the updated version of the LenderPayment.
     *
     */
    public CompletableFuture<LenderPayment> makePayment(final String loanId, final String lenderId,
                                                         final LenderPayment lenderPayment) {
        LenderPayment dbLenderPayment = lenderPaymentDao.findLenderPaymentsByLoanIdAndLenderId(loanId, lenderId);
        if (dbLenderPayment != null && dbLenderPayment.getAmount() != DOUBLE_ZERO) {
            dbLenderPayment.setUpdatedEpoc(Instant.now().getEpochSecond());
            Double amountToUpdate = getAmountPrecessionToTwoDecimal(dbLenderPayment.getOutstandingAmount() - lenderPayment.getAmount());
            Integer outStandingInstallments=dbLenderPayment.getOutstandingInstallments()-1;
            if (amountToUpdate >= DOUBLE_ZERO && outStandingInstallments>=INTEGER_ZERO) {
                dbLenderPayment.setOutstandingAmount(amountToUpdate);
                dbLenderPayment.setOutstandingInstallments(outStandingInstallments);
                Payment payment=new Payment.Builder().setLenderPaymentId(dbLenderPayment.getLenderPaymentId()).setAmount(lenderPayment.getAmount()).build();
                Set<ConstraintViolation<Payment>> paymentValidations=validator.validate(payment);
                if(!paymentValidations.isEmpty()) {
                    throw new RuntimeException("Please provide all the required fields such as amount");
                }
                Set<ConstraintViolation<LenderPayment>> lenderPaymentValidations=validator.validate(dbLenderPayment);
                if(!lenderPaymentValidations.isEmpty()) {
                    throw new RuntimeException("Please provide all the required fields such amount etc");
                }
                txHelper.addPaymentUpdateLenderPayment(dbLenderPayment,payment);
                cache.invalidate(loanId);
            }

        }
        return CompletableFuture.completedFuture(dbLenderPayment);
    }


    /**
     *
     * This function is used to get the lender for a particular loan
     *
     * @param lenderId This is the loan id
     * @return The response from the kiva api call
     *
     */
    private CompletableFuture<String> getLenders(final String lenderId) {
        final String url = String.format("/%s/%s/%s%s",
                LOAN_PATH, lenderId, "lenders",
                DEFAULT_JSON_PATH);
        return (CompletableFuture<String>) restCall.makeGetCall(url,String.class);
    }

    /**
     *
     * This function is used to make a rest api call to KIVA api to get loan based on the id.
     * If the loanId is null it will get all the loans
     *
     * @param loandId This is the loan id. If the id is null/empty it is going to return all the data.
     * @return The response from the kiva api call
     *
     */
    private CompletableFuture<String> getLoan(final String loandId) {
        final String url = String.format("/%s/%s%s",
                LOAN_PATH, StringUtils.isBlank(loandId) ? DEFAULT_PATH : loandId,
                DEFAULT_JSON_PATH, "?status=funded");
        return (CompletableFuture<String>)restCall.makeGetCall(url,String.class);
    }

    /**
     *
     * This function is used to distibute the amount between different parts.
     * This is used to calculate loan for each Loaner as well as each repayment amount.
     *
     * @param totalAmount This is the total amount either the total loan amount or the
     *                    total amount the loaner has to pay
     * @param parts       This is either the number of loaners or the number of months for repayment.
     * @return List of Amount. Either the amount each Loaner has to pay
     * OR the amount each loaner has to pay per installment.
     *
     */
    public List<Double> calculateLoanDisperseAmount(final Double totalAmount, final Integer parts) {
        List<Double> amountList = new ArrayList<>();
        try {
            Double finalValue = getAmountPrecessionToTwoDecimal((Double) totalAmount / parts);
            Double total = new Double(INTEGER_ZERO);
            for (int i = 1; i < parts; i++) {
                total += finalValue;
                amountList.add(finalValue);
            }
            amountList.add((double) totalAmount - total);

        } catch (Exception e) {
            //TODO handle exception

        } finally {
            return amountList;
        }
    }

    /**
     *
     * This function is used to get the value of amount with two decimal precession.
     *
     * @param amount This is the amount such as 3.5723423423432
     * @return Double that is with two decimal precession
     *
     */

    private Double getAmountPrecessionToTwoDecimal(final Double amount){
        Double finalValue=DOUBLE_ZERO;
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            String formate = df.format((Double) amount);
            finalValue = df.parse(formate).doubleValue();

        }catch (Exception e){
            System.out.println("Error when calculating the Dispersion amount");
        }finally {
            return finalValue;
        }

    }


    /**
     *
     * This function is used to calculate the loan amount for each lender.
     *
     * @param loanResponse This is the response from kiva api for loan
     * @param lenderResponse This is the response from kiva api for lender
     * @param loanId This is the loan id.
     * @param lenderPayment This is the LenderPayment object that has the First Installment date and
     *                      the number of installments
     * @return List of LenderPayments after calculation.
     */

    public CompletableFuture<List<LenderPayment>> calculateLoanRepayments(
            final String loanResponse, final String lenderResponse,final String loanId,
            final LenderPayment lenderPayment) {

        final Double totalFundedAmount = getTotalFundedAmount(loanResponse);
        final List<LenderPayment> lenderList = getListOfLenders(lenderResponse, loanId);
        if (totalFundedAmount != DOUBLE_ZERO && !CollectionUtils.isEmpty(lenderList)) {
            List<Double> amountForEachLender = calculateLoanDisperseAmount(totalFundedAmount, lenderList.size());
            updateAmountForEachLender(amountForEachLender, lenderList,lenderPayment);
        }

        cache.put(loanId,lenderList);
        return CompletableFuture.completedFuture(lenderList);
    }

    /**
     *
     * This function is used to persist the LenderPayment information to the DB.
     *
     * @param amountForEachLender This is the List of amount for each Lender
     * @param lenderList This is the list of Lenders
     * @param lenderPayment This is the LenderPayment object that has the First Installment date and
     *                      the number of installments
     *
     */
    public void updateAmountForEachLender(final List<Double> amountForEachLender,
                                          final List<LenderPayment> lenderList,
                                          final LenderPayment lenderPayment) {
        Integer index = INTEGER_ZERO;
        Boolean lenderPresentInDB=true;
        for (LenderPayment eachLenderPayment : lenderList) {
            eachLenderPayment.setAmount(amountForEachLender.get(index));
            eachLenderPayment.setOutstandingAmount(amountForEachLender.get(index));
            eachLenderPayment.setInstallments(lenderPayment.getInstallments());
            eachLenderPayment.setOutstandingInstallments(lenderPayment.getInstallments());
            eachLenderPayment.setFirstinstallmentDate(lenderPayment.getFirstinstallmentDate());
            LenderPayment dbLenderPayment= lenderPaymentDao.findLenderPaymentsByLoanIdAndLenderId(eachLenderPayment.getId().getLoanId(),
                    eachLenderPayment.getId().getLenderId());
            if (dbLenderPayment == null){
                lenderPresentInDB=false;
                Set<ConstraintViolation<LenderPayment>> validations=validator.validate(eachLenderPayment);
                if(!validations.isEmpty()) {
                    throw new RuntimeException("Please provide all the required fields such as installments and firstinstallmentDate");
                }
            }else{
                eachLenderPayment.setLenderPaymentId(dbLenderPayment.getLenderPaymentId());
                eachLenderPayment.setOutstandingAmount(dbLenderPayment.getOutstandingAmount());
                eachLenderPayment.setOutstandingInstallments(dbLenderPayment.getOutstandingInstallments());
            }


        }
        if(!lenderPresentInDB){
            txHelper.addMultipleLenderPayments(lenderList);
        }
    }

    /**
     *
     * This function is used to get lenders based on the response form KIVA API.
     *
     * @param lenderResponse This is the response from kiva api for Lender
     * @param loanId The loan Id
     * @return List of LenderPayments
     *
     */
    private List<LenderPayment> getListOfLenders(final String lenderResponse, final String loanId) {
        final List<LenderPayment> lenderList = new ArrayList<>();
        final ObjectNode lenderJsonObj = fromJson(lenderResponse,
                ObjectNode.class);
        if (lenderJsonObj.hasNonNull(LENDERS_PATH)) {
            for (JsonNode lender : lenderJsonObj.get(LENDERS_PATH)) {
                String lenderId = lender.hasNonNull(LENDER_ID_PATH) ? lender.get(LENDER_ID_PATH).asText() : ANNONYMUS;
                lenderList.add(new LenderPayment.Builder().setLenderId(lenderId).setLoanId(loanId).build());
            }
        }
        return lenderList;

    }

    /**
     *
     * This function is used to extract the loan amount for a particular loan
     *
     * @param loanResponse This is the response from kiva api for loan
     * @return Loan amount of the loan.
     *
     */
    private Double getTotalFundedAmount(final String loanResponse) {
        Double fundedAmount = DOUBLE_ZERO;
        final ObjectNode loanJsonObj = fromJson(loanResponse,
                ObjectNode.class);
        if (loanJsonObj.hasNonNull(LOAN_PATH)) {
            ObjectNode eachLoan = fromJson(loanJsonObj.get(LOAN_PATH).get(INTEGER_ZERO).toString(), ObjectNode.class);

            if (eachLoan.hasNonNull(FUNDED_AMOUNT_PATH)) {
                fundedAmount = eachLoan.get(FUNDED_AMOUNT_PATH).asDouble();
            }
        }
        return fundedAmount;

    }


}
