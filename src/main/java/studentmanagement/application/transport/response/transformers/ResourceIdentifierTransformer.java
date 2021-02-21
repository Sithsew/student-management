package studentmanagement.application.transport.response.transformers;

import java.util.HashMap;
import java.util.Map;
import lk.dialog.dds.common.response.transformer.ResponseEntityInterface;

public class ResourceIdentifierTransformer implements ResponseEntityInterface<String> {

  @Override
  public Map<String, Object> transform(String entity) {

    Map<String, Object> mapping = new HashMap<>();

    mapping.put("id", entity);

    return mapping;

  }
}
