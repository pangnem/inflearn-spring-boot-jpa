package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		// 엔티티 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);

		// 배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());

		// 주문 상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

		// 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);
		orderRepository.save(order); // CascadeType.ALL 옵션이면 안에 있는 객체도 같이 persist가 된다.

		return order.getId();
	}

	@Transactional
	public void cancelOrder(Long orderId) {
		// 주문 엔티티 조회 후 취소
		Order order = orderRepository.findOne(orderId);
		order.cancel(); // 엔티티 안 내용만 바껴도 dirty checking이 되어 데이터베이스에 업데이트 쿼리가 날라간다!
	}

//	public List<Order> SearchOrders(OrderSearchDto orderSearchDto) {}

}
