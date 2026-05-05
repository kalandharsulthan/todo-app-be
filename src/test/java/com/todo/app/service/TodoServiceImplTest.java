package com.todo.app.service;

import com.todo.app.dto.request.TodoRequest;
import com.todo.app.dto.response.TodoResponse;
import com.todo.app.entity.Todo;
import com.todo.app.enums.TodoPriority;
import com.todo.app.enums.TodoStatus;
import com.todo.app.exception.ResourceNotFoundException;
import com.todo.app.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void getTodoById_whenExists_returnsTodoResponse() {
        UUID id = UUID.randomUUID();
        Todo todo = Todo.builder()
                .id(id).title("Test Todo")
                .status(TodoStatus.PENDING).priority(TodoPriority.MEDIUM)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        TodoResponse response = todoService.getTodoById(id);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo("Test Todo");
        assertThat(response.getStatus()).isEqualTo(TodoStatus.PENDING);
    }

    @Test
    void getTodoById_whenNotExists_throwsResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.getTodoById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Todo not found");
    }

    @Test
    void createTodo_withNullStatusAndPriority_savedWithDefaults() {
        TodoRequest request = new TodoRequest();
        request.setTitle("New Todo");

        Todo savedTodo = Todo.builder()
                .id(UUID.randomUUID()).title("New Todo")
                .status(TodoStatus.PENDING).priority(TodoPriority.MEDIUM)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        TodoResponse response = todoService.createTodo(request);

        assertThat(response.getStatus()).isEqualTo(TodoStatus.PENDING);
        assertThat(response.getPriority()).isEqualTo(TodoPriority.MEDIUM);
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void deleteTodo_whenNotExists_throwsResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(todoRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> todoService.deleteTodo(id))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(todoRepository, never()).deleteById(any());
    }
}
