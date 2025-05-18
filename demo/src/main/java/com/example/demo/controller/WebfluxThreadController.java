package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class WebfluxThreadController {

    private static final Logger log = LoggerFactory.getLogger(WebfluxThreadController.class);

    @GetMapping("/webflux/thread-test")
    public Mono<String> threadTest() {
        long threadIdBefore = Thread.currentThread().getId();

        return Mono.defer(() -> {
            long threadIdInside = Thread.currentThread().getId();

            return Mono.just("done")
                    .delayElement(Duration.ofMillis(500))
                    .map(result -> {
                        long threadIdAfter = Thread.currentThread().getId();

                        String json = String.format(
                                "{\n  \"before\": %d,\n  \"inside\": %d,\n  \"after\": %d\n}",
                                threadIdBefore, threadIdInside, threadIdAfter
                        );

                        System.out.println("==== START ====\n" + json + "\n==== END ====\n");

                        return json;
                    });
        });
    }
}
