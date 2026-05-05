package com.todo.app.dto.request;

import com.todo.app.enums.TodoPriority;
import com.todo.app.enums.TodoStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private TodoStatus status;

    private TodoPriority priority;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;
}
