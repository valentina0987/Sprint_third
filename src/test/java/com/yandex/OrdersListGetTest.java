package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class OrdersListGetTest {
    ScooterClient scooterClient;

    @Before
    public void setUp() {
        scooterClient = new ScooterClient();
    }

    @Test
    @DisplayName("Получение списка всех заказов")
    public void checkGetAllOrders() {
        Response ordersResponse = scooterClient.getOrders();
        OrdersList scooterOrders = ordersResponse.body().as(OrdersList.class);
        int totalOrder = ordersResponse.then().statusCode(200).extract().path("pageInfo.total");

        assertThat(totalOrder, greaterThan(0));
        Assert.assertFalse("Список заказов пустой", scooterOrders.getOrders().isEmpty());
    }
}
