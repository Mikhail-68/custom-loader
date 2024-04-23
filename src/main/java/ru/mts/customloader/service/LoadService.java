package ru.mts.customloader.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import ru.mts.customloader.dto.LoadDto;

import java.time.Duration;

@Service
@Slf4j
public class LoadService {

    public void load(LoadDto loadDto) {
        int rps = loadDto.getRps();
        String url = "http://" + loadDto.getHost() +
                ((loadDto.getEndpoint().charAt(0) == '/') ? loadDto.getEndpoint() : "/" + loadDto.getEndpoint());

        ConnectionProvider connectionProvider = ConnectionProvider.builder("myConnectionPool")
                .maxConnections(loadDto.getRps())
                .pendingAcquireMaxCount((int) (loadDto.getRps() * 1.3))
                .build();
        ReactorClientHttpConnector clientHttpConnector =
                new ReactorClientHttpConnector(HttpClient.create(connectionProvider));
        WebClient client = WebClient.builder().clientConnector(clientHttpConnector).build();

        log.info("Load started");
        log.debug("Load url: {}", url);

        Flux.interval(Duration.ofSeconds(1))
                .take(loadDto.getPeriodInSeconds())
                .flatMap(i -> Flux.range(0, rps)
                        .flatMap(j -> client.get().uri(url).retrieve().bodyToMono(String.class)))
                .subscribe(c -> log.debug("Iteration complete: {}", c),
                        e -> log.info(e.getMessage()),
                        () -> log.info("complete"));
    }
}