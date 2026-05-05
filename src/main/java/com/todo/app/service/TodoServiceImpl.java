package com.todo.app.service;

import com.todo.app.dto.request.TodoRequest;
import com.todo.app.dto.request.TodoStatusUpdateRequest;
import com.todo.app.dto.response.PagedResponse;
import com.todo.app.dto.response.TodoResponse;
import com.todo.app.entity.Todo;
import com.todo.app.enums.TodoPriority;
import com.todo.app.enums.TodoStatus;
import com.todo.app.exception.ResourceNotFoundException;
import com.todo.app.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TodoResponse> getAllTodos(int page, int size,
            TodoStatus status, TodoPriority priority, String sort) {

        Sort sortOrder = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Todo> todoPage = todoRepository.findByFilters(status, priority, pageable);
        Page<TodoResponse> responsePage = todoPage.map(TodoResponse::from);
        return PagedResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public TodoResponse getTodoById(UUID id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));
        return TodoResponse.from(todo);
    }

    @Override
    public TodoResponse createTodo(TodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDueDate(request.getDueDate());
        if (request.getStatus() != null) todo.setStatus(request.getStatus());
        if (request.getPriority() != null) todo.setPriority(request.getPriority());
        return TodoResponse.from(todoRepository.save(todo));
    }

    @Override
    public TodoResponse updateTodo(UUID id, TodoRequest request) {
        Todo existing = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));
        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setDueDate(request.getDueDate());
        existing.setStatus(request.getStatus() != null ? request.getStatus() : TodoStatus.PENDING);
        existing.setPriority(request.getPriority() != null ? request.getPriority() : TodoPriority.MEDIUM);
        return TodoResponse.from(todoRepository.save(existing));
    }

    @Override
    public TodoResponse updateTodoStatus(UUID id, TodoStatusUpdateRequest request) {
        Todo existing = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));
        existing.setStatus(request.getStatus());
        return TodoResponse.from(todoRepository.save(existing));
    }

    @Override
    public void deleteTodo(UUID id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo", "id", id);
        }
        todoRepository.deleteById(id);
    }

    @Override
    public int deleteAllCompletedTodos() {
        return todoRepository.deleteAllCompleted();
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        Sort.Direction direction = parts.length > 1 && parts[1].trim().equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(direction, field);
    }
}
