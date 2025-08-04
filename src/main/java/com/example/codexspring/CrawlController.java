package com.example.codexspring;

import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawl")
public class CrawlController {

    private final HotelbedsCrawler crawler;

    public CrawlController(HotelbedsCrawler crawler) {
        this.crawler = crawler;
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<String> crawlHotel(@PathVariable String id) {
        Document doc = crawler.fetchPage("/hotel/" + id);
        return ResponseEntity.ok(doc.title());
    }
}
