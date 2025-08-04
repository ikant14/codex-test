package com.example.codexspring;

import java.math.BigDecimal;

/**
 * Simple DTO representing a hotel offer.
 */
public record HotelOption(String id, String name, BigDecimal price) {
}
