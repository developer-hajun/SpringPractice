package jpabook.jpashop.repository.order.Querys;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;
    public List<OrderDTO> findOrders() {
        List<OrderDTO> resultList = em.createQuery("select new jpabook.jpashop.repository.order.Querys.OrderDTO(o.id,m.name,o.orderDate,o.status,d.address) " +
                "from Order o join o.member m join o.delivery d", OrderDTO.class).getResultList();
        return resultList;
    }
    public List<OrderDTO> findOrderQueryDtos(){
        List<OrderDTO> orders = findOrders();
        orders.forEach(
                o-> {
                    List<OrderItemDTO> orderItems = findOrderItems(o.getId());
                    o.setOrderItems(orderItems);
                }
        );
        return orders;
    }
    public List<OrderDTO> findOrderQueryDtos_optimization(){
        List<OrderDTO> orders = findOrders();
        List<Long> orderId = orders.stream()
                        .map(o->o.getId()).collect(Collectors.toList());
        List<OrderItemDTO> orderId1 = em.createQuery("select new jpabook.jpashop.repository.order.Querys.OrderItemDTO(oi.id,oi.item.name,oi.orderPrice,oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item " +
                                "where oi.order.id in :orderId ",
                        OrderItemDTO.class)
                .setParameter("orderId", orderId)
                .getResultList();

        Map<Long, List<OrderItemDTO>> collect = orderId1.stream().
                collect(Collectors.groupingBy(OrderItemDTO -> OrderItemDTO.getId()));
        orders.forEach(o->o.setOrderItems(collect.get(o.getId())));
        return orders;
    }

    private List<OrderItemDTO> findOrderItems(Long id) {
        return em.createQuery("select new jpabook.jpashop.repository.order.Querys.OrderItemDTO(oi.id,oi.item.name,oi.orderPrice,oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item " +
                        "where oi.order.id=:orderId ",
                        OrderItemDTO.class)
                .setParameter("orderId", id)
                .getResultList();
    }
}
