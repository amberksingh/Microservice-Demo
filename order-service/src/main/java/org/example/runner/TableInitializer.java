//package org.example.runner;
//
//import jakarta.annotation.PostConstruct;
////import org.example.config.SqlLoader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//
//@Configuration
//public class TableInitializer {
//
//    public static final String CREATE_TABLE_QUERY_FILE = "order_service_create_table.sql";
//
////    @Autowired
////    OrderJdbcRepo jdbcRepo;
//
////    @Autowired
////    private SqlLoader sqlLoader;
//
////    @Override
////    public void run(String... args) throws Exception {
////        repo.createTable();
////        System.out.println("✅ Orders table ensured on startup");
////
////    }
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
////    @Autowired
////    private SqlLoader sqlLoader;
//
//    @PostConstruct
//    public void ensureTables() throws IOException {
//        System.out.println("Inside @PostConstruct : "+getClass().getName());
////        String sql = sqlLoader.loadSql("order_service_create_table.sql");
////        System.out.println("🔹 Executing table creation SQL before JPA validation...");
////        jdbcTemplate.execute(sql);
////        System.out.println("✅ Orders table ensured before Hibernate validation");
//        ClassPathResource resource = new ClassPathResource("sql/" + CREATE_TABLE_QUERY_FILE);
////        String query = Files.readString(resource.getFile().toPath());
////        System.out.println("query = " + query);
//
//        String query;
//        try (InputStream in = resource.getInputStream()) {
//            query = new String(in.readAllBytes(), StandardCharsets.UTF_8);
//        }
//        System.out.println("query = " + query);
//        jdbcTemplate.execute(query);
//        System.out.println("✅ Orders table ensured before Hibernate validation");
//    }
//}
//
