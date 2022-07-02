package jpabook.jpashop;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 * userA
 * 	jpa1
 * 	jpa2
 * userB
 * 	spring1
 * 	spring2
 */
@Component
@RequiredArgsConstructor
public class InitDB {

	private final InitService initService;

	@PostConstruct
	public void init(){
		initService.dbInit1();
		initService.dbInit2();
	}


	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {

		private final EntityManager em;

		public void dbInit1() {
			Member member = new Member();
			member.setName("userA");
			member.setAddress(new Address("서울", "1", "12345"));
			em.persist(member);

			Book book1 = new Book();
			book1.setName("jpa1");
			book1.setPrice(10000);
			book1.setStockQuantity(100);
			em.persist(book1);

			Book book2 = new Book();
			book2.setName("jpa2");
			book2.setPrice(20000);
			book2.setStockQuantity(100);
			em.persist(book2);

			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 1);

			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());

			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}

		public void dbInit2() {
			Member member = new Member();
			member.setName("userB");
			member.setAddress(new Address("부산", "2", "23456"));
			em.persist(member);

			Book book1 = new Book();
			book1.setName("spring1");
			book1.setPrice(10000);
			book1.setStockQuantity(100);
			em.persist(book1);

			Book book2 = new Book();
			book2.setName("spring2");
			book2.setPrice(20000);
			book2.setStockQuantity(100);
			em.persist(book2);

			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());

			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);

		}
	}
}
