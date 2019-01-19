package kiva.exercise.com.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CompositeKey implements Serializable {

    @NotNull
    @Column(name = "loanId")
    private String loanId;

    @NotNull
    @Column(name = "lenderId")
    private String lenderId;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLenderId() {
        return lenderId;
    }

    public void setLenderId(String lenderId) {
        this.lenderId = lenderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKey that = (CompositeKey) o;
        return  Objects.equals(loanId, that.loanId) &&
                Objects.equals(lenderId, that.lenderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, lenderId);
    }
}
