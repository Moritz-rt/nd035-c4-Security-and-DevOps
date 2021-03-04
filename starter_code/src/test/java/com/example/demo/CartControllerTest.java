package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class CartControllerTest {


    @Test
    public void prepareCart() {
        Cart cart = new Cart();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("apples");
        item1.setPrice(new BigDecimal(2.99));
        item1.setDescription("Big box with fresh apples");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("bananas");
        item2.setPrice(new BigDecimal(1.99));
        item2.setDescription("5 bananas special");

        assertFalse(item1.equals(item2));

        cart.addItem(item1);
        cart.addItem(item2);
        assertEquals(2, cart.getItems().size());
        assertEquals(new BigDecimal(4.98).setScale(2, RoundingMode.HALF_UP), cart.getTotal().setScale(2, RoundingMode.HALF_UP));
    }
}
