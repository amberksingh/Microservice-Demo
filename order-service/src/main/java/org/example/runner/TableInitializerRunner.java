package org.example.runner;

import org.example.config.SqlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.example.config.Constants.*;

//@Configuration
//public class TableInitializerConfig {
//
//    //✅ What this does:
//    //
//    //Runs automatically as soon as the DataSource is ready,
//    //before Hibernate starts schema validation.
//    //
//    //Executes your SQL file once.
//    //
//    //Keeps ddl-auto: validate fully functional (Hibernate just checks afterward).
//
//    public static final String CREATE_TABLE_QUERY_FILE = "order_service_create_table.sql";
//
//    @Bean
//    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
//        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(new ClassPathResource("sql/" + CREATE_TABLE_QUERY_FILE));
//        populator.setContinueOnError(true); // if table already exists, continue
//
//        DataSourceInitializer initializer = new DataSourceInitializer();
//        initializer.setDataSource(dataSource);
//        initializer.setDatabasePopulator(populator);
//        initializer.setEnabled(true);
//
//        System.out.println("✅ Ensuring orders table before Hibernate validation...");
//        return initializer;
//    }

@Component
@Order(1)
public class TableInitializerRunner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    SqlLoader sqlLoader;

    @Override
    public void run(String... args) throws Exception {
//        System.out.println("🔹 Running table creation before Hibernate startup...TableInitializerRunner");
//        jdbcTemplate.execute("""
//                    CREATE TABLE IF NOT EXISTS orders (
//                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
//                        order_number VARCHAR(50) NOT NULL,
//                        product_name VARCHAR(100),
//                        amount DOUBLE,
//                        status VARCHAR(20),
//                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
//                    );
//                """);
//        System.out.println("✅ Orders table ensured before JPA validation.");

        //
        // ✅ Check if the table exists first
//        String existsQuery = """
//                SELECT COUNT(*) FROM information_schema.tables
//                WHERE table_schema = DATABASE() AND table_name = ?;
//                """;

        System.out.println("🔹 Ensuring orders table...");
        jdbcTemplate.execute("USE ecommerce;");

        String existsQuery = sqlLoader.loadSql(EXISTS_TABLE_QUERY_FILE);
        try {
            Integer count = jdbcTemplate.queryForObject(existsQuery, Integer.class);
            if (count != null && count > 0) {
                System.out.printf("Table '%s' already exists — skipping creation.%n", ORDER_TABLE_NAME);
                return;
            }
        } catch (DataAccessException e) {
            System.err.println("Could not check if table exists: " + e.getMessage());
        }

        //ClassPathResource resource = new ClassPathResource("sql/" + CREATE_TABLE_QUERY_FILE);
        String createTableQuery = sqlLoader.loadSql(CREATE_TABLE_QUERY_FILE);
//        try (InputStream in = resource.getInputStream()) {
//            createTableQuery = new String(in.readAllBytes(), StandardCharsets.UTF_8);
//        }
        System.out.println("creating orders table as it doesn't exits");
        System.out.println("createTableQuery = " + createTableQuery);
        jdbcTemplate.execute(createTableQuery);
        System.out.println("Orders table created ");
    }
}



