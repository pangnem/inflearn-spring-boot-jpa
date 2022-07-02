package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String name;

	@Embedded
	private Address address;

	@JsonIgnore
	@OneToMany(mappedBy = "member") // order 테이블의 member 필드에 의해서 매핑됨을 알림(읽기 전용)
	private List<Order> orders = new ArrayList<>();
}
