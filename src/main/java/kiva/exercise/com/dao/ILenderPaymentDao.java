package kiva.exercise.com.dao;


import kiva.exercise.com.pojo.LenderPayment;

import java.util.List;

public interface ILenderPaymentDao {

    /**
     *
     * This function is used to find the Lender Payment Object from DB based on the
     * Loan id and the Lender id.
     *
     * @param loanId This is the Loan id.
     * @param lenderId This is the Lender id
     * @return Lender Payment object in the DB
     *
     */
    LenderPayment findLenderPaymentsByLoanIdAndLenderId(String loanId, String lenderId);

    /**
     *
     * This function is used to persist the Lender Payment Object to the DB
     *
     * @param lenderPayment The Lender Payment Object to be persisted in the DB.
     *
     */
    void addLenderPayment(LenderPayment lenderPayment);

    /**
     *
     * This function is used to update the Lender Payment Object to the DB
     *
     * @param lenderPayment The Lender Payment Object to be updated in the DB.
     */
    void updateLenderPayment(LenderPayment lenderPayment);



}
