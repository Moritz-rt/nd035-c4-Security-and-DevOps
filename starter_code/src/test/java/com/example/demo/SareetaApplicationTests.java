package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.HelperPW;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {

	private UserController mockUserController;

	private UserRepository mockUserRepository = mock(UserRepository.class);

	private CartRepository mockCartRepository = mock(CartRepository.class);

	private BCryptPasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);

	@Before
	public void setUp() {
		mockUserController = new UserController();
		TestUtils.injectObjects(mockUserController, "userRepository", mockUserRepository);
		TestUtils.injectObjects(mockUserController, "cartRepository", mockCartRepository);
		TestUtils.injectObjects(mockUserController, "bCryptPasswordEncoder", mockEncoder);
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void createUserHappyPath() throws Exception {
		when(mockEncoder.encode("123-456-789")).thenReturn("mocked-hashedPassword");
		CreateUserRequest createUserRequest = createUserRequest("");

		final ResponseEntity<User> responseEntity = mockUserController.createUser(createUserRequest);
		assertNotNull(responseEntity);
		assertEquals(200, responseEntity.getStatusCodeValue());
		User u = responseEntity.getBody();
		assertNotNull(u);
		assertEquals(0, u.getId());
		assertEquals(createUserRequest.getUsername(), u.getUsername());
		assertEquals("mocked-hashedPassword", u.getPassword());
	}

	@Test
	public void testPwHashing() {
		byte[] salt = HelperPW.createSalt();
		String password = "sK4_:8lf-3FP8s+f";
		String hash1 = HelperPW.get_SecurePassword(password, salt);
		String hash2 = HelperPW.get_SecurePassword(password, salt);
		assertEquals(hash1, hash2);
	}

	private static CreateUserRequest createUserRequest(String number) {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("TestEmployee"+number);
		createUserRequest.setPassword("123-456-789"+number);
		createUserRequest.setConfirmPassword("123-456-789"+number);
		return createUserRequest;
	}
}
