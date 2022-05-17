package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;


@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final String[] colorArray;
    ScooterClient scooterClient;
    int orderId;

    public OrderCreateTest(String[] colorArray) {
        this.colorArray = colorArray;
    }

    @Parameterized.Parameters(name = "Test scooter color: {0}{1}")
    public static Object[][] colorArrayData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {null},
        };
    }

    @Test
    @DisplayName("Проверка создания нового заказа с возможностью заказать самокат разных цветов")
    public void newOrderCanCreateWithValidDataAndDifferentColors() {
        scooterClient = new ScooterClient();
        Order orderWithValidData = Order.getRandomOrder();
        orderWithValidData.setColor(colorArray);
        ValidatableResponse createResponse = scooterClient.createOrder(orderWithValidData);
        orderId = createResponse.extract().path("track");

        assertThat(orderId, greaterThan(0));

    }

}
