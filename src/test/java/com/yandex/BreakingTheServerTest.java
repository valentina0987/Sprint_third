package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class BreakingTheServerTest {

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
    //тест падает, при дебагинге ошибка 504 (такая же и при отправке запроса через Postman)

    @Test
    @DisplayName("Проверка, что сервер может сломаться")
    public void courierWithNotPasswordBreaksServer() {
        ValidatableResponse loginResponse = scooterClient.loginCourier(CourierCredentials.builder().login(courierWithValidData.getLogin()).build());
        String message = loginResponse.statusCode(400).extract().path("message");

        Assert.assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для входа", message);
    }
}
