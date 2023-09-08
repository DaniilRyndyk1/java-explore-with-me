package ru.practicum.statservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.statservice.dto.EndpointHitInputDto;
import ru.practicum.statservice.dto.EndpointHitResultDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class StatClient {
    private final WebClient webClient;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void createHit(EndpointHitInputDto hit) {
        webClient
                .post()
                .uri("/hit")
                .body(BodyInserters.fromValue(hit))
                .retrieve();
    }

    public Mono<List<EndpointHitResultDto>> getHitsWithParams(LocalDateTime start,
                                                              LocalDateTime end,
                                                              String[] uris,
                                                              Boolean unique) {
        var builder = new StringBuilder();
        builder.append("/stats?start=");
        builder.append(start.format(formatter));
        builder.append("&end=");
        builder.append(end.format(formatter));
        if (uris != null) {
            builder.append("&uris=");
            for (String uri : uris) {
                builder.append(uri);
                builder.append(",");
            }
        }

        if (unique != null) {
            builder.append("&unique=");
            builder.append(unique);
        }

        return webClient
                .get()
                .uri(builder.toString())
                .retrieve()
                .bodyToFlux(EndpointHitResultDto.class)
                .collectList();
    }
}
