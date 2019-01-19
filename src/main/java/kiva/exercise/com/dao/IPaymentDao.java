package kiva.exercise.com.dao;

import kiva.exercise.com.pojo.Payment;

import java.util.List;

public interface IPaymentDao {
    /**
     *
     * This function is used to persist the Payment Object to the DB
     *
     * @param payment The Payment Object to be persisted in the DB.
     *
     */
    void addPayment(final Payment payment);

    /**
     *
     * This function is used to retrive all the persisted payment form the DB based on landerPaymentId
     *
     * @param lenderPaymentId This is the lender payment id form the Lender Payment table.
     * @return List of Payments associated with the lenderPaymentId
     *
     */
    List<Payment> getByLenderPaymentId(final String lenderPaymentId);
}
