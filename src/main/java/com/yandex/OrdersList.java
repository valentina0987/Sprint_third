package com.yandex;

import lombok.Data;

import java.util.List;

@Data
public class OrdersList {
    private List<Order> orders;
    private PageInfo pageInfo;
    private List<Stations> stations;

    public OrdersList(List<Order> orders, PageInfo pageInfo, List<Stations> availableStations) {
        this.orders = orders;
        this.pageInfo = pageInfo;
        this.stations = availableStations;
    }
}
