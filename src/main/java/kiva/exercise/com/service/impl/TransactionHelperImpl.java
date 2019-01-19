package kiva.exercise.com.service.impl;

import kiva.exercise.com.dao.ILenderPaymentDao;
import kiva.exercise.com.dao.IPaymentDao;
import kiva.exercise.com.pojo.LenderPayment;
import kiva.exercise.com.pojo.Payment;
import kiva.exercise.com.service.ITransactionalHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TransactionHelperImpl implements ITransactionalHelper {
    @Autowired
    ILenderPaymentDao lenderPaymentDao;

    @Autowired
    IPaymentDao paymentDao;

    @Override
    @Transactional
    public void addPaymentUpdateLenderPayment(LenderPayment lenderPayment, Payment payment) {
        paymentDao.addPayment(payment);
        lenderPaymentDao.updateLenderPayment(lenderPayment);
    }

    @Override
    @Transactional
    public void addMultipleLenderPayments(List<LenderPayment> lenderList) {
        for(LenderPayment lenderPayment:lenderList)
        lenderPaymentDao.addLenderPayment(lenderPayment);

    }


}
