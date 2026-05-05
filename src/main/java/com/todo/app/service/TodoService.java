package com.todo.app.service;

import com.todo.app.dto.request.TodoRequest;
import com.todo.app.dto.request.TodoStatusUpdateRequest;
import com.todo.app.dto.response.PagedResponse;
import com.todo.app.dto.response.TodoResponse;
import com.todo.app.enums.TodoPriority;
import com.todo.app.enums.TodoStatus;

import java.util.UUID;

public interface TodoService {

    PagedResponse<TodoResponse> getAllTodos(int page, int size,
            TodoStatus status, TodoPriority priority, String sort);

    TodoResponse getTodoById(UUID id);

    TodoResponse createTodo(TodoRequest request);

    TodoResponse updateTodo(UUID id, TodoRequest request);

    TodoResponse updateTodoStatus(UUID id, TodoStatusUpdateRequest request);

    void deleteTodo(UUID id);

    int deleteAllCompletedTodos();
}
