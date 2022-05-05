package com.yandex;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CourierCreateTest {

    ScooterClient scooterClient;
    Courier courierWithValidData;
    int courierId;

    @Before
    public void setUp() {
        scooterClient = new ScooterClient();
    }

    @Test
    @DisplayName("Проверка, что курьера можно создать")
    public void courierCreateWithValidCredentials() {
        courierWithValidData = Courier.getRandomCourier();
        CourierCredentials courierCredentialsWithValidData = new CourierCredentials(courierWithValidData);
        ValidatableResponse createResponse = scooterClient.createCourier(courierWithValidData);
        courierId = scooterClient.getIdCourier(courierCredentialsWithValidData);
        int statusCode = createResponse.extract().statusCode();
        boolean message = createResponse.extract().path("ok");
        Assert.assertEquals("Неверный статус-код", 201, statusCode);
        Assert.assertNotEquals("Не удалось создать курьера", 0, courierId);
        Assert.assertTrue("Неверное сообщение об ошибке", message);
    }

    @Test
    @DisplayName("Проверка, что нельзя создать двух одинаковых курьеров")
    public void createCannotTwoIdenticalCouriers() {
        courierWithValidData = Courier.getRandomCourier();
        ValidatableResponse createFirstCourierResponse = scooterClient.createCourier(courierWithValidData);
        ValidatableResponse createSecondCourierResponse = scooterClient.createCourier(courierWithValidData);
        int statusCode = createSecondCourierResponse.extract().statusCode();
        String message = createSecondCourierResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 409, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Этот логин уже используется. Попробуйте другой.", message);
    }

    @Test
    @DisplayName("Проверка, что нельзя создать нового курьера без логина")
    public void courierCreateCannotWithoutLogin() {
        Courier courierWithoutLogin = Courier.builder()
                .password("ValidPassword")
                .firstName("ValidName")
                .build();
        ValidatableResponse createResponse = scooterClient.createCourier(courierWithoutLogin);
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 400, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для создания учетной записи", message);
    }

    @Test
    @DisplayName("Проверка, что нельзя создать нового курьера без пароля")
    public void courierCreateCannotWithoutPassword() {
        Courier courierWithoutPassword = Courier.builder()
                .login("ValidTestLogin")
                .firstName("ValidName")
                .build();
        ValidatableResponse createResponse = scooterClient.createCourier(courierWithoutPassword);
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 400, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Недостаточно данных для создания учетной записи", message);
    }

    @Test
    @DisplayName("Проверка, что нельзя создать нового курьера без имени")
    public void courierCreateCanWithoutFirstName() {
        Courier courierWithoutFirstName = Courier.builder()
                .login("ValidTestLogin")
                .password("ValidPassword")
                .build();
        CourierCredentials courierCredentialsWithoutFirstName = new CourierCredentials(courierWithoutFirstName);
        ValidatableResponse createResponse = scooterClient.createCourier(courierWithoutFirstName);
        courierId = scooterClient.getIdCourier(courierCredentialsWithoutFirstName);
        int statusCode = createResponse.extract().statusCode();
        boolean message = createResponse.extract().path("ok");
        Assert.assertEquals("Неверный статус-код", 201, statusCode);
        Assert.assertNotEquals("Не удалось создать курьера", 0, courierId);
        Assert.assertTrue("Неверное сообщение об ошибке", message);
    }

    @Test
    @DisplayName("Проверка, что новый курьер не может быть создан при повторном входе в систему")
    public void createCannotCouriersWithRepeatedLogin() {
        courierWithValidData = Courier.getRandomCourier();
        Courier courierWithRepeatedLogin = new Courier(courierWithValidData.getLogin(), "newPassword", "Name");
        ValidatableResponse createFirstCourierResponse = scooterClient.createCourier(courierWithValidData);
        ValidatableResponse createSecondCourierResponse = scooterClient.createCourier(courierWithRepeatedLogin);
        int statusCode = createSecondCourierResponse.extract().statusCode();
        String message = createSecondCourierResponse.extract().path("message");
        Assert.assertEquals("Неверный статус-код", 409, statusCode);
        Assert.assertEquals("Неверное сообщение об ошибке", "Этот логин уже используется. Попробуйте другой.", message);
    }

    @After
    public void tearDown() {
        scooterClient.deleteCourier(courierId);
    }

}