package jpabook.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@Transactional
	@Rollback(false) // 트랜잭션 롤백하지 않고 DB에 저장
	void testMember() {
		Member member = new Member();
		member.setUsername("memberA");

		Long savedId = memberRepository.save(member);
		Member findMember = memberRepository.find(savedId);

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);

		System.out.println("findMember == member = " + (findMember == member));
	}
}
