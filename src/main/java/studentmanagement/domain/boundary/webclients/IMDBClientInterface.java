package studentmanagement.domain.boundary.webclients;

import studentmanagement.domain.entities.Movie;
import reactor.core.publisher.Mono;

public interface IMDBClientInterface {

  Mono<Double> getRating(Movie movie);
}
