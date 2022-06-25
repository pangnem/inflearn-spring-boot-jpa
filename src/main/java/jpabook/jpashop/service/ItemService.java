package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	@Transactional
	public void updateItem(Long itemId, Book book) {
		Item findItem = itemRepository.findOne(itemId);
		findItem.setPrice(book.getPrice());
		findItem.setName(book.getName());
		findItem.setStockQuantity(book.getStockQuantity());

		// 변경 감지가 일어나서 호출하지 않아도 된다!
//		itemRepository.save(findItem);
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
}
