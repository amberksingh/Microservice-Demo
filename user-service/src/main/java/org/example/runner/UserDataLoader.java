//package org.example.runner;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.example.UserJdbcRepo;
//import org.example.config.SqlLoader;
//import org.example.entity.User;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//
//import static org.example.config.Constants.*;
//
//@Component
//@Slf4j
//public class UserDataLoader implements CommandLineRunner {
//
//
//    private final JdbcTemplate jdbcTemplate;
//    private final SqlLoader sqlLoader;
//    //private final ObjectMapper mapper = new ObjectMapper();
//
//    public UserDataLoader(UserJdbcRepo repo, JdbcTemplate jdbcTemplate, SqlLoader sqlLoader) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.sqlLoader = sqlLoader;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        log.info("Ensuring USERS table...");
//        //jdbcTemplate.execute("USE ecommerce_user;");
////        if (repo.count() > 0) {
////            log.info("Users table not empty, skipping JSON load.");
////            return;
////        }
//
//        String existsQuery = sqlLoader.loadSql(EXISTS_TABLE_QUERY_FILE);
//        try {
//            Integer count = jdbcTemplate.queryForObject(existsQuery, Integer.class);
//            if (count != null && count > 0) {
//                System.out.printf("Table '%s' already exists — skipping creation.%n", USERS_TABLE_NAME);
//                return;
//            }
//        } catch (DataAccessException e) {
//            System.err.println("Could not check if table exists: " + e.getMessage());
//        }
//
//        //log.info("Users table empty → loading initial user data…");
//
////        ClassPathResource resource = new ClassPathResource("users.json");
////        try (InputStream in = resource.getInputStream()) {
////            List<User> users = mapper.readValue(in, new TypeReference<List<User>>() {
////            });
////            repo.saveAll(users);
////            log.info("Inserted {} mock users.", users.size());
////            return;
////        } catch (IOException | IllegalArgumentException e) {
////            log.warn("Unable to load user data into db");
////            log.warn("exception : ", e);
////        }
//
//        String createTableQuery = sqlLoader.loadSql(CREATE_TABLE_QUERY_FILE);
//        log.warn("creating users table as it doesn't exists");
//        log.info("createTableQuery = {} ", createTableQuery);
//        jdbcTemplate.execute(createTableQuery);
//        log.info("users table created ");
//
//
////        InputStream is = UserDataLoader.class.getResourceAsStream("users.json");
////        if (is == null) {
////            log.error("users.json not found!");
////            return;
////        }
////
////        List<User> users = mapper.readValue(is, new TypeReference<List<User>>() {});
////        repo.saveAll(users);
//
//
//    }
//}
