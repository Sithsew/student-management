package studentmanagement.application.handlers;

import java.util.Map;
import lk.dialog.dds.common.exception.types.application.HandlerException;
import lk.dialog.dds.common.request.validator.RequestEntityValidator;
import lk.dialog.dds.common.response.transformer.ResponseEntityTransformer;
import studentmanagement.application.transport.request.entities.MovieRequestEntity;
import studentmanagement.application.transport.response.transformers.MovieTransformer;
import studentmanagement.application.transport.response.transformers.ResourceIdentifierTransformer;
import studentmanagement.domain.entities.Movie;
import studentmanagement.domain.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j // logger (generates a class property of name `log`)
public class MovieHandler {

  @Autowired
  private RequestEntityValidator validator;

  @Autowired
  private ResponseEntityTransformer transformer;

  @Autowired
  private MovieService movieService;

  /**
   * Handle get all movies request
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> get(ServerRequest request) {

    // logging
    log.info("Lombok logger");

    return ServerResponse.ok().body(transformer.transform(movieService.getAllMovies(),
        new MovieTransformer()),
        Map.class);
  }

  /**
   * Handle get movie by id
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> getById(ServerRequest request) {

    return ServerResponse.ok()
        .body(transformer.transform(movieService.getMovieById(request.pathVariable("id")),
            new MovieTransformer()),
            Map.class);
  }

  /**
   * Handle get rating of a movie.
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> getRating(ServerRequest request) {

    // map
    Movie movie = new Movie();
    movie.setId(request.pathVariable("id"));

    return ServerResponse.ok()
        .body(this.movieService.getRating(movie), Double.class);
  }

  /**
   * Handle creating a new movie entry
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> add(ServerRequest request) {

    return ServerResponse
        .status(HttpStatus.CREATED)
        .body(transformer.transform(request.bodyToMono(MovieRequestEntity.class)
            .onErrorMap(e -> new HandlerException(e.getMessage()))
            .flatMap(payload -> {

              // validate
              this.validator.validate(payload);

              // map
              Movie movie = new Movie();
              movie.setTitle(payload.getTitle());

              return this.movieService.addMovie(movie);
            }), new ResourceIdentifierTransformer()), Map.class);
  }

  /**
   * Handle updating of an existing movie
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> edit(ServerRequest request) {

    return request.bodyToMono(MovieRequestEntity.class)
        .flatMap(payload -> {

          // validate
          this.validator.validate(payload);

          // map
          Movie movie = new Movie();
          movie.setId(request.pathVariable("id"));
          movie.setTitle(payload.getTitle());

          return this.movieService.edit(movie);
        }).then(ServerResponse.noContent().build());
  }

  /**
   * Handle deletion of an existing movie
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> delete(ServerRequest request) {

    return this.movieService.delete(request.pathVariable("id"))
        .then(ServerResponse.noContent().build());
  }
}
