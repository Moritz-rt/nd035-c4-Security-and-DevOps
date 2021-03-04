package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.HelperPW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Transactional
@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		if(createUserRequest.getPassword().length()<7 ) {
			log.trace("User creation failed: "+createUserRequest.getUsername() + " - length:" + createUserRequest.getPassword().length());
			return ResponseEntity.badRequest().build();
		} else if(!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.trace("User creation failed: "+createUserRequest.getUsername() + " - pass and conf pass do not match");
			return ResponseEntity.badRequest().build();
		}

		User existingUser = userRepository.findByUsername(createUserRequest.getUsername());
		if(null != existingUser) {
			log.trace("User creation failed: "+createUserRequest.getUsername() + " already exists");
//			throw new Exception("User creation failed: " + createUserRequest.getUsername() + " already exists. Choose other name!");
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		Cart cart = new Cart();
		user.setCart(cart);
		//		cartRepository.save(cart);


		log.trace("User created: "+createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}


}
