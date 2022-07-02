package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne 성능 최적화
 * <p>
 * Order
 * Order -> Member (OnetoMany)
 * Order -> Delivery (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;

	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

		for (Order order : orders) {
			// LAZY 된거를 호출해서 강제 초기화
			order.getMember().getName();
			order.getDelivery().getAddress();
		}

		return orders;
	}
}
