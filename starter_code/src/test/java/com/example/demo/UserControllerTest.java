package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;


@SpringBootTest(classes = SareetaApplication.class)
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Test
    public void createUser() {
        CreateUserRequest createUserRequest = createUserRequest("3");
        ResponseEntity<User> user = userController.createUser(createUserRequest);
        Assert.assertEquals(200, user.getStatusCodeValue());

        ResponseEntity<User> userFromDB = userController.findByUserName(createUserRequest.getUsername());
        Assert.assertEquals(200, userFromDB.getStatusCodeValue());
        Assert.assertEquals(createUserRequest.getUsername(), userFromDB.getBody().getUsername());
    }

    @Test
    public void createUserValidation() {
        CreateUserRequest createUserRequest = createUserRequest("4");
        createUserRequest.setPassword("123");
        ResponseEntity<User> user = userController.createUser(createUserRequest);
        Assert.assertEquals(400, user.getStatusCodeValue());

        createUserRequest.setConfirmPassword("123");
        ResponseEntity<User> user2 = userController.createUser(createUserRequest);
        Assert.assertEquals(400, user2.getStatusCodeValue());
    }

//    @Test
//    public void shoppingTour() {
//        CreateUserRequest createUserRequest = createUserRequest("5");
//        ResponseEntity<User> user = userController.createUser(createUserRequest);
//
//        ResponseEntity<User> userFromDB = userController.findById(user.getBody().getId());
//        Assert.assertEquals(200, userFromDB.getStatusCodeValue());
//        Assert.assertEquals(createUserRequest.getUsername(), userFromDB.getBody().getUsername());
//
//        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(createUserRequest.getUsername(), 1, 1);
//        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
//        Assert.assertEquals(200, cartResponseEntity.getStatusCodeValue());
//        Assert.assertEquals(1, cartResponseEntity.getBody().getItems().size());
//
//        ResponseEntity<Cart> cartResponseEntity2 = cartController.removeFromcart(modifyCartRequest);
//        Assert.assertEquals(200, cartResponseEntity2.getStatusCodeValue());
//        Assert.assertEquals(0, cartResponseEntity2.getBody().getItems().size());
//    }

    private static CreateUserRequest createUserRequest(String number) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("TestEmployee"+number);
        createUserRequest.setPassword("123-456-789"+number);
        createUserRequest.setConfirmPassword("123-456-789"+number);
        return createUserRequest;
    }
}
