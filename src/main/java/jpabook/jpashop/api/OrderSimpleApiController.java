package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

		return orders.stream()
				.map(SimpleOrderDto::new)
				.collect(Collectors.toList());
	}

	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		public SimpleOrderDto(Order order) {
			this.orderId = order.getId();
			this.name = order.getMember().getName(); // LAZY 초기화
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getMember().getAddress(); // LAZY 초기화
		}
	}
}
