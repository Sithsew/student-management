package studentmanagement.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

  @Id
  private String id;
  private String firstName;
  private String lastName;
  @Indexed(unique = true)
  private String email;
  private Double age;
  private Integer grade;

  @Override
  public String toString() {
    return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age
            + ", grade=" + grade + ", email=" + email  + "]";
  }
}
