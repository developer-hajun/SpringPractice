package jpabook.jpashop.repository.order.Querys;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDTO{
    private Long id;
    private String name;
    private LocalDateTime dateTime;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Long id, String name, LocalDateTime dateTime, OrderStatus orderStatus, Address address) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}