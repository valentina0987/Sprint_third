package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderGetByTrackTest {
    ScooterClient scooterClient;
    int trackId;

    @Before
    public void setUp() {
        scooterClient = new ScooterClient();
    }

    @Test
    @DisplayName("Проверка получения заказа с действительным ID трека")
    public void checkGetOrderWithValidTrackId() {
        trackId = createRandomOrder();
        Response getOrderResponse = scooterClient.getOrderByTrack(trackId);
        Order order = getOrderResponse.body().as(OrderWithTrack.class).getOrder();
        int statusCode = getOrderResponse.then().extract().statusCode();
        Assert.assertEquals("Неверный статус-код", 200, statusCode);
        Assert.assertEquals("Возвращен неверный заказ", order.getTrack(), trackId);
    }

    @Test
    @DisplayName("Проверка получения заказа с недействительным ID трека")
    public void checkGetOrderWithNotValidTrackId() {
        int notValidTrackId = 1;
        Response getOrderResponse = scooterClient.getOrderByTrack(notValidTrackId);
        int statusCode = getOrderResponse.then().extract().statusCode();
        String message = getOrderResponse.then().extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Заказ не найден", message);
    }

    @Test
    @DisplayName("Проверка получения заказа без ID трека")
    public void checkGetOrderWithoutTrackId() {
        int trackId = 0;
        Response getOrderResponse = scooterClient.getOrderByTrack(trackId);
        int statusCode = getOrderResponse.then().extract().statusCode();
        String message = getOrderResponse.then().extract().path("message");
        Assert.assertEquals("Неверный статус-код", 400, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для поиска", message);
    }

    private int createRandomOrder() {
        Order orderWithValidData = Order.getRandomOrder();
        trackId = scooterClient.createOrder(orderWithValidData).extract().path("track");
        return trackId;
    }

}
