package io.castelo.main_server.client.github;

import io.castelo.main_server.client.github.response.GitHubEmailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubService {

    private final GitHubClient gitHubClient;

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public String fetchEmailFromGitHub(String accessToken) {
        List<GitHubEmailResponse> emails = gitHubClient.getEmails("token " + accessToken);

        if (emails != null && !emails.isEmpty()) {
            return emails.stream()
                    .filter(email -> email.primary() && email.verified())
                    .map(GitHubEmailResponse::email)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
