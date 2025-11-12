package org.example.repo;

import org.example.config.SqlLoader;
import org.example.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class PaymentJdbcRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SqlLoader sqlLoader;

//    public void createTable() throws IOException {
//        String sql = sqlLoader.loadSql("payment_service_create_table.sql");
//        jdbcTemplate.execute(sql);
//        System.out.println("Payments table ensured on startup");
//    }

    public List<Payment> findAllPayments() {
        return jdbcTemplate.query("SELECT * FROM payments", new BeanPropertyRowMapper<>(Payment.class));
    }
}

