package org.example.forelmaniafishmag.tildawebhook.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients", uniqueConstraints = @UniqueConstraint(columnNames = {"phone"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "phone", nullable = false, unique = true, length = 12)
    private String phone;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public ClientsModel(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
