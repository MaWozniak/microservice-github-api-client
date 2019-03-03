package com.microservice.githubApp;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class E2ETest {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(8089);

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    @Test
    public void E2E() {

        stubFor(get(urlEqualTo("/repos/MaWozniak/chess-engine-java"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"full_name\":\"MaWozniak/chess-engine-java\",\"description\":\"Simply chess engine for OOP exercise\"," +
                                "\"clone_url\":\"https://github.com/MaWozniak/chess-engine-java.git\",\"stargazers_count\":1,\"created_at\":\"2019-01-12T15:08:00Z\"}")
                ));

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://localhost:8080/repositories/MaWozniak/chess-engine-java", String.class);
        Assert.assertEquals("{\"fullName\":\"MaWozniak/chess-engine-java\",\"description\":\"Simply chess engine for OOP exercise\"," +
                "\"cloneUrl\":\"https://github.com/MaWozniak/chess-engine-java.git\",\"stars\":1,\"createdAt\":\"2019-01-12T15:08:00Z\"}", result);

    }

    /*

    Non functional requirements - should be able to serve 20 requests per second:

     */

    @Test
    public void Handled20RPS() throws Exception {
        int nThreads = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        stubFor(get(urlEqualTo("/repos/MaWozniak/chess-engine-java"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"full_name\":\"MaWozniak/chess-engine-java\",\"description\":\"Simply chess engine for OOP exercise\"," +
                                "\"clone_url\":\"https://github.com/MaWozniak/chess-engine-java.git\",\"stargazers_count\":1,\"created_at\":\"2019-01-12T15:08:00Z\"}")
                ));

        AtomicInteger count = new AtomicInteger();

        RestTemplate restTemplate = new RestTemplate();

        for (int i = 0; i < nThreads; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String result = restTemplate.getForObject("http://localhost:8080/repositories/MaWozniak/chess-engine-java", String.class);
                    Assert.assertEquals("{\"fullName\":\"MaWozniak/chess-engine-java\",\"description\":\"Simply chess engine for OOP exercise\"," +
                            "\"cloneUrl\":\"https://github.com/MaWozniak/chess-engine-java.git\",\"stars\":1,\"createdAt\":\"2019-01-12T15:08:00Z\"}", result);
                    count.incrementAndGet();
                }
            });
        }

        Thread.sleep(1000);

        Assert.assertEquals(nThreads, count.get());
    }
}