package kiva.exercise.com.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.util.Builder;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "lenderpayment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LenderPayment {

    @NotNull
    @Column(name = "lenderPaymentId")
    private String lenderPaymentId;

    @EmbeddedId
    private CompositeKey id;

    @Column(name = "amount")
    @NotNull
    @Min(1)
    private Double amount;


    @Min(0)
    @Column(name = "outstandingAmount")
    private Double outstandingAmount;


    @Min(1)
    @Column(name = "installments")
    private Integer installments;


    @Min(0)
    @Column(name = "outstandingInstallments")
    private Integer outstandingInstallments;


    @NotNull
    @Column(name = "firstinstallmentDate")
    private String firstinstallmentDate;

    @Column(name = "createdEpoc")
    private Long createdEpoc;


    @Column(name = "updatedEpoc")
    private Long updatedEpoc;

    public String getLenderPaymentId() {
        return lenderPaymentId;
    }

    public void setLenderPaymentId(String lenderPaymentId) {
        this.lenderPaymentId = lenderPaymentId;
    }

    public CompositeKey getId() {
        return id;
    }

    public void setId(CompositeKey id) {
        this.id = id;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public Integer getOutstandingInstallments() {
        return outstandingInstallments;
    }

    public void setOutstandingInstallments(Integer outstandingInstallments) {
        this.outstandingInstallments = outstandingInstallments;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getFirstinstallmentDate() {
        return firstinstallmentDate;
    }

    public void setFirstinstallmentDate(String firstinstallmentDate) {
        this.firstinstallmentDate = firstinstallmentDate;
    }

    public Long getUpdatedEpoc() {
        return updatedEpoc;
    }

    public void setUpdatedEpoc(Long updatedEpoc) {
        this.updatedEpoc = updatedEpoc;
    }

    public Long getCreatedEpoc() {
        return createdEpoc;
    }

    public void setCreatedEpoc(Long createdEpoc) {
        this.createdEpoc = createdEpoc;
    }

    public static class Builder {

        private String loanId;
        private String lenderId;
        private Double amount;

        public Builder Builder() {
            return this;
        }

        public Builder setAmount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setLenderId(String lenderId) {
            this.lenderId = lenderId;
            return this;
        }

        public Builder setLoanId(String loanId) {
            this.loanId = loanId;
            return this;
        }

        public LenderPayment build() {
            LenderPayment object = new LenderPayment();
            CompositeKey ck = new CompositeKey();
            ck.setLoanId(this.loanId);
            ck.setLenderId(lenderId);
            object.setLenderPaymentId(UUID.randomUUID().toString());
            object.setAmount(amount);
            object.setId(ck);
            object.setCreatedEpoc(Instant.now().getEpochSecond());
            object.setUpdatedEpoc(Instant.now().getEpochSecond());
            return object;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LenderPayment that = (LenderPayment) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount);
    }
}
