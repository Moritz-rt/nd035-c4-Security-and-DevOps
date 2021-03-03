package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SareetaApplication.class)
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    CartController cartController;

    @Autowired
    ItemController itemController;

    @Autowired
    OrderController orderController;

    // Integration
    @Test
    public void createOrder() {
        CreateUserRequest createUserRequest = createUserRequest("2");
        ResponseEntity<User> user = userController.createUser(createUserRequest);

        // read items
        ResponseEntity<List<Item>> itemListAll = itemController.getItems();
        assertEquals(2, itemListAll.getBody().size());

        ResponseEntity<List<Item>> itemListRound = itemController.getItemsByName("Round Widget");
        assertEquals(1, itemListRound.getBody().size());

        ResponseEntity<List<Item>> itemListNotFound = itemController.getItemsByName("something");
        assertEquals(404, itemListNotFound.getStatusCodeValue());

        ResponseEntity<Item> itemOne = itemController.getItemById(1L);
        assertTrue(itemOne.getBody().getName().equals("Round Widget"));

        // add to cart
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(user.getBody().getUsername(), 1, 2);
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        assertEquals(200, cartResponseEntity.getStatusCodeValue());
        assertEquals(2, cartResponseEntity.getBody().getItems().size());
        assertEquals(5.98, cartResponseEntity.getBody().getTotal().doubleValue());


        ResponseEntity<List<UserOrder>> userOrderListBefore = orderController.getOrdersForUser(user.getBody().getUsername());
        assertEquals(0, userOrderListBefore.getBody().size());
        // submit order
        ResponseEntity<UserOrder> userOrder = orderController.submit(user.getBody().getUsername());
        assertEquals(1, userOrder.getBody().getId());

        // get order history
        ResponseEntity<List<UserOrder>> userOrderList = orderController.getOrdersForUser(user.getBody().getUsername());
        assertEquals(1, userOrderList.getBody().size());
    }

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

    @Test
    public void shoppingTour() {
        CreateUserRequest createUserRequest = createUserRequest("5");
        ResponseEntity<User> user = userController.createUser(createUserRequest);

        ResponseEntity<User> userFromDB = userController.findById(user.getBody().getId());
        Assert.assertEquals(200, userFromDB.getStatusCodeValue());
        Assert.assertEquals(createUserRequest.getUsername(), userFromDB.getBody().getUsername());

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(createUserRequest.getUsername(), 1, 1);
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        Assert.assertEquals(200, cartResponseEntity.getStatusCodeValue());
        Assert.assertEquals(1, cartResponseEntity.getBody().getItems().size());

        ResponseEntity<Cart> cartResponseEntity2 = cartController.removeFromcart(modifyCartRequest);
        Assert.assertEquals(200, cartResponseEntity2.getStatusCodeValue());
        Assert.assertEquals(0, cartResponseEntity2.getBody().getItems().size());
    }

    private static CreateUserRequest createUserRequest(String number) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("TestEmployee"+number);
        createUserRequest.setPassword("123-456-789"+number);
        createUserRequest.setConfirmPassword("123-456-789"+number);
        return createUserRequest;
    }
}
