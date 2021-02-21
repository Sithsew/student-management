package studentmanagement.application.transport.request.entities;

import lk.dialog.dds.common.request.validator.RequestEntityInterface;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class StudentRequestEntity implements RequestEntityInterface {

  @NotEmpty(message = "First Name cannot be null")
  @Size(min = 4, max = 20)
  private String firstName;

  @NotEmpty(message = "Last Name cannot be null")
  @Size(min = 4, max = 20)
  private String lastName;

  @NotEmpty(message = "Email cannot be null")
  @Email
  private String email;

  @NotNull(message = "Age cannot be null")
  @Min(value=5, message="Must be equal or greater than 5")
  @Max(value=20, message="Must be equal or less than 20")
  private Double age;

  @NotNull(message = "Grade cannot be null")
  @Min(value=1, message="Must be equal or greater than 1")
  @Max(value=13, message="Must be equal or less than 13")
  private Integer grade;
}
