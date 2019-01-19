package kiva.exercise.com.config;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import kiva.exercise.com.pojo.LenderPayment;
import kiva.exercise.com.pojo.Payment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class Config {

    @Value("${db.url}")
    private String dbURL;
    @Value("${db.user}")
    private String dbUser;
    @Value("${db.password}")
    private String dbPassword;



    @Bean
    public RestTemplate gerRestTemplate() {
        return new RestTemplate();
    }


    @Bean(name = "dbPool")
    public DataSource getDbPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setAutoCommit(true);
        hikariConfig.setJdbcUrl(dbURL);
        hikariConfig.setUsername(dbUser);
        hikariConfig.setPassword(dbPassword);
        return new HikariDataSource(hikariConfig);

    }

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean getSessionFactory(@Qualifier("dbPool") DataSource dataSource) {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        Properties props = new Properties();
        props.put("hibernate.show_sql", true);
        props.put("hibernate.hbm2ddl.auto", "validate");
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(props);
        factoryBean.setAnnotatedClasses(LenderPayment.class, Payment.class);
        return factoryBean;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager(
            @Qualifier("sessionFactory") LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }




    @Bean(name = "validator")
    public Validator getValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    @Bean(name = "cache")
    public LoadingCache<String,Object> getCache() {
        LoadingCache<String, Object> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(k -> k);
        return cache;
    }

}
