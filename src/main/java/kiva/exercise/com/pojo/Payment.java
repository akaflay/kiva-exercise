package kiva.exercise.com.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Entity
@Table(name = "payment")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(NON_NULL)
public class Payment {

    @Column(name="paymentId")
    @Id
    @NotNull
    private String paymentId;

    @Column(name = "lenderPaymentId")
    @NotNull
    private String lenderPaymentId;

    @Column(name = "amount")
    @NotNull
    @Min(0)
    private Double amount;

    @Column(name = "createdEpoc")
    private Long createdEpoc;

    @Transient
    private String paymentScheduledDate;

    public String getLenderPaymentId() {
        return lenderPaymentId;
    }

    public void setLenderPaymentId(String lenderPaymentId) {
        this.lenderPaymentId = lenderPaymentId;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public Long getCreatedEpoc() {
        return createdEpoc;
    }

    public void setCreatedEpoc(Long createdEpoc) {
        this.createdEpoc = createdEpoc;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentScheduledDate() {
        return paymentScheduledDate;
    }

    public void setPaymentScheduledDate(String paymentScheduledDate) {
        this.paymentScheduledDate = paymentScheduledDate;
    }

    public static class Builder{

        private String lenderPaymentId;
        private Double amount;
        private String paymentScheduleDate;

        public Builder Builder(){
            return this;
        }

        public Builder setAmount(Double amount){
            this.amount=amount;
            return  this;
        }

        public Builder setLenderPaymentId(String lenderPaymentId) {
            this.lenderPaymentId=lenderPaymentId;
            return this;
        }
        public Builder setPaymentScheduleDate(String paymentScheduleDate){
            this.paymentScheduleDate= paymentScheduleDate;
            return this;
        }

        public Payment build(){
            Payment object=new Payment();

            object.setLenderPaymentId(this.lenderPaymentId);
            object.setAmount(amount);

            object.setCreatedEpoc(Instant.now().getEpochSecond());
            object.setPaymentId( UUID.randomUUID().toString());
            object.setPaymentScheduledDate(this.paymentScheduleDate);
            return  object;

        }
    }
}
