package ru.practicum.statservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.practicum.statservice.dto.EndpointHitInputDto;
import ru.practicum.statservice.dto.EndpointHitResultDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatClient {
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String BASE_URL;
    private final HttpClient client;

    @Autowired
    public StatClient(Environment env) {
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(1000))
                .build();
        var uriPropertyName = "stat-server.url";
        if (env.containsProperty(uriPropertyName)) {
            this.BASE_URL = env.getProperty(uriPropertyName);
        } else {
            this.BASE_URL = "http://localhost:9090/";
        }
    }

    public void createHit(EndpointHitInputDto hit) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "hit"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(getJson(hit)))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }

    }

    public List<EndpointHitResultDto> getHitsWithParams(LocalDateTime start,
                                                    LocalDateTime end,
                                                    String[] uris,
                                                    Boolean unique) {

        try {
            var builder = new StringBuilder();
            builder.append("stats?start=");
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

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + builder.toString().replace(' ', '+')))
                    .GET()
                    .build();

            var stream = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(stream.body(), List.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private String getJson(Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);
    }
}
