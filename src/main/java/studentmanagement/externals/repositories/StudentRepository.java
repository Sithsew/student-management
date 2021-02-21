package studentmanagement.externals.repositories;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import studentmanagement.domain.entities.Student;

public interface StudentRepository extends ReactiveMongoRepository<Student, String> {

  /**
   * Get all student with paging
   *
   * @param pageable Pageable
   * @return Page<Student>
   */
//  Page<Student> findAll(Pageable pageable);

  /**
   * Get Student by email
   *
   * @param email Student email
   * @return Mono<Student>
   */
  Mono<Student> findByEmail(String email);
}
