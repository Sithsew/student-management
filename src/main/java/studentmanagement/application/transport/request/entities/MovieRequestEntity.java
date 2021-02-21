package studentmanagement.application.transport.request.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lk.dialog.dds.common.request.validator.RequestEntityInterface;
import lombok.Data;

@Data
public class MovieRequestEntity implements RequestEntityInterface {

  @NotEmpty
  @Size(min = 4, max = 7)
  private String title;
}
