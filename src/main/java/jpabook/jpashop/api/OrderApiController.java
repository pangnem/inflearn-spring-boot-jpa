package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;

	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1OrderList() {
		List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

		for (Order order : orders) {
			order.getMember().getName();
			order.getDelivery().getAddress();

			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.forEach(o -> o.getItem().getName());
		}

		return orders;
	}

	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2OrderList() {
		List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

		return orders.stream()
				.map(OrderDto::new)
				.collect(Collectors.toList());
	}


	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3OrderList() {
		List<Order> orders = orderRepository.findAllWithItem();

		return orders.stream()
				.map(OrderDto::new)
				.collect(Collectors.toList());
	}

	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersV3OrderList_page(
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "100") int limit
			) {
		List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

		return orders.stream()
				.map(OrderDto::new)
				.collect(Collectors.toList());
	}


	@Data
	private static class OrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItems;

		public OrderDto(Order order) {
			this.orderId = order.getId();
			this.name = order.getMember().getName();
			this.orderDate = order.getOrderDate();
			this.orderStatus = order.getStatus();
			this.address = order.getDelivery().getAddress();

			this.orderItems = order.getOrderItems().stream()
					.map(orderItem -> new OrderItemDto(orderItem))
					.collect(Collectors.toList());
		}
	}

	@Data
	static class OrderItemDto {

		private String itemName;
		private int orderPrice;
		private int count;

		public OrderItemDto(OrderItem orderItem) {
			this.itemName = orderItem.getItem().getName();
			this.orderPrice = orderItem.getOrderPrice();
			this.count = orderItem.getCount();
		}
	}
}
