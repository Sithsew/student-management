package studentmanagement.domain.services;

import lk.dialog.dds.common.exception.types.domain.DomainException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import studentmanagement.domain.boundary.repositories.StudentRepositoryInterface;
import studentmanagement.domain.entities.Student;

import java.util.Optional;

@Service
public class StudentService {

  private static final String NO_DATA_TYPE_MESSAGE = "No student record found for given Id";
  private static final String DUPLICATE_MESSAGE = "Email already exists";

  private final StudentRepositoryInterface studentRepositoryInterface;

  public StudentService(StudentRepositoryInterface studentRepositoryInterface) {
    this.studentRepositoryInterface = studentRepositoryInterface;
  }

  /**
   * Get a Flux of Students
   *
   * @return Page<Student>
   * @param size
   * @param page
   */
  public Flux<Student> getAllStudents(Optional<String> page, Optional<String> size) {
    Integer pageNumber = page.isPresent() ? Integer.parseInt(page.get()): 1;
    Integer pageSize = size.isPresent() ? Integer.parseInt(size.get()): 10;
    Pageable paging = PageRequest.of(pageNumber- 1, pageSize);
    System.out.println("+++++++++++++++++++++++++++++++++++++ 2 StudentService");
    return this.studentRepositoryInterface.findAll();
  }

//  public Page<Student> getAllStudents(Integer page, Integer size) {
//    Pageable paging = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
//    return this.studentRepository.findAll(paging);
//  }


  /**
   * Get a single Student by id
   *
   * @param id Student id
   * @return Mono<Student>
   */
  public Mono<Student> getStudentById(String id) {
    return studentRepositoryInterface.findById(id)
            //throw error if no country for given id
            .switchIfEmpty(Mono.error(new DomainException(NO_DATA_TYPE_MESSAGE)));
  }

  /**
   * Add a Student
   *
   * @param student Student
   * @return Mono<Student>
   */
  public Mono<Student> addStudent(Student student) {

    return studentRepositoryInterface.save(student)
    // check for duplicate key adding;
            .onErrorMap(DuplicateKeyException.class,
                    e -> new DomainException(DUPLICATE_MESSAGE));
  }



  /**
   * Edit Student
   *
   * @param student Student
   * @return Mono<Void>
   */
  public Mono<Void> edit(Student student) {

    return studentRepositoryInterface.existById(student.getId())
            .flatMap(exists -> {
              // if only exists, allow edit
              if (Boolean.TRUE.equals(exists)) {
                return studentRepositoryInterface.save(student).then()
                        .onErrorMap(DuplicateKeyException.class
                                , e -> new DomainException(DUPLICATE_MESSAGE));
              }
              // throw error if editing id not exists
              return Mono.error(new DomainException(NO_DATA_TYPE_MESSAGE));
            });
  }

  /**
   * Delete Student
   *
   * @param id Student id
   * @return Mono<Void>
   */
  public Mono<Void> delete(String id) {
    return studentRepositoryInterface.existById(id)
            .flatMap(exist -> {
              // if exists, delete
              if (Boolean.TRUE.equals(exist)) {
                return studentRepositoryInterface.deleteById(id);
              }
              // if does not exists, throw error
              return Mono.error(new DomainException(NO_DATA_TYPE_MESSAGE));
            });
  }
}
