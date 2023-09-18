package jpabook.jpashop.service.Query;

import jpabook.jpashop.domain.OrderItem;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private String name;
    private int price;
    private int count;


    public OrderItemDTO(Long id, String name, int price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }
    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.name = orderItem.getItem().getName();
        this.price = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}