package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class Delivery {

	@Id
	@GeneratedValue
	@Column(name = "delivery_id")
	private Long id;

	@OneToOne(mappedBy = "delivery")
	private Order oder;

	@Embedded
	private Address address;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus status; // READY, COMP

}