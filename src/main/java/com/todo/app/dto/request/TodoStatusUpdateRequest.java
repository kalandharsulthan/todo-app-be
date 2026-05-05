package com.todo.app.dto.request;

import com.todo.app.enums.TodoStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private TodoStatus status;
}
