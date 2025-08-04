package com.example.codexspring;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

@Service
public class HotelbedsCrawler {

    private final WebClient webClient;

    public HotelbedsCrawler(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://www.hotelbeds.com")
                .build();
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
        // Placeholder implementation; in a real crawler this would query Hotelbeds APIs
        return List.of(new HotelOption("1", "Sample Hotel in " + country, BigDecimal.valueOf(100)));
    }
}
