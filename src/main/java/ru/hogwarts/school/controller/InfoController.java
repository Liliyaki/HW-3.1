package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class InfoController {
    @Value("${server.port}")
    private int port;

    @GetMapping("/port")
    public int getPort() {
        return port;
    }
    @GetMapping("/sum-optimized")
    public long getSumOptimized() {

        long startTime = System.currentTimeMillis();

        long sum = Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0L, Long::sum);

        long endTime = System.currentTimeMillis();

        return sum;
    }
}
