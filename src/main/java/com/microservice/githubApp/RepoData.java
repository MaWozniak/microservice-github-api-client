package com.microservice.githubApp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepoData {

    final private String fullName;
    final private String description;
    final private String cloneUrl;
    final private String stargazersCount;
    final private String createdAt;

    public RepoData(@JsonProperty("full_name") String fullName, @JsonProperty("description") String description, @JsonProperty("clone_url") String cloneUrl,
                    @JsonProperty("stargazers_count") String stargazersCount, @JsonProperty("created_at") String createdAt) {
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stargazersCount = stargazersCount;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public String getStargazersCount() {
        return stargazersCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
