package kiva.exercise.com.service;

import kiva.exercise.com.pojo.LenderPayment;

import javax.ws.rs.container.AsyncResponse;

public interface ILoan {
	/**
	 *
	 * This function is used to get loan using KIVA APIS
	 *
	 * @param response The suspended asynchronous response
	 * @param loanId The id of the loan or null if we need to fetch all the loans
	 *
	 */
	void get(final AsyncResponse response, final String loanId);

	/**
	 * This function is used to get Lenders based on the loan id using KIVA APIS.
	 *
	 * @param response The suspended asynchronous response
	 * @param loanId The Loan Id to be used
	 *
	 */
	void getLenders(final AsyncResponse response, final String loanId);

	/**
	 *
	 * This function is used to calculate repayments for each lender.
	 *
	 * @param response The suspended asynchronous response.
	 * @param loanId The id of the loan to be used to calculate repayments.
	 * @param lenderPayment The Lender Payment object that contains the first instalment date
	 *                       and the number of  installments
	 */
	void calculateRepayments(final AsyncResponse response, final String loanId,final LenderPayment lenderPayment);

	/**
	 *
	 * This function is used to make payment against a particular loan for a lender.
	 *
	 * @param response The suspended asynchronous response.
	 * @param loanId The id of the loan
	 * @param lenderId The id of the lender
	 * @param lenderPayment The lender payment object that has the amount.
	 *
	 */
	void makePayment(final AsyncResponse response, final String loanId,final String lenderId,
					 final LenderPayment lenderPayment);

	/**
	 *
	 * This function is used to get all the payment made against a loan for a lender
	 *
	 * @param response The suspended asynchronous response.
	 * @param loanId The id of the loan
	 * @param lenderId The id of the lender
	 *
	 */
	void getPayments(final AsyncResponse response, final String loanId,final String lenderId);

	/**
	 *
	 * This function is used to get all the list of payments that has been scheduler for a lender
	 * of a particular loan
	 *
	 * @param response The suspended asynchronous response.
	 * @param loanId The id of the loan
	 * @param lenderId The id of the lender
	 *
	 */
	void getSchedule(final AsyncResponse response, final String loanId, final String lenderId);
}