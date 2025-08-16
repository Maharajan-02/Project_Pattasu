package com.pattasu.service.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class MailjetHttpMailService {

    private final String apiKey;
    private final String apiSecret;
    private final String fromEmail;
    private final String fromName;
    private final HttpClient http = HttpClient.newHttpClient();

    public MailjetHttpMailService(
            @Value("${MJ_APIKEY_PUBLIC}") String apiKey,
            @Value("${MJ_APIKEY_PRIVATE}") String apiSecret,
            @Value("${MAIL_FROM}") String fromEmail,
            @Value("${MAIL_FROM_NAME:Suriya Pyro Park}") String fromName) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public void send(String toEmail, String subject, String html) throws Exception {
        JSONObject payload = new JSONObject()
                .put("Messages", new JSONArray().put(new JSONObject()
                        .put("From", new JSONObject()
                                .put("Email", fromEmail)
                                .put("Name", fromName))
                        .put("To", new JSONArray().put(new JSONObject().put("Email", toEmail)))
                        .put("Subject", subject)
                        .put("HTMLPart", html)
                ));

        String basic = Base64.getEncoder()
                .encodeToString((apiKey + ":" + apiSecret).getBytes(StandardCharsets.UTF_8));

        HttpRequest req = HttpRequest.newBuilder(URI.create("https://api.mailjet.com/v3.1/send"))
                .header("Authorization", "Basic " + basic)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
            throw new RuntimeException("Mailjet error " + resp.statusCode() + ": " + resp.body());
        }
    }
}
