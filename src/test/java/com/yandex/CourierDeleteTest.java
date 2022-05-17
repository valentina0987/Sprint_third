package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CourierDeleteTest {
    ScooterClient scooterClient;
    int courierId;

    @Before
    public void setUp() {
        scooterClient = new ScooterClient();
    }

    @Test
    @DisplayName("Проверка удаления курьера с действительным ID")
    public void checkDeleteCourierWithValidId() {
        courierId = scooterClient.createRandomCourier();
        ValidatableResponse deleteResponse = scooterClient.deleteCourier(courierId);
        int statusCode = deleteResponse.extract().statusCode();
        boolean message = deleteResponse.extract().path("ok");
        Assert.assertEquals("Неверный статус-код", 200, statusCode);
        Assert.assertTrue("Не удалось удалить курьера", message);
    }

    @Test
    @DisplayName("Проверка удаления курьера с недействительным ID")
    public void checkDeleteCourierWithNotValidId() {
        courierId = scooterClient.createRandomCourier();
        scooterClient.deleteCourier(courierId);
        ValidatableResponse deleteResponse = scooterClient.deleteCourier(courierId);
        int statusCode = deleteResponse.extract().statusCode();
        String message = deleteResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Не верное сообщение об ошибке", "Курьера с таким id нет.", message);
    }

    @Test
    @DisplayName("Проверка удаления курьера без ID")
    public void checkDeleteCourierWithoutId() {
        ValidatableResponse deleteResponse = scooterClient.deleteCourier(0);
        int statusCode = deleteResponse.extract().statusCode();
        String message = deleteResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Не верное сообщение об ошибке", "Not Found.", message);
    }

}