package com.example.codexspring;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final HotelbedsCrawler crawler;

    public SearchController(HotelbedsCrawler crawler) {
        this.crawler = crawler;
    }

    @GetMapping("/hotels")
    public List<HotelOption> search(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam String country) {
        return crawler.search(checkIn, checkOut, country);
    }
}
