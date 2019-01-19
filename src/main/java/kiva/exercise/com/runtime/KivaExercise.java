package kiva.exercise.com.runtime;

import kiva.exercise.com.pojo.LenderPayment;
import kiva.exercise.com.pojo.Payment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan(basePackages = "kiva.exercise.com.pojo")
@ComponentScan({"kiva.exercise.com"})
public class KivaExercise {
    public static void main(String[] args) {
        SpringApplication.run(KivaExercise.class, args);
    }

}
