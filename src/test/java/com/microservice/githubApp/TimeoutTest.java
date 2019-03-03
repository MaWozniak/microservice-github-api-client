package com.microservice.githubApp;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class TimeoutTest {

    @Autowired
    private Controller controller;

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(8089);

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    @Test(expected = ResourceAccessException.class)
    public void serverTimeoutTest() {

        stubFor(get(urlEqualTo("/repos/MaWozniak/chess-engine-java"))
                .willReturn(aResponse()
                        .withFixedDelay(3000)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"full_name\":\"MaWozniak/chess-engine-java\",\"description\":\"Simply chess engine for OOP exercise\"," +
                                "\"clone_url\":\"https://github.com/MaWozniak/chess-engine-java.git\",\"stargazers_count\":1,\"created_at\":\"2019-01-12T15:08:00Z\"}")
                ));

        controller.getData("MaWozniak", "chess-engine-java");
    }

}