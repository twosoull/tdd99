package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.UserPointDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class PointServiceConcurrencyTest {

    @Autowired
    PointService pointService;

    @Test
    void point_and_charge_and_use(){
        //give
        pointService.charge(1L, 10000);

        //when
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    pointService.use(1L, 1000);
                }),
                CompletableFuture.runAsync(() -> {
                    pointService.charge(1L, 400);
                }),
                CompletableFuture.runAsync(() -> {
                    pointService.use(1L, 100);
                })
        ).join();

        UserPointDto point = pointService.point(1L);
        Assertions.assertEquals(point.getPoint(),10000-1000+400-100);
    }


    //한번에 100ㅇ명이
    @Test
    void Charge_with_100_request() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        int point = 100;
        for(int i = 0; i< threadCount; i++){
            executorService.submit(() -> {
                try {
                    pointService.charge(1L, point);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        //then
        UserPointDto point1 = pointService.point(1L);
        Assertions.assertEquals(100*100, point1.getPoint())  ;
    }

    //포인트 부족으로 97번째에서 예외가 터져야한다.
    @Test
    void use_with_100_request_97throw() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorAt = new AtomicInteger(-1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicBoolean hasErrorOccurred = new AtomicBoolean(false); // 예외 발생 여부 추적

        //when
        pointService.charge(1L, 9700);
        int point = 100;

        for(int i = 0; i< threadCount; i++){
            int currentIndex = i;
            executorService.submit(() -> {
                try {
                    if (!hasErrorOccurred.get()) {
                        pointService.use(1L, point);
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    if (errorAt.compareAndSet(-1, currentIndex)) {
                        hasErrorOccurred.set(true); // 예외 발생 표시
                        e.printStackTrace();
                        executorService.shutdownNow();  // 예외 발생 시 즉시 모든 작업 중단
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }

        //then
        Assertions.assertEquals(97, errorAt.get())  ;
    }



}
