package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughtStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	EntityManager em;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;

	@Test
	void 상품_주문() {
		Member member = createMember("회원1", new Address("서울", "강가", "123-123"));
		Item book = createBook("시골 JPA", 10000, 10);

		int orderCount = 2;

		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		Order getOrder = orderRepository.findOne(orderId);

		assertEquals("상품 주문시 상태는 ORDER", getOrder.getStatus(), OrderStatus.ORDER);
		assertEquals("주문한 상품 종류 수가 정확해야 한다.", getOrder.getOrderItems().size(), 1);
		assertEquals("주문 가격은 가격 * 수량이다", 10000 * orderCount, getOrder.getTotalPrice());
		assertEquals("주문 수량만큼 재고가 줄어야한다.", 8, book.getStockQuantity());
	}

	@Test
	void 상품주문_재고수량초과() {
		Member member = createMember("회원1", new Address("서울", "강가", "123-123"));
		Item book = createBook("시골 JPA", 10000, 10);

		int orderCount = 11;

		assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
				.isInstanceOf(NotEnoughtStockException.class);
	}

	@Test
	void 주문취소() {
		// given
		Member member = createMember("회원1", new Address("서울", "강가", "123-123"));
		Item book = createBook("시골 JPA", 10000, 10);

		int orderCount = 2;

		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// when
		orderService.cancelOrder(orderId);

		// then
		Order order = orderRepository.findOne(orderId);

		assertEquals("주문 취소시 상태는 CANCLE", order.getStatus(), OrderStatus.CANCLE);
		assertEquals("주문이 취소된 상품은 그만큼 재고 증가", book.getStockQuantity(), 10);
	}

	private Item createBook(String name, int price, int stockQuantity) {
		Item book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}

	private Member createMember(String name, Address address) {
		Member member = new Member();
		member.setName(name);
		member.setAddress(address);
		em.persist(member);
		return member;
	}
}
