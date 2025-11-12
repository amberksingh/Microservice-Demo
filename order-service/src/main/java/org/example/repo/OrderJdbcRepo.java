package org.example.repo;

//import org.example.config.SqlLoader;
import org.example.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class OrderJdbcRepo {

    public static final String SELECT_ALL_ROWS_QUERY = "SELECT * FROM orders";
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Order> findOrdersRaw() {
        return jdbcTemplate.query(SELECT_ALL_ROWS_QUERY, new BeanPropertyRowMapper<>(Order.class));
    }
}

