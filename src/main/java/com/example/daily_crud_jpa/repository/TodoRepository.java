package com.example.daily_crud_jpa.repository;

import com.example.daily_crud_jpa.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 완료 상태별 조회
    List<Todo> findByCompleted(Boolean completed);

    // 제목으로 검색
    List<Todo> findByTitleContaining(String keyword);

    // 완료 상태별 정렬 조회
    List<Todo> findAllByOrderByCreatedAtDesc();
}
