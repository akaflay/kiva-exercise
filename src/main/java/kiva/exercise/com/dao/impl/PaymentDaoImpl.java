package kiva.exercise.com.dao.impl;

import kiva.exercise.com.dao.IPaymentDao;
import kiva.exercise.com.pojo.Payment;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PaymentDaoImpl extends BaseDao<Payment> implements IPaymentDao {

    @Transactional
    @Override
    public void addPayment(Payment payment) {
    add(payment);
    }

    @Override
    @Transactional
    public List<Payment> getByLenderPaymentId(String lenderPaymentId) {
    return  findListBySecondryId(Payment.class,"lenderPaymentId",lenderPaymentId);
    }
}
