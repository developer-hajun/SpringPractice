package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit();
        initService.dbInit2();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void dbInit(){
            Member member = CreateMember("userA",new Address("부산","사하구","8828"));
            em.persist(member);
            Book book = CreateBook("JPA1",22210);
            em.persist(book);
            Book book2 = CreateBook("JPA2",13000);
            em.persist(book2);
            OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem1 = OrderItem.createOrderItem(book2, 10000, 1);
            Delivery delivery = new Delivery();
            delivery.setAddress(new Address("거제","수월","3174"));
            Order order = Order.createOrder(member, delivery, orderItem, orderItem1);
            em.persist(order);
        }

        public void dbInit2(){
            Member member = CreateMember("userB",new Address("거제","수월","1245"));
            em.persist(member);
            Book book = CreateBook("Spring1",20000);
            em.persist(book);
            Book book2 = CreateBook("Spring2",25000);
            em.persist(book2);
            OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem1 = OrderItem.createOrderItem(book2, 10000, 1);
            Delivery delivery = new Delivery();
            delivery.setAddress(new Address("부산","수영구","1234"));
            Order order = Order.createOrder(member, delivery, orderItem, orderItem1);
            em.persist(order);
        }

    }

    private static Member CreateMember(String name,Address address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        return member;
    }

    private static Book CreateBook(String Spring1,int price) {
        Book book = new Book();
        book.setName(Spring1);
        book.setPrice(price);
        book.setStockQuantity(20);
        return book;
    }
}
