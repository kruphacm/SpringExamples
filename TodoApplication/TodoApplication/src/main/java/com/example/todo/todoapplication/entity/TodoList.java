package com.example.todo.todoapplication.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="todolist")
public class TodoList {
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	private int id;
	
	@NotBlank(message="Content should not be blank")
	@Size(min = 10,message="Minimum of 10 character acceptable")
	private String content;
	
	private String status;

}
