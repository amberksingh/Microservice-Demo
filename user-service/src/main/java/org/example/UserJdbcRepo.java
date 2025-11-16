package org.example;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJdbcRepo extends JpaRepository<User, Long> {
}
