package com.todo.app.repository;

import com.todo.app.entity.Todo;
import com.todo.app.enums.TodoPriority;
import com.todo.app.enums.TodoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface TodoRepository extends JpaRepository<Todo, UUID> {

    @Query("""
            SELECT t FROM Todo t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:priority IS NULL OR t.priority = :priority)
            """)
    Page<Todo> findByFilters(
            @Param("status") TodoStatus status,
            @Param("priority") TodoPriority priority,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Todo t WHERE t.status = com.todo.app.enums.TodoStatus.COMPLETED")
    int deleteAllCompleted();
}
