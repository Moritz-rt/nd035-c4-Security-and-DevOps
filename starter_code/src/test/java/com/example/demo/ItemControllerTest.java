package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemControllerTest {

    @Autowired
    ItemController itemController;

    @Test
    public void getItems() {

        Boolean isException = false;
        try {
            ResponseEntity<Item> item = itemController.getItemById(1L);
        } catch (Exception e) {
            isException = true;
        }
        assertFalse(isException);

        try {
            ResponseEntity<Item> item2 = itemController.getItemById(10L);
        } catch (Exception e) {
            isException = true;
        }
        assertTrue(isException);
    }
}
