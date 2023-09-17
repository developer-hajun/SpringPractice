package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.Querys.OrderSimpleQueryRepository;
import jpabook.jpashop.repository.order.Querys.SimpleOrderQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;
    @GetMapping("/api/v1/simple-order")
    public List<Order> showOrderV1(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        for (Order order : allByString) {
            order.getMember().getName();//LAZY강제 초기화
            order.getDelivery();
        }
        return allByString;
    }
    //Entity를 그대로 json 형태로 노출
    @GetMapping("/api/v2/simple-order")
    public Result showOrderV2(){
        List<Order> allByString = orderRepository.findAllByString(new OrderSearch());
        List<OrderDTO> collect = allByString.stream()
                .map(m -> new OrderDTO(m))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    //DTO로 변환하여 JSON 형태로 노출 BUT N+1문제가 남아있음 WHY? LAZY형태로 join되기 때문에
    @GetMapping("/api/v3/simple-order")
    public Result showOrderV3(){
        List<Order> allwithMemberDelivery = orderRepository.findAllwithMemberDelivery();
        List<OrderDTO> collect = allwithMemberDelivery.stream()
                .map(m -> new OrderDTO(m))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    // v2에서 n+1번 문제를 패치 조인으로 한방 쿼리로 필요한 모든 객체를 들고와서 성능 최적화

    @GetMapping("/api/v4/simple-order")
    public List<SimpleOrderQueryDTO> showOrderV4(){
        return orderSimpleQueryRepository.findOrderDTO();
    }
    //v4와 가져오는 데이터의 차이만 있을뿐이다 이떄 v3와 v4의 쿼리가 차이가 나기떄문에 그렇기에 성능차이가 거의 없다.( join 할 테이블이 매우 많을때 사용)

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
