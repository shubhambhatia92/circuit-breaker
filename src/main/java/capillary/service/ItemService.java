/*
READ ME
item service class in which circuit breaker is injected. Dummy items are already added
just to create successful request and exception is thrown deliberately to demonstrate failed request
Circuit breaker Counters of successfully and failed are incremented accordingly
 */

package capillary.service;
import capillary.models.Item;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private final CircuitBreaker circuitBreaker;
    private List<Item> items = new ArrayList<>();
    private Integer idCounter = 1;

    public ItemService(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
        createItems();
    }

    public void createItems() {
        items= List.of(
                new Item(1, "item 1", 10.2, 1000),
                new Item(2, "item 2", 20.1, 2000),
                new Item(3, "item 3", 30.0, 3000)
        );
    }
    /*
   This is marked as a unsuccessful request to demonstrate circuit breaker.
   Every api call will result in a unsuccessful request
   All other methods are considered to be successful request
    */
    public Item create(Item item) {
        try {
            item.setId(idCounter++);
            items.add(item);
            throw new RuntimeException();
            //return item;
        } catch (Exception e) {
            circuitBreaker.incrementFailedRequestCount();
            circuitBreaker.setSuccessfulRequestCountToZero();
            throw e;
        }
    }

    public List<Item> findAll() {
        try {
            circuitBreaker.incrementSuccessfulRequestCount();
            return items;
        } catch (Exception e) {
            circuitBreaker.incrementFailedRequestCount();
            circuitBreaker.setSuccessfulRequestCountToZero();
            throw e;
        }
    }

    public Item findById(Integer id) {
        try {
            circuitBreaker.incrementSuccessfulRequestCount();
            return items.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        }catch (Exception e){
            circuitBreaker.incrementFailedRequestCount();
            circuitBreaker.setSuccessfulRequestCountToZero();
         throw e;
        }
    }

    public Item update(Integer id, Item item) {
        try {


            Item existing = findById(id);
            if (existing == null) {
                throw new RuntimeException();
            }
            existing.setName(item.getName());
            existing.setPrice(item.getPrice());
            existing.setQuantity(item.getQuantity());
            circuitBreaker.incrementSuccessfulRequestCount();
            return existing;
        }catch (Exception e){
            circuitBreaker.incrementFailedRequestCount();
            circuitBreaker.setSuccessfulRequestCountToZero();
            throw e;
        }
    }

}
