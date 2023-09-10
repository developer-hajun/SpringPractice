package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-order")
    public List<Order> showOrderV1(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        for (Order order : allByString) {
            order.getMember().getName();//LAZY강제 초기화
            order.getDelivery();
        }
        return allByString;
    }
    @GetMapping("/api/v2/simple-order")
    public Result showOrderV2(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        List<OrderDTO> collect = allByString.stream()
                .map(m -> new OrderDTO(m))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    @Data
    static class OrderDTO{
        private Long OrderId;
        private String name;
        private LocalDateTime localDateTime;
        private OrderStatus orderStatus;
        private Address address;
        public OrderDTO(Order order){
            this.OrderId = order.getId();
            this.name = order.getMember().getName(); //LAZY 초기화
            this.localDateTime = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress(); //LAZY초기화
        }
    }
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}
