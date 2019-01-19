package kiva.exercise.com.service;

import kiva.exercise.com.pojo.LenderPayment;
import kiva.exercise.com.pojo.Payment;

import java.util.List;

public interface ITransactionalHelper {
    /**
     *
     * This function is is used to perform a transactional operation on adding a payment and updating
     * respective LenderPayment.
     *
     * @param lenderPayment The Lender Payment associated with the payment that needs to be updated
     * @param payment The payment that needs to be persisted in the DB.
     *
     */
    void addPaymentUpdateLenderPayment(final LenderPayment lenderPayment,final Payment payment);

    /**
     *
     * This function is used to add multiple Lender Payment.
     *
     * @param lenderList This is the list of Lender Payment that needs to be persisted to the DB.
     */
    void addMultipleLenderPayments(final List<LenderPayment> lenderList);
}
