package studentmanagement.application.transport.response.transformers;

import lk.dialog.dds.common.response.transformer.ResponseEntityInterface;
import studentmanagement.domain.entities.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentTransformer implements ResponseEntityInterface<Student> {

  @Override
  public Map<String, Object> transform(Student student) {

    Map<String, Object> mapping = new HashMap<>();

    mapping.put("id", student.getId());
    mapping.put("firstName", student.getFirstName());
    mapping.put("lastName", student.getLastName());
    mapping.put("email", student.getEmail());
    mapping.put("age", student.getAge());
    mapping.put("grade", student.getGrade());

    return mapping;
  }
}
