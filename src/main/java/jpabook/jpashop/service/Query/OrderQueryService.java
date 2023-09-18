package jpabook.jpashop.service.Query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;
    public List<OrderDto> ordersV3(){
        List<Order> allByString = orderRepository.findAllWithByItem();
        for (Order order : allByString) {
            System.out.println("order = "+order + " id = "+order.getId());
        }
        List<OrderDto> collect = allByString.stream()
                .map(o-> new OrderDto(o.getId(),o.getMember().getName(),o.getOrderDate(),o.getStatus(),o.getDelivery().getAddress(),o.getOrderItems()))
                .collect(Collectors.toList());
        return collect;

    }
}
