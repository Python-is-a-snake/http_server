package edu.http_server.app.model.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private int age;
    private String email;

    public User(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}
