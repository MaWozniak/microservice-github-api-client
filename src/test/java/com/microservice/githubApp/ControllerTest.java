package com.microservice.githubApp;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

public class ControllerTest {

    //UnitTests

    private RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);

    @Test(expected = EmptyFieldException.class)
    public void emptyUser() throws Exception {
        Controller controller = new Controller(mockRestTemplate);
        controller.getData(null, "chess-engine-java");
    }

    @Test(expected = EmptyFieldException.class)
    public void emptyRepo() throws Exception {
        Controller controller = new Controller(mockRestTemplate);
        controller.getData("MaWozniak", null);
    }

    @Test(expected = EmptyFieldException.class)
    public void emptyRequest() throws Exception {
        Controller controller = new Controller(mockRestTemplate);
        controller.getData(null, null);
    }

    @Test
    public void OutDataIsProduced() {
        Mockito.when(mockRestTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenReturn(new RepoData("Program", "simply program", "http://net.net/", "1", "2019/10/10"));
        Controller controller = new Controller(mockRestTemplate);
        OutData data = controller.getData("MaWozniak", "chess-engine-java");

        Assert.assertEquals("Program", data.getFullName());
        Assert.assertEquals("simply program", data.getDescription());
        Assert.assertEquals("http://net.net/", data.getCloneUrl());
        Assert.assertEquals(1, data.getStars());
        Assert.assertEquals("2019/10/10", data.getCreatedAt());

    }

}