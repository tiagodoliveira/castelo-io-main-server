package io.castelo.main_server.client.github.response;

public record GitHubEmailResponse(String email, boolean primary, boolean verified) {}
