package studentmanagement.externals.webclients;

import studentmanagement.domain.boundary.webclients.IMDBClientInterface;
import studentmanagement.domain.entities.Movie;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Component
public class IMDBClient implements IMDBClientInterface {

  @Autowired
  private MeterRegistry meterRegistry;

  @Override
  public Mono<Double> getRating(Movie movie) {

    // https://stackoverflow.com/questions/55085767/get-response-time-of-web-client-web-flux
    
    // Mono<String> resultMono = Mono.subscriberContext()
    //     .flatMap(context -> webClient.get()
    //             .uri("/foo")
    //             .retrieve()
    //             .bodyToMono(String.class)
    //             .doOnNext(ignored -> log.info("Execution duration is {} ms",
    //                     System.currentTimeMillis() - context.<Long>get("stopWatch"))))
    //     .subscriberContext(context -> context.put("stopWatch",
    //             System.currentTimeMillis()));

    // register metrics
    Timer timer = this.meterRegistry.timer("imdb.request.time", "title", movie.getId());
    long start = System.currentTimeMillis();

    // call REST API

    // set metrics
    timer.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);

    return Mono.just(3.5);
  }
}
