## codex-test

Spring Boot sample that demonstrates a basic crawler for [hotelbeds.com](https://www.hotelbeds.com).

### Sample API

The application exposes a simple search endpoint returning placeholder hotel options:

```
GET /search/hotels?checkIn=2024-01-01&checkOut=2024-01-05&country=ES
```

The response is a JSON array with hotel id, name and price fields.
