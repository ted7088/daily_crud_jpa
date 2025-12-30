package com.example.daily_crud_jpa.service;

import com.example.daily_crud_jpa.dto.TodoRequestDto;
import com.example.daily_crud_jpa.dto.TodoResponseDto;
import com.example.daily_crud_jpa.entity.Todo;
import com.example.daily_crud_jpa.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    // 전체 조회
    public List<TodoResponseDto> getAllTodos() {
        return todoRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 단건 조회
    public TodoResponseDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("할일을 찾을 수 없습니다. ID: " + id));
        return TodoResponseDto.fromEntity(todo);
    }

    // 완료 상태별 조회
    public List<TodoResponseDto> getTodosByCompleted(Boolean completed) {
        return todoRepository.findByCompleted(completed)
                .stream()
                .map(TodoResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 생성
    @Transactional
    public TodoResponseDto createTodo(TodoRequestDto requestDto) {
        Todo todo = Todo.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .completed(requestDto.getCompleted() != null ? requestDto.getCompleted() : false)
                .build();

        Todo savedTodo = todoRepository.save(todo);
        return TodoResponseDto.fromEntity(savedTodo);
    }

    // 수정
    @Transactional
    public TodoResponseDto updateTodo(Long id, TodoRequestDto requestDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("할일을 찾을 수 없습니다. ID: " + id));

        todo.setTitle(requestDto.getTitle());
        todo.setDescription(requestDto.getDescription());
        if (requestDto.getCompleted() != null) {
            todo.setCompleted(requestDto.getCompleted());
        }

        return TodoResponseDto.fromEntity(todo);
    }

    // 완료 상태 토글
    @Transactional
    public TodoResponseDto toggleTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("할일을 찾을 수 없습니다. ID: " + id));

        todo.setCompleted(!todo.getCompleted());
        return TodoResponseDto.fromEntity(todo);
    }

    // 삭제
    @Transactional
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("할일을 찾을 수 없습니다. ID: " + id);
        }
        todoRepository.deleteById(id);
    }

    // 일괄 삭제
    @Transactional
    public void deleteMultipleTodos(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("삭제할 할일 ID가 없습니다.");
        }

        // 존재하지 않는 ID는 무시하고 계속 진행
        for (Long id : ids) {
            try {
                todoRepository.deleteById(id);
            } catch (Exception e) {
                // 이미 삭제되었거나 존재하지 않는 경우 무시
            }
        }
    }
}
