package com.example.todo.todoapplication.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.todoapplication.entity.TodoList;

public interface TodoRepository extends CrudRepository<TodoList,Integer>{

	@Modifying
	@Transactional
	@Query("update TodoList t set t.status = 'close' where t.id = :id")
	public int updateRow(@Param("id") int id);
}
