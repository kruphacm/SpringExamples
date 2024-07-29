package com.example.todo.todoapplication.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.todo.todoapplication.entity.TodoList;
import com.example.todo.todoapplication.repository.TodoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TodoController {

	@Autowired
	private TodoRepository todoRepository;

	private static final String TODOHTML="todo.html";
	private static final String TODOLIST="todolist";
	private static final String TODOALLLIST="allTabActive";
	private static final String TODOACTIVELIST="OpenTabActive";
	private static final String TODOCLOSELIST="CloseTabActive";
	@RequestMapping(value = { "", "/", "/todo" })
	public String displayContactPage(Model model) {
		List<TodoList> list = (List<TodoList>) todoRepository.findAll();
		model.addAttribute(TODOLIST, new TodoList());
		model.addAttribute(TODOALLLIST, true);
		model.addAttribute(TODOACTIVELIST, false);
		model.addAttribute(TODOCLOSELIST, false);
		model.addAttribute("list", list);
		return TODOHTML;
	}

	@PostMapping(value = "/saveTodo")
	public String saveMessage(@Valid @ModelAttribute(TODOLIST) TodoList todolist, Errors errors, Model model,
			HttpServletRequest request) {
		if (errors.hasErrors()) {
			log.error("Contact form validation failed due to : " + errors.toString());
			// Get the previous request path
			String previousPath = request.getHeader("Referer");
			// You may want to handle cases where the previous path is null

			// Add the previous path to the model
			log.info(previousPath);
			List<TodoList> list;
			if (previousPath.contains("/active")) {
				list = ((List<TodoList>) todoRepository.findAll()).stream()
						.filter(todo -> "open".equals(todo.getStatus())).toList();
				model.addAttribute(TODOALLLIST, false);
				model.addAttribute(TODOACTIVELIST, true);
				model.addAttribute(TODOCLOSELIST, false);
			} else if (previousPath.contains("/completed")) {
				list = ((List<TodoList>) todoRepository.findAll()).stream()
						.filter(todo -> "close".equals(todo.getStatus())).toList();
				model.addAttribute(TODOALLLIST, false);
				model.addAttribute(TODOACTIVELIST, false);
				model.addAttribute(TODOCLOSELIST, true);
			} else {
				list = (List<TodoList>) todoRepository.findAll();
				model.addAttribute(TODOALLLIST, true);
				model.addAttribute(TODOACTIVELIST, false);
				model.addAttribute(TODOCLOSELIST, false);
			}
			model.addAttribute("list", list);
			return TODOHTML;
		}
		todolist.setStatus("open");
		log.info("Saving TODO value");
		todoRepository.save(todolist);
		return "redirect:/";
	}

	@GetMapping(value = { "/all" })
	public String displayAllTodo(Model model) {
		List<TodoList> list = (List<TodoList>) todoRepository.findAll();
		model.addAttribute(TODOLIST, new TodoList());
		model.addAttribute("list", list);
		model.addAttribute(TODOALLLIST, true);
		model.addAttribute(TODOACTIVELIST, false);
		model.addAttribute(TODOCLOSELIST, false);
		return TODOHTML;
	}

	@GetMapping(value = { "/active" })
	public String displayOpenTodo(Model model) {
		List<TodoList> list = ((List<TodoList>) todoRepository.findAll()).stream()
				.filter(todo -> "open".equals(todo.getStatus())).toList();
		model.addAttribute(TODOLIST, new TodoList());
		model.addAttribute(TODOALLLIST, false);
		model.addAttribute(TODOACTIVELIST, true);
		model.addAttribute(TODOCLOSELIST, false);
		model.addAttribute("list", list);
		return TODOHTML;
	}

	@GetMapping(value = { "/completed" })
	public String displayCloseTodo(Model model) {
		List<TodoList> list = ((List<TodoList>) todoRepository.findAll()).stream()
				.filter(todo -> "close".equals(todo.getStatus())).toList();
		model.addAttribute(TODOLIST, new TodoList());
		model.addAttribute("list", list);
		model.addAttribute(TODOALLLIST, false);
		model.addAttribute(TODOACTIVELIST, false);
		model.addAttribute(TODOCLOSELIST, true);
		return TODOHTML;
	}

	@PostMapping(value = "/closeTodo/{id}")
	public String closeTodo(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		Optional<TodoList> val = todoRepository.findById(id);
		if (!val.isEmpty()) {
			int count = todoRepository.updateRow(id);
			if (count > 0) {
				log.info("Updated successfully");
			} else {
				log.info("Update failed");
			}
		} else {
			redirectAttributes.addFlashAttribute("closeError", "Failed to close the task.");
			log.info("Entry not available");
		}
		return "redirect:/todo";
	}
}
