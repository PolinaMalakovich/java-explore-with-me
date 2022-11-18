package ru.practicum.explorewithme.ewmservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.dto.hit.HitDto;
import ru.practicum.explorewithme.dto.hit.Stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.explorewithme.ewmservice.EwmConfiguration.DATE_TIME_FORMATTER;

@Component
public class EwmClient {
    private final RestTemplate rest;
    private final String apiBase;

    @Autowired
    public EwmClient(@Value("${ewm.stats.server.schema}") String schema,
                     @Value("${ewm.stats.server.host}") String host,
                     @Value("${ewm.stats.server.port}") int port,
                     RestTemplateBuilder builder) {
        this.rest = builder.build();
        this.apiBase = schema + "://" + host + ":" + port;
    }

    private static <T> ResponseEntity<T> prepareResponse(final ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    public void hit(HitDto hitDto) {
        post("/hit", hitDto);
    }

    public List<Stats> stats(final LocalDateTime start,
                             final LocalDateTime end,
                             final Iterable<String> uris,
                             final boolean unique) {
        return get(
            "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
            Map.ofEntries(
                Map.entry("start", DATE_TIME_FORMATTER.format(start)),
                Map.entry("end", DATE_TIME_FORMATTER.format(end)),
                Map.entry("uris", uris),
                Map.entry("unique", String.valueOf(unique))
            )
        );
    }

    private <T, R> R post(final String path, final T body) {
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    private <R> R get(final String path, @Nullable final Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    private <T, R> R makeAndSendRequest(final HttpMethod method,
                                        final String path,
                                        @Nullable final Map<String, Object> parameters,
                                        @Nullable final T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<R> serverResponse;
        if (parameters != null) {
            serverResponse =
                rest.exchange(getFullPath(path), method, requestEntity, new ParameterizedTypeReference<R>() {
                }, parameters);
        } else {
            serverResponse =
                rest.exchange(getFullPath(path), method, requestEntity, new ParameterizedTypeReference<R>() {
                });
        }
        return prepareResponse(serverResponse).getBody();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }

    private String getFullPath(final String path) {
        return apiBase + path;
    }
}
