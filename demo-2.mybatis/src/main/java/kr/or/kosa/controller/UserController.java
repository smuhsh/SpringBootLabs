package kr.or.kosa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.kosa.model.User;
import kr.or.kosa.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
	
	/*
	@GetMapping("/users")
	public String list(Model model) {
		
		//ModelAndView mv = new ModelAndView();
		//함수의 return String >> view 이름
		model.addAttribute("message","Hello World Spering Boot ^^"); //자동 forward view 전달된다 ... key 로 view 처리
		//return "/WEB-INF/views/Test.jsp";
		return "Test";
		
	};
	 */
	@Autowired
	private UserService userService;
	
	@GetMapping
	public String listUser(Model model) {
		List<User> users = userService.getAllUsers();
		model.addAttribute("users",users);
		
		return "user/list";
		// /WEB-INF/views/user/list.jsp
	}
	
	@GetMapping("/new")
	public String showCreateForm() {
		return "user/form";
	}
	
	@PostMapping
	public String createUser(@ModelAttribute User user) {
		userService.createUser(user);
		return "redirect:/users"; //insert 하고 목록가기 (F5 새로고침: list 페이지)
	}
	
	//수정하기
	//1. 기존 데이터 보여주기 SELECT
	//2. 데이터 수정해서 저장하기 UPDATE
	
	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable("id")long id, Model model) {
		
		User user = userService.getUserById(id);
		model.addAttribute("user",user);
		
		return "user/edit";
	}
	
	@PostMapping("/{id}/edit")
	public String updateUser(@PathVariable("id")long id, @ModelAttribute User user) {
		user.setId(id);
		userService.updateUser(user);
		return "redirect:/users";
		
	}
	
	@GetMapping("/{id}/delete")
	public String deleteUser(@PathVariable("id")long id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}

}
