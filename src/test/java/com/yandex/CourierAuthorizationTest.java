package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CourierAuthorizationTest {
    ScooterClient scooterClient;
    Courier courierWithValidData;
    CourierCredentials courierCredentialsWithValidData;
    int courierId;

    @Before
    public void setUp() {
        scooterClient = new ScooterClient();
        courierWithValidData = Courier.getRandomCourier();
        scooterClient.createCourier(courierWithValidData);
        courierCredentialsWithValidData = new CourierCredentials(courierWithValidData.getLogin(), courierWithValidData.getPassword());
        courierId = scooterClient.getIdCourier(courierCredentialsWithValidData);
    }

    @Test
    @DisplayName("Проверка, что курьер может авторизоваться")
    public void courierCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = scooterClient.loginCourier(courierCredentialsWithValidData);
        int statusCode = loginResponse.extract().statusCode();
        if (statusCode == 200) {
            courierId = loginResponse.extract().path("id");
        } else {
            courierId = 0;
        }
        Assert.assertEquals("Неверный статус-код", 200, statusCode);
        Assert.assertNotEquals("Не удалось залогиниться", 0, courierId);
    }

    @Test
    @DisplayName("Проверка, что курьер не может авторизоваться с неверным паролем")
    public void courierCannotLoginWithNotValidPassword() {
        String wrongPassword = "wrongPassword";

        ValidatableResponse loginResponse = scooterClient.loginCourier(new CourierCredentials(courierWithValidData.getLogin(), wrongPassword));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Учетная запись не найдена", message);
    }

    @Test
    @DisplayName("Проверка, что курьер не может авторизоваться с неверным логином")
    public void courierCannotLoginWithNotValidLogin() {
        String wrongLogin = "wrongLogin";

        ValidatableResponse loginResponse = scooterClient.loginCourier(new CourierCredentials(wrongLogin, courierWithValidData.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Учетная запись не найдена", message);
    }

    @Test
    @DisplayName("Проверка, что курьер не может авторизоваться с неверными логином и паролем")
    public void courierCannotLoginWithNotValidLoginAndPassword() {
        String wrongLogin = "wrongLogin";
        String wrongPassword = "wrongPassword";

        ValidatableResponse loginResponse = scooterClient.loginCourier(new CourierCredentials(wrongLogin, wrongPassword));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 404, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Учетная запись не найдена", message);
    }

    @Test
    @DisplayName("Проверка, что курьер не может авторизоваться без пароля")
    public void courierCannotLoginWithoutPassword() {
        ValidatableResponse loginResponse = scooterClient.loginCourier(CourierCredentials.builder().login(courierWithValidData.getLogin()).password("").build());
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 400, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для входа", message);
    }

    @Test
    @DisplayName("Проверка, что курьер не может авторизоваться без логина")
    public void courierCannotLoginWithoutLogin() {
        ValidatableResponse loginResponse = scooterClient.loginCourier(CourierCredentials.builder().login("").password(courierWithValidData.getPassword()).build());
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 400, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для входа", message);
    }

    @After
    public void tearDown() {
        scooterClient.deleteCourier(courierId);
    }

}