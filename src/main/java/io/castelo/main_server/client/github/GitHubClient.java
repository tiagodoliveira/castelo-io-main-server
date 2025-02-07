package io.castelo.main_server.client.github;

import io.castelo.main_server.client.FeignConfig;
import io.castelo.main_server.client.github.response.GitHubEmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "github", url = "https://api.github.com", configuration = FeignConfig.class)
public interface GitHubClient {

    @GetMapping("/user/emails")
    List<GitHubEmailResponse> getEmails(@RequestHeader("Authorization") String accessToken);
}
