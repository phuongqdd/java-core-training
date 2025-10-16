package com.example.wikimedia.producer_2.stream;

import com.example.wikimedia.producer_2.producer.WikimediaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class WikimediaStreamConsumer {

    private final WebClient webClient;
    private final WikimediaProducer wikimediaProducer;

    public WikimediaStreamConsumer(WebClient.Builder webClientBuilder, WikimediaProducer wikimediaProducer) {
        // Build WebClient với base URL
        this.webClient = webClientBuilder.baseUrl("https://stream.wikimedia.org/v2").build();
        this.wikimediaProducer = wikimediaProducer;
    }

    // Hàm consume stream và publish lên Kafka
    public void consumeStreamAndPublish() {
        Flux<String> eventStream = webClient.get()
                .uri("/stream/recentchange")
                .retrieve()
                .bodyToFlux(String.class);

        // Subscribe với xử lý từng event
        eventStream.subscribe(
                event -> {
                    log.info("Received event: {}", event);   // log dữ liệu
                    wikimediaProducer.sendMessage(event);     // gửi lên Kafka
                },
                error -> log.error("Error in Wikimedia stream", error),   // handle lỗi
                () -> log.info("Wikimedia stream completed")              // khi stream kết thúc
        );
    }
}
