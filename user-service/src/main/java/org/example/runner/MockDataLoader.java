package org.example.runner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.UserJdbcRepo;
import org.example.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MockDataLoader implements CommandLineRunner {

    private final ObjectMapper mapper;
    private final UserJdbcRepo jdbcRepo;

    public MockDataLoader(ObjectMapper mapper, UserJdbcRepo jdbcRepo) {
        this.mapper = mapper;
        this.jdbcRepo = jdbcRepo;
    }

//    public List<User> loadMockUsers() {
//        List<User> users = new ArrayList<>();
//        ClassPathResource resource = new ClassPathResource("users.json");
//        try (InputStream in = resource.getInputStream()) {
//            users = mapper.readValue(in, new TypeReference<List<User>>(){});
//            //repo.saveAll(users);
//            log.info("Inserted {} mock users.", users.size());
//            return users;
//        } catch (IOException | IllegalArgumentException e) {
//            log.warn("Unable to load user data into db");
//            log.warn("exception : ", e);
//        }
//        log.warn("returning empty users list to load");
//        return users;
//    }


    @Override
    public void run(String... args) throws Exception {

        long count = jdbcRepo.count();
        if (count != 0) {
            log.info("user data present already. skipping mock data addition");
            return;
        }
        log.warn("Users table empty → loading initial user data…");
        List<User> users;
        ClassPathResource resource = new ClassPathResource("users.json");
        try (InputStream in = resource.getInputStream()) {
            users = mapper.readValue(in, new TypeReference<List<User>>(){});
            jdbcRepo.saveAll(users);
            log.info("Inserted {} mock users.", users.size());
            //return users;
        } catch (IOException | IllegalArgumentException e) {
            log.warn("Unable to load user data into db");
            log.warn("exception : ", e);
        }
        //log.warn("returning empty users list to load");
        //return users;

    }
}
