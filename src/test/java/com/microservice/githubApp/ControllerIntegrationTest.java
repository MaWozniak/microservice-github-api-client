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

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:test.properties")

public class ControllerIntegrationTest {

    @Autowired
    private Controller controller;

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(8089);

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    @Test(expected = WrongRepoOrUserException.class)
    public void thereNoUserOrRepos() throws Exception {

        stubFor(get(urlEqualTo("/repos/ghfdkfdh/ghfdkfdh"))
                .willReturn(aResponse()
                        .withStatus(404)
                ));

        controller.getData("ghfdkfdh", "ghfdkfdh");
    }

    @Test(expected = ServerErrorException.class)
    public void ServerError() throws Exception {

        stubFor(get(urlEqualTo("/repos/ghfdkfdh/ghfdkfdh"))
                .willReturn(aResponse()
                        .withStatus(500)
                ));

        controller.getData("ghfdkfdh", "ghfdkfdh");
    }

}