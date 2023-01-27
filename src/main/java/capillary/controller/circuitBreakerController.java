/*
READ ME
Controller to get the status of service.This can be modified accordingly in case we have multiple services
and we can fetch status of different services
 */

package capillary.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import capillary.service.CircuitBreaker;
import capillary.constants.ApplicationConstants;

@RestController
@RequestMapping("/status")
public class circuitBreakerController {
    @Autowired
    private CircuitBreaker circuitBreaker;

    @GetMapping
    public String getServiceStatus() {
        if (circuitBreaker.getState()==ApplicationConstants.OPEN) {
            return "Service is unavailable";
        } else
            if(circuitBreaker.getState()==ApplicationConstants.CLOSED)
            return "Service is available";
        else
        return "Service is partially available";
    }
}
