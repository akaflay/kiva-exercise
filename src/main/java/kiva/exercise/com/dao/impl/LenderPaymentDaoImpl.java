package kiva.exercise.com.dao.impl;


import kiva.exercise.com.dao.ILenderPaymentDao;
import kiva.exercise.com.pojo.CompositeKey;
import kiva.exercise.com.pojo.LenderPayment;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class LenderPaymentDaoImpl extends BaseDao<LenderPayment> implements ILenderPaymentDao {

    @Override
    public void addLenderPayment(LenderPayment lenderPayment) {
        add(lenderPayment);

    }

    @Override
    public void updateLenderPayment(LenderPayment lenderPayment) {
        update(lenderPayment);
    }

    @Override
    @Transactional
    public LenderPayment findLenderPaymentsByLoanIdAndLenderId(String loanId, String lenderId) {
        CompositeKey key=new CompositeKey();
        key.setLoanId(loanId);
        key.setLenderId(lenderId);
        return findById(LenderPayment.class,key);
    }
}
