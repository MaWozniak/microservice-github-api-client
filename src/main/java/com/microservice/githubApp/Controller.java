package com.microservice.githubApp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {

    final private RestTemplate restTemplate;

    public Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${adress}")
    private String adress;

    @RequestMapping("/repositories/{owner}/{repository-name}")
    public OutData getData(@PathVariable(value = "owner") String name, @PathVariable(value = "repository-name") String repoName) {

        String urlString = adress + name + "/" + repoName;

        RepoData data = restTemplate.getForObject(urlString, RepoData.class);

        if (data == null) {
            throw new EmptyFieldException();
        }
        return new OutData(data.getFullName(), data.getDescription(), data.getCloneUrl(), Integer.parseInt(data.getStargazersCount()), data.getCreatedAt());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
            reason = "You should correct name Owner of Repository")
    @ExceptionHandler(EmptyFieldException.class)
    private void emptyNameException() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "Wrong Repo or Wrong User name")
    @ExceptionHandler(WrongRepoOrUserException.class)
    public void handlerWrongUserException() {
    }

}