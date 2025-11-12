package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Value("${app.restTemplate.connectTimeout:2000}")
    private int connectTimeout;

    @Value("${app.restTemplate.readTimeout:3000}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        //System.out.println("connectTimeout = " + connectTimeout);
        //System.out.println("readTimeout = " + readTimeout);
        return builder
                .setConnectTimeout(Duration.ofMillis(connectTimeout))  // connection timeout
                .setReadTimeout(Duration.ofMillis(readTimeout))     // read timeout
                .build();
    }

    //⚙️ Your Config Recap
    //resilience4j:
    //  circuitbreaker:
    //    instances:
    //      order-service:
    //        registerHealthIndicator: true
    //        eventConsumerBufferSize: 10
    //        failureRateThreshold: 50
    //        minimumNumberOfCalls: 5
    //        automaticTransitionFromOpenToHalfOpenEnabled: true
    //        waitDurationInOpenState: 5s
    //        permittedNumberOfCallsInHalfOpenState: 3
    //        slidingWindowSize: 5
    //        slidingWindowType: COUNT_BASED
    //
    //🧩 Let’s explain each key property
    //🔹 registerHealthIndicator: true
    //
    //Exposes the circuit breaker’s health as part of Spring Boot Actuator /actuator/health.
    //
    //So /actuator/health will include something like:
    //
    //{
    //  "status": "UP",
    //  "components": {
    //    "order-service": {
    //      "status": "CIRCUIT_CLOSED"
    //    }
    //  }
    //}
    //
    //
    //Helps external monitoring tools (Prometheus, Grafana, etc.) track breaker status.
    //
    //🔹 eventConsumerBufferSize: 10
    //
    //The circuit breaker emits events (success, failure, state transition).
    //
    //This property defines how many events are buffered for monitoring/listening purposes.
    //
    //For example, if you attach an event listener (or use /actuator/circuitbreakers), it can see up to the last 10 events.
    //
    //🔹 failureRateThreshold: 50
    //
    //The percentage of failed calls (out of the last N calls) that will trip the circuit to Open.
    //
    //So if failureRateThreshold: 50, and 3 out of 5 calls fail → failure rate = 60% → circuit opens.
    //
    //💡 Formula:
    //
    //failureRate = (failed_calls / total_calls_in_window) * 100
    //
    //🔹 minimumNumberOfCalls: 5
    //
    //The breaker doesn’t start calculating failure rate until at least 5 calls have been made.
    //
    //This avoids overreacting to random 1 or 2 initial failures.
    //
    //✅ Example:
    //If only 3 calls have been made and 2 failed → failure rate = 66% but still below minimumNumberOfCalls, so circuit stays closed.
    //
    //🔹 automaticTransitionFromOpenToHalfOpenEnabled: true
    //
    //Enables automatic recovery attempts.
    //
    //After the circuit has stayed open for the configured waitDurationInOpenState, it automatically moves to Half-Open to test if the service has recovered.
    //
    //If false → circuit remains open until you manually call a special reset method.
    //
    //✅ Typically you keep it true.
    //
    //🔹 waitDurationInOpenState: 5s
    //
    //Once the circuit opens, it will stay open for 5 seconds before trying to move to Half-Open.
    //
    //During this time, all calls are blocked immediately (fallbacks triggered).
    //
    //🔹 permittedNumberOfCallsInHalfOpenState: 3
    //
    //When Half-Open, only 3 test calls are allowed through to the service.
    //
    //If all/some succeed — circuit closes again.
    //
    //If any fail — circuit reopens immediately.
    //
    //✅ Example:
    //
    //Test call #	Result	Circuit Action
    //1	Success	Keep testing
    //2	Success	Keep testing
    //3	Success	→ Circuit closes (service healthy again)
    //1	Failure	→ Circuit reopens immediately
    //🔹 slidingWindowSize: 5
    //
    //The number of recent calls the circuit breaker uses to calculate the failure rate.
    //
    //Here, it checks the last 5 calls.
    //
    //So every time a call finishes, the stats window slides — oldest record drops, newest adds.
    //
    //🔹 slidingWindowType: COUNT_BASED
    //
    //There are two types of windows:
    //
    //COUNT_BASED: Breaker measures last N calls (e.g. 5 calls).
    //
    //TIME_BASED: Breaker measures all calls in last N seconds.
    //
    //COUNT_BASED is more predictable in small-load scenarios.
    //
    //🚦 Now, how the states transition
    //State	Description	What happens next
    //CLOSED	Normal operation — all requests flow. Failures are counted in the window.	If failure rate > threshold (50%) → move to OPEN
    //OPEN	Circuit is broken — all requests fail immediately (fallback triggered).	After waitDurationInOpenState (5s), auto transition → HALF-OPEN
    //HALF-OPEN	Limited test calls allowed (permittedNumberOfCallsInHalfOpenState=3).	If all succeed → CLOSED. If any fail → OPEN again
    //CLOSED again	Normal operation resumes.	Keeps monitoring with sliding window again.
    //🧠 Example in your setup
    //Event	State	What happens
    //Start	CLOSED	Tracking begins
    //5 calls made → 3 failed → 60% failures	→ failureRate > 50% → transition to OPEN
    //Next 5s	OPEN	All calls blocked, fallback instantly
    //After 5s	HALF-OPEN	Allows 3 test calls
    //If 3 succeed	→ CLOSED
    //If any of 3 fail	→ OPEN again
    //🔍 View in Actuator
    //
    //Once running, you can check:
    //
    //GET /actuator/health
    //GET /actuator/resilience4j-circuitbreakers
    //
    //
    //Example output:
    //
    //{
    //  "order-service": {
    //    "state": "HALF_OPEN",
    //    "failureRate": 40.0,
    //    "failureRateThreshold": 50.0,
    //    "numberOfBufferedCalls": 5,
    //    "numberOfFailedCalls": 2,
    //    "slowCallRate": 0.0
    //  }
    //}
    //
    //🧭 TL;DR Summary Table
    //Property	Meaning	Example Value
    //registerHealthIndicator	Show breaker state in health	✅
    //failureRateThreshold	% failures to open circuit	50
    //minimumNumberOfCalls	Calls before evaluating	5
    //waitDurationInOpenState	Time to stay open	5s
    //automaticTransitionFromOpenToHalfOpenEnabled	Auto recover	✅ true
    //permittedNumberOfCallsInHalfOpenState	Test calls allowed	3
    //slidingWindowSize	Sample size for failure rate	5
    //slidingWindowType	Count- or time-based	COUNT_BASED
    //When threshold crosses	Circuit goes from Closed → Open
    //After wait duration	Circuit goes Open → Half-Open
    //If Half-Open calls succeed	Circuit closes
    //If they fail	Circuit reopens
}
