package capillary;

import capillary.service.CircuitBreaker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CapillaryApplication {
	/* circuit breaker is initialized in the starting and
	 can be injected to any service to get the status of the service
	 */
	CircuitBreaker circuitBreaker = new CircuitBreaker();
	public static void main(String[] args) {
		SpringApplication.run(CapillaryApplication.class, args);
	}

}
