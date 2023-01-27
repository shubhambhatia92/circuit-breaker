/*
READ ME
This is a independent circuit breaker class with three states - 1)CLOSED,OPEN,PARTIALLY_OPEN
The maximum success request number after which service is considered available is defined in applicationConstants file
The maximum partial request number after which service is considered partially available is defined in applicationConstants file
The maximum failed request number after which service is considered unavailable is defined in applicationConstants file
This class is designed in a way to keep it thread safe
 */

package capillary.service;
import capillary.constants.ApplicationConstants;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CircuitBreaker {

    public enum CircuitBreakerState {
        CLOSED,
        OPEN,
        PARTIALLY_OPEN
    }

    private final int MAX_FAILED_REQUESTS;
    private final int MAX_PARTIAL_OPEN_REQUESTS;
    private final int MAX_SUCCESSFUL_REQUESTS;

    private CircuitBreakerState state;


    public AtomicInteger getFailedRequestCount() {
        return failedRequestCount;
    }

    public AtomicInteger getSuccessfulRequestCount() {
        return successfulRequestCount;
    }

    private AtomicInteger failedRequestCount;

    public void setFailedRequestCount(AtomicInteger failedRequestCount) {
        this.failedRequestCount = failedRequestCount;
    }

    public void setSuccessfulRequestCount(AtomicInteger successfulRequestCount) {
        this.successfulRequestCount = successfulRequestCount;
    }

    private AtomicInteger successfulRequestCount;

    private final Object lock;
/*
    constructor of circuit breaker.Using lock and atomic Integer to keep it thread safe
    and initialising variables
 */
    public CircuitBreaker() {
        state = CircuitBreakerState.CLOSED;
        failedRequestCount = new AtomicInteger(0);
        successfulRequestCount = new AtomicInteger(0);
        lock = new Object();
        this.MAX_FAILED_REQUESTS = ApplicationConstants.circuitBreakerMaxFailed;
        this.MAX_SUCCESSFUL_REQUESTS = ApplicationConstants.circuitBreakerMaxSuccess;
        this.MAX_PARTIAL_OPEN_REQUESTS = ApplicationConstants.circuitBreakerPartialFailed;
    }


    /*
        This Function is used to keep a track of failedRequestCount
    */
    public void incrementFailedRequestCount() {
        synchronized (lock) {
            failedRequestCount.incrementAndGet();
            checkCircuitBreakerState();
        }
    }
    /*
        This Function is used to keep a track of SuccessfulRequestCount
    */
    public void incrementSuccessfulRequestCount() {
        synchronized (lock) {
            successfulRequestCount.incrementAndGet();
            checkCircuitBreakerState();
        }
    }
    /*
        This Function is the heart of service. 2 scenario's which are handled are
        1)service goes from available to partial available and then unavailable
        2)service goes from unavailable to partial available and then available
        This can be tested and observed as well
     */
    private void checkCircuitBreakerState() {
        synchronized (lock) {

            if (successfulRequestCount.get() > MAX_SUCCESSFUL_REQUESTS) {
                state = CircuitBreakerState.CLOSED;
                failedRequestCount.set(0);
                successfulRequestCount.set(0);
            } else {
                if (failedRequestCount.get() > MAX_FAILED_REQUESTS) {
                    state = CircuitBreakerState.OPEN;
                }
                if(failedRequestCount.get() > MAX_PARTIAL_OPEN_REQUESTS && failedRequestCount.get()<=MAX_FAILED_REQUESTS){
                    state = CircuitBreakerState.PARTIALLY_OPEN;
                }
                if(successfulRequestCount.get()>=MAX_PARTIAL_OPEN_REQUESTS && failedRequestCount.get()>0){
                    state = CircuitBreakerState.PARTIALLY_OPEN;
                }
            }
        }
    }
    /*
    To get the state of service ie - available,unavailable,partially available
    */
    public String getState() {
        return state.name();
    }

    /*
    It is used to maintain the continuous Count of successful request to handle different scenario
    and reset it if it breaks
    */
    public void setSuccessfulRequestCountToZero() {
        this.successfulRequestCount = new AtomicInteger(0);;
    }

}



