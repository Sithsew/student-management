package studentmanagement.application.handlers;

import lk.dialog.dds.common.exception.types.application.HandlerException;
import lk.dialog.dds.common.request.validator.RequestEntityValidator;
import lk.dialog.dds.common.response.transformer.ResponseEntityTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import studentmanagement.application.transport.request.entities.StudentRequestEntity;
import studentmanagement.application.transport.response.transformers.StudentTransformer;
import studentmanagement.domain.entities.Student;
import studentmanagement.domain.services.StudentService;

import java.util.Map;

@Component
@Slf4j // logger (generates a class property of name `log`)
public class StudentHandler {

    private final StudentService studentService;
    private final ResponseEntityTransformer transformer;
    private final RequestEntityValidator validator;

    public StudentHandler(StudentService studentService, ResponseEntityTransformer transformer, RequestEntityValidator validator) {
        this.studentService = studentService;
        this.transformer = transformer;
        this.validator = validator;
    }

  /**
   * Handle get all student request
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> get(ServerRequest request) {

    // logging
    log.info("Lombok logger get students");

    return ServerResponse.ok().body(transformer.transform(studentService.getAllStudents(
            request.queryParam("page" ), request.queryParam("size")
            ),
            new StudentTransformer()), Map.class);
  }

  /**
   * Handle get student by id
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> getById(ServerRequest request) {

    return ServerResponse.ok()
        .body(transformer.transform(studentService.getStudentById(request.pathVariable("id")),
            new StudentTransformer()),
            Map.class);
  }

  /**
   * Handle creating a new student entry
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> add(ServerRequest request) {

    return ServerResponse
        .status(HttpStatus.CREATED)
        .body(transformer.transform(request.bodyToMono(StudentRequestEntity.class)
            .onErrorMap(e ->
                    new HandlerException(e.getMessage()))
            .flatMap(payload -> {

              // validate
              this.validator.validate(payload);

              // map
              Student student = new Student();
              student.setFirstName(payload.getFirstName());
              student.setLastName(payload.getLastName());
              student.setEmail(payload.getEmail());
              student.setGrade(payload.getGrade());
              student.setAge(payload.getAge());

                return this.studentService.addStudent(student);
            }), new StudentTransformer()), Map.class);
  }

  /**
   * Handle updating of an existing student
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> edit(ServerRequest request) {

    return request.bodyToMono(StudentRequestEntity.class)
        .flatMap(payload -> {

          // validate
          this.validator.validate(payload);

          // map
            Student student = new Student();

            student.setId(request.pathVariable("id"));
            student.setFirstName(payload.getFirstName());
            student.setLastName(payload.getLastName());
            student.setEmail(payload.getEmail());
            student.setGrade(payload.getGrade());
            student.setAge(payload.getAge());

          return this.studentService.edit(student);
        }).then(ServerResponse.noContent().build());
  }

  /**
   * Handle deletion of an existing movie
   *
   * @param request ServerRequest
   * @return ServerResponse
   */
  public Mono<ServerResponse> delete(ServerRequest request) {

    return this.studentService.delete(request.pathVariable("id"))
        .then(ServerResponse.noContent().build());
  }
}
