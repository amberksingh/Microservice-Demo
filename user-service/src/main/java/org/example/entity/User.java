package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //@Column(unique = true)
    private String email;

    private Double balance;

    private String phone;
    private String status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Automatically fill before persist if null
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            System.out.println("createdAt is null. Setting in code");
            this.createdAt = LocalDateTime.now();
        }
    }
}
