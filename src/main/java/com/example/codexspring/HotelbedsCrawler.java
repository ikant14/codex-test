package com.example.codexspring;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

@Service
public class HotelbedsCrawler {

    private final WebClient webClient;
    private final WebClient apiClient;
    private final String apiKey;
    private final String apiSecret;

    public HotelbedsCrawler(WebClient.Builder builder,
                            @Value("${hotelbeds.api-key}") String apiKey,
                            @Value("${hotelbeds.secret}") String apiSecret) {
        this.webClient = builder.baseUrl("https://www.hotelbeds.com").build();
        this.apiClient = builder.baseUrl("https://api.test.hotelbeds.com/hotel-api/1.0").build();
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public Document fetchPage(String path) {
        String html = webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return Jsoup.parse(html);
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void crawl() {
        // Periodic crawl of a sample page; replace with real logic
        fetchPage("/");
    }

    public List<HotelOption> search(LocalDate checkIn, LocalDate checkOut, String country) {
        String signature = buildSignature();
        JsonNode json = apiClient.get()
                .uri(uriBuilder -> uriBuilder.path("/hotels")
                        .queryParam("checkIn", checkIn)
                        .queryParam("checkOut", checkOut)
                        .queryParam("country", country)
                        .build())
                .header("Api-Key", apiKey)
                .header("X-Signature", signature)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return mapHotels(json);
    }

    private String buildSignature() {
        long ts = Instant.now().getEpochSecond();
        String payload = apiKey + apiSecret + ts;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    private List<HotelOption> mapHotels(JsonNode json) {
        List<HotelOption> hotels = new ArrayList<>();
        if (json == null) {
            return hotels;
        }
        JsonNode array = json.path("hotels");
        for (JsonNode node : array) {
            String id = node.path("code").asText();
            String name = node.path("name").path("content").asText();
            BigDecimal price = node.path("minRate").decimalValue();
            hotels.add(new HotelOption(id, name, price));
        }
        return hotels;
    }
}
