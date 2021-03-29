package com.example.primavista;


import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.primavista.documents.entity.User;
import com.example.primavista.documents.repository.UserRepository;
import com.example.primavista.documents.services.UsersService;


@Controller
@RequestMapping("/signUpForm")
public class ViewController {
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	UserRepository userRepo;
	
	
	@ModelAttribute("user")
	public User userDto() {
		return new User();
	}
	
	
	@GetMapping
	public String showSignUpForm(Model model) {
		
	    return "userForm";
	}
	
	@PostMapping
	public String registerUser(@ModelAttribute("user") User userDto,RedirectAttributes redirAttrs) {
	List<User> users = userRepo.findAll();
	for (User user : users) {
		if (user.getEmail().equalsIgnoreCase(userDto.getEmail())) {
			redirAttrs.addFlashAttribute("error", "Email exists");
			return "redirect:/signUpForm?error";
		}
	}
	usersService.save(userDto);
	redirAttrs.addFlashAttribute("success", "Success");
	return  "redirect:/signUpForm?success" ;
	}

}
