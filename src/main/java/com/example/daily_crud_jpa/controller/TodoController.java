package com.example.daily_crud_jpa.controller;

import com.example.daily_crud_jpa.dto.TodoRequestDto;
import com.example.daily_crud_jpa.dto.TodoResponseDto;
import com.example.daily_crud_jpa.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Vue 개발 서버 CORS 허용
public class TodoController {

    private final TodoService todoService;

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getAllTodos(
            @RequestParam(value = "completed", required = false) Boolean completed) {

        List<TodoResponseDto> todos;
        if (completed != null) {
            todos = todoService.getTodosByCompleted(completed);
        } else {
            todos = todoService.getAllTodos();
        }
        return ResponseEntity.ok(todos);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable("id") Long id) {
        TodoResponseDto todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    // 생성
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(@Valid @RequestBody TodoRequestDto requestDto) {
        TodoResponseDto createdTodo = todoService.createTodo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable("id") Long id,
            @Valid @RequestBody TodoRequestDto requestDto) {
        TodoResponseDto updatedTodo = todoService.updateTodo(id, requestDto);
        return ResponseEntity.ok(updatedTodo);
    }

    // 완료 상태 토글
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponseDto> toggleTodo(@PathVariable("id") Long id) {
        TodoResponseDto toggledTodo = todoService.toggleTodo(id);
        return ResponseEntity.ok(toggledTodo);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("id") Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    // 일괄 삭제
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteMultipleTodos(@RequestBody List<Long> ids) {
        todoService.deleteMultipleTodos(ids);
        return ResponseEntity.noContent().build();
    }
}
