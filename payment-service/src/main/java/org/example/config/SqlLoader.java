package org.example.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class SqlLoader {
    public String loadSql(String fileName) {
        String query = null;
        ClassPathResource resource = new ClassPathResource("sql/" + fileName);
        try (InputStream in = resource.getInputStream()) {
            query = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return query;
    }
}
