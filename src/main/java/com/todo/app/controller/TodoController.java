package com.todo.app.controller;

import com.todo.app.dto.request.TodoRequest;
import com.todo.app.dto.request.TodoStatusUpdateRequest;
import com.todo.app.dto.response.ApiResponse;
import com.todo.app.dto.response.PagedResponse;
import com.todo.app.dto.response.TodoResponse;
import com.todo.app.enums.TodoPriority;
import com.todo.app.enums.TodoStatus;
import com.todo.app.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todo API", description = "CRUD operations for Todo items")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    @Operation(summary = "Get all todos", description = "Paginated and filterable list of todos")
    public ResponseEntity<ApiResponse<PagedResponse<TodoResponse>>> getAllTodos(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by status") @RequestParam(required = false) TodoStatus status,
            @Parameter(description = "Filter by priority") @RequestParam(required = false) TodoPriority priority,
            @Parameter(description = "Sort field and direction, e.g. createdAt,desc") @RequestParam(required = false) String sort) {

        PagedResponse<TodoResponse> response = todoService.getAllTodos(page, size, status, priority, sort);
        return ResponseEntity.ok(ApiResponse.success("Todos retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a todo by ID")
    public ResponseEntity<ApiResponse<TodoResponse>> getTodoById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Todo retrieved successfully", todoService.getTodoById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new todo")
    public ResponseEntity<ApiResponse<TodoResponse>> createTodo(
            @Valid @RequestBody TodoRequest request) {
        TodoResponse created = todoService.createTodo(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Todo created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Fully update a todo")
    public ResponseEntity<ApiResponse<TodoResponse>> updateTodo(
            @PathVariable UUID id,
            @Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Todo updated successfully", todoService.updateTodo(id, request)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update todo status only")
    public ResponseEntity<ApiResponse<TodoResponse>> updateTodoStatus(
            @PathVariable UUID id,
            @Valid @RequestBody TodoStatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Todo status updated successfully", todoService.updateTodoStatus(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a todo by ID")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @PathVariable UUID id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok(ApiResponse.success("Todo deleted successfully", null));
    }

    @DeleteMapping("/completed")
    @Operation(summary = "Delete all completed todos")
    public ResponseEntity<ApiResponse<String>> deleteAllCompleted() {
        int count = todoService.deleteAllCompletedTodos();
        return ResponseEntity.ok(ApiResponse.success("Deleted " + count + " completed todo(s)", null));
    }
}
