package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.Querys.OrderDTO;
import jpabook.jpashop.repository.order.Querys.OrderFlatDto;
import jpabook.jpashop.repository.order.Querys.OrderQueryRepository;
import jpabook.jpashop.service.Query.OrderDto;
import jpabook.jpashop.service.Query.OrderQueryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

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
    private final OrderQueryService orderQueryService;
    @GetMapping("/api/v3/order")
    public List<OrderDto> ordersV3(){
       return orderQueryService.ordersV3();

    }
    @GetMapping("/api/v3.1/order")
    public List<OrderDTO> ordersV3_1(
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "limit",defaultValue = "100") int limit
            )
    {
        List<Order> allByString = orderRepository.findAllwithMemberDelivery(offset,limit);
        for (Order order : allByString) {
            System.out.println("order = "+order + " id = "+order.getId());
        }
        List<OrderDTO> collect = allByString.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return collect;

    }
    @GetMapping("/api/v4/order")
    public List<jpabook.jpashop.repository.order.Querys.OrderDTO> ordersV4()
    {
        List<jpabook.jpashop.repository.order.Querys.OrderDTO> allByString = orderQueryRepository.findOrderQueryDtos_optimization();
        return allByString;
    }
    @GetMapping("/api/v5/order")
    public List<jpabook.jpashop.repository.order.Querys.OrderDTO> ordersV5()
    {
        List<jpabook.jpashop.repository.order.Querys.OrderDTO> allByString = orderQueryRepository.findOrderQueryDtos();
        return allByString;
    }
    @GetMapping("/api/v6/order")
    public List<OrderFlatDto> ordersV6()
    {
        List<OrderFlatDto> allByString = orderQueryRepository.findOrderQueryDtos_flat();

        return allByString;
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