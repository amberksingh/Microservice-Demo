//package org.example.runner;
//
//import org.example.config.SqlLoader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import static org.example.config.Constants.*;
//
//@Component
//@Order(1)
//public class TableInitializerRunner implements CommandLineRunner {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    SqlLoader sqlLoader;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        System.out.println("🔹 Ensuring payments table...");
//        jdbcTemplate.execute("USE ecommerce;");
//
//        String existsQuery = sqlLoader.loadSql(EXISTS_TABLE_QUERY_FILE);
//        try {
//            Integer count = jdbcTemplate.queryForObject(existsQuery, Integer.class);
//            if (count != null && count > 0) {
//                System.out.printf("Table '%s' already exists — skipping creation.%n", PAYMENT_TABLE_NAME);
//                return;
//            }
//        } catch (DataAccessException e) {
//            System.err.println("Could not check if table exists: " + e.getMessage());
//        }
//
//        System.out.println("creating payments table as it doesn't exits");
//        String createTableQuery = sqlLoader.loadSql(CREATE_TABLE_QUERY_FILE);
//        System.out.println("createTableQuery = " + createTableQuery);
//        jdbcTemplate.execute(createTableQuery);
//        System.out.println("payments table created ");
//    }
//}
