package edu.http_server.app.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private boolean isDone;
    private User user;
}
