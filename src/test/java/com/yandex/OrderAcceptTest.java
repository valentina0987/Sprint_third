package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class OrderAcceptTest {
    ScooterClient scooterClient;
    int courierId = 0;
    int orderId = 0;
    int track = 0;

    @Before
    public void setUp() {
        scooterClient = new ScooterClient();
    }

    @Test
    @DisplayName("Проверка принятия заказа с действительными данными")
    public void checkPutAcceptOrderWithValidData() {
        courierId = scooterClient.createRandomCourier();
        HashMap<String, Integer> TrackAndIdOrder = scooterClient.createRandomOrder();
        orderId = TrackAndIdOrder.get("id");
        track = TrackAndIdOrder.get("track");

        ValidatableResponse acceptOrderResponse = scooterClient.putAcceptOrder(orderId, courierId);
        int statusCode = acceptOrderResponse.extract().statusCode();
        boolean message = acceptOrderResponse.extract().path("ok");
        Assert.assertEquals("Неверный статус-код", 200, statusCode);
        Assert.assertTrue("Неверное ответное сообщение сервера", message);
    }

    @Test
    @DisplayName("Проверка принятия заказа с недействительными данными (с недействительным ID курьера)")
    public void checkPutAcceptOrderWithNotValidCourierId() {
        courierId = scooterClient.createRandomCourier();
        scooterClient.deleteCourier(courierId);
        HashMap<String, Integer> TrackAndIdOrder = scooterClient.createRandomOrder();
        orderId = TrackAndIdOrder.get("id");
        track = TrackAndIdOrder.get("track");

        ValidatableResponse acceptOrderResponse = scooterClient.putAcceptOrder(orderId, courierId);
        int statusCode = acceptOrderResponse.extract().statusCode();
        String message = acceptOrderResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Курьера с таким id не существует", message);
    }

    @Test
    @DisplayName("Проверка принятия заказа с недействительными данными (без ID курьера)")
    public void checkPutAcceptOrderWithoutCourierId() {
        courierId = 0;
        HashMap<String, Integer> TrackAndIdOrder = scooterClient.createRandomOrder();
        orderId = TrackAndIdOrder.get("id");
        track = TrackAndIdOrder.get("track");

        ValidatableResponse acceptOrderResponse = scooterClient.putAcceptOrder(orderId, courierId);
        int statusCode = acceptOrderResponse.extract().statusCode();
        String message = acceptOrderResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 400, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для поиска", message);
    }

    @Test
    @DisplayName("Проверка принятия заказа с недействительными данными (без ID заказа)")
    public void checkPutAcceptOrderWithoutOrderId() {
        courierId = scooterClient.createRandomCourier();
        orderId = 0;
        track = 0;

        ValidatableResponse acceptOrderResponse = scooterClient.putAcceptOrder(orderId, courierId);
        int statusCode = acceptOrderResponse.extract().statusCode();
        String message = acceptOrderResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Not Found.", message);
    }

    @Test
    @DisplayName("Проверка принятия заказа с недействительными данными (с недействительным ID заказа)")
    public void checkPutAcceptOrderWithNotValidOrderId() {
        courierId = scooterClient.createRandomCourier();
        orderId = 999999;
        track = 0;
        ValidatableResponse acceptOrderResponse = scooterClient.putAcceptOrder(orderId, courierId);
        int statusCode = acceptOrderResponse.extract().statusCode();
        String message = acceptOrderResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Заказа с таким id не существует", message);
    }

    @After
    public void tearDown() {
        scooterClient.deleteCourier(courierId);
        scooterClient.cancelOrderByTrack(track);
    }

}
