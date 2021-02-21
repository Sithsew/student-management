package studentmanagement.application.transport.response.transformers;

import java.util.HashMap;
import java.util.Map;
import lk.dialog.dds.common.response.transformer.ResponseEntityInterface;
import studentmanagement.domain.entities.Movie;

public class MovieTransformer implements ResponseEntityInterface<Movie> {

  @Override
  public Map<String, Object> transform(Movie movie) {

    Map<String, Object> mapping = new HashMap<>();

    mapping.put("transformed_id", movie.getId());
    mapping.put("transformed_title", movie.getTitle());

    return mapping;
  }
}
