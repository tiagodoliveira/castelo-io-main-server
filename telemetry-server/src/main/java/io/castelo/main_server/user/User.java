package io.castelo.main_server.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Entity
@Table(name = "User")
public record User(
        @Id UUID userId,
        String userName
) {
}
