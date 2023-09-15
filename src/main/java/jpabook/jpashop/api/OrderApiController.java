package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/order")
    public List<Order> ordersV1(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        for (Order order : allByString) {
            order.getMember().getName();                                          
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o-> o.getItem().getName());
        }
        return allByString;
    }
    @GetMapping("/api/v2/order")
    public List<OrderDTO> ordersV2(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        List<OrderDTO> collect = allByString.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return collect;
    }
    @GetMapping("/api/v3/order")
    public List<OrderDTO> ordersV3(){
        List<Order> allByString = orderRepository.findAllWithByItem();
        for (Order order : allByString) {
            System.out.println("order = "+order + " id = "+order.getId());
        }
        List<OrderDTO> collect = allByString.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return collect;

    }
    @Data
    public class OrderDTO{
        private Long id;
        private String name;
        private LocalDateTime dateTime;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDTO> orderItems;
        public OrderDTO(Order order) {
            this.id = order.getId();
            this.name = order.getMember().getName();
            this.dateTime = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream().map(o-> new OrderItemDTO(o)).collect(Collectors.toList());
        }
    }
    @Data
    public class OrderItemDTO{
        private Long id;
        private String name;
        private int price;
        private int count;

        public OrderItemDTO(OrderItem orderItem) {
            this.id = orderItem.getId();
            this.name = orderItem.getItem().getName();
            this.price = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}