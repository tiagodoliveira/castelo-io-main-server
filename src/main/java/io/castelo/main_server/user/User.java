package io.castelo.main_server.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

        @Id
        @NotNull
        @Column(name = "user_id")
        private UUID userId;

        @NotBlank
        @Column(name = "user_name", nullable = false, columnDefinition = "text")
        String userName;

        public User(@NotNull UUID userId, @NotBlank String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        public User() {

        }

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(@NotNull UUID userId) {
            this.userId = userId;
        }

        public @NotBlank String getUserName() {
            return userName;
        }

        public void setUserName(@NotBlank String userName) {
            this.userName = userName;
        }

}
