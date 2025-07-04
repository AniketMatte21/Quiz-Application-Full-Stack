package com.quiz.quiz_backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "UserTable")
public class UserEntity
{
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NonNull
    @Column(unique = true)
    private String username;

    @Setter
    @Column(unique = true)
    private String email;

    @Setter
    private String password;

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserEntity() {
    }

    public void setName(String username) {
        this.username = username;
    }


    @OneToMany(mappedBy = "userEntity" , cascade =CascadeType.ALL)
    @JsonManagedReference(value = "user_entity")
    private List<UserQuizPurchase> userQuizPurchaseList;

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @OneToMany(mappedBy = "userEntity" ,cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user_score")
    private List<ScoreStoredEntity> scoreStoredEntity;

}
