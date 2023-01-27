package capillary;

import capillary.models.Item;
import capillary.service.CircuitBreaker;
import capillary.service.ItemService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class CapillaryApplicationTests {


	@InjectMocks
	private ItemService itemService;

	@Mock
	private CircuitBreaker circuitBreaker;

	@Test
	public void testFindAllItem() {

		List<Item> result = itemService.findAll();

		Assert.assertEquals(circuitBreaker.getState(), "CLOSE");
	}

	@Test
	public void testCreateItemWithCloseCircuit() {

		Item item  = new Item(5, "item 1", 10.2, 1000);

		CircuitBreaker.setFailedRequestCount(new AtomicInteger(10));
		Item result = itemService.create(item);
		Assert.assertEquals(circuitBreaker.getState(), "OPEN");
	}

	@Test
	public void testCreateItemWithPartiallyOpenCircuit() {

		Item item  = new Item(5, "item 1", 10.2, 1000);

		CircuitBreaker.setFailedRequestCount(new AtomicInteger(6));
		CircuitBreaker.setSuccessfulRequestCount(new AtomicInteger(2));
		Item result = itemService.create(item);
		Assert.assertEquals(circuitBreaker.getState(), "PARTIALLY_OPEN");
	}



}
