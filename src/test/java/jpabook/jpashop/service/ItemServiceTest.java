package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceTest {

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	void 상품_저장() {
		Item item = new Movie();

		itemService.saveItem(item);

		assertThat(itemRepository.findAll()).hasSize(1);
	}

	@Test
	void 저장된_상품_조회() {
		Item item = new Movie();

		itemService.saveItem(item);
		Item findItem = itemService.findOne(item.getId());

		assertThat(item).isEqualTo(findItem);
	}
}
