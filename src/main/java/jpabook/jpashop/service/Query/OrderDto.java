package jpabook.jpashop.service.Query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto {
    private Long id;
    private String name;
    private LocalDateTime dateTime;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDTO> orderItems;

    public OrderDto(Long id, String name, LocalDateTime dateTime, OrderStatus orderStatus, Address address) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public OrderDto(Long id, String name, LocalDateTime dateTime, OrderStatus orderStatus, Address address, List<OrderItem> orderItems) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderItems = orderItems.stream().map(o-> new jpabook.jpashop.service.Query.OrderItemDTO(o)).collect(Collectors.toList());
    }
}