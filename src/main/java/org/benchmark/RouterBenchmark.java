package org.benchmark;

import org.routing.OSCRouter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class RouterBenchmark implements CommandLineRunner {

    private final OSCRouter router;

    public RouterBenchmark(OSCRouter router) {
        this.router = router;
    }

    @Override
    public void run(String... args) throws Exception {
        benchmarkInitialization();
        benchmarkNavigationPerformance();
        benchmarkConcurrency();
    }

    // Measure initialization time
    private void benchmarkInitialization() {
        long startTime = System.nanoTime();
        router.initialize();
        long endTime = System.nanoTime();
        System.out.println("Initialization Time: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    // Measure navigation performance for a single request
    private void benchmarkNavigationPerformance() {
        try {
            String testNamespace = "/light/x1y2/test";
            long startTime = System.nanoTime();
            router.navigate(testNamespace, "hallo"); // Adjust params as needed
            long endTime = System.nanoTime();
            System.out.println("Navigation Time: " + (endTime - startTime) / 1_000_000 + " ms");
        } catch (Exception e) {
            System.err.println("Error during navigation benchmark: " + e.getMessage());
        }
    }

    // Measure performance under concurrent load
    private void benchmarkConcurrency() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String testNamespace = "/light/x1y2/test";

        long startTime = System.nanoTime();

        for (int i = 0; i < 100000; i++) { // Number of requests to simulate
            executor.submit(() -> {
                try {
                    router.navigate(testNamespace, "hallo"); // Adjust params as needed
                } catch (Exception e) {
                    System.err.println("Error during concurrent navigation: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        long endTime = System.nanoTime();
        System.out.println("Concurrent Navigation Time: " + (endTime - startTime) / 1_000_000 + " ms for 100000 requests");
    }
}