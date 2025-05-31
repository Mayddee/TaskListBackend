package org.example.tasklist.domain.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private long id;
    private String Title;
    private String description;
    private Status status;
    private LocalDateTime expirationDate;

}
