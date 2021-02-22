package studentmanagement.externals.repositories;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import studentmanagement.domain.entities.Student;

import java.awt.print.Pageable;

public interface StudentRepository extends ReactiveMongoRepository<Student, String> {

  /**
   * Get Student by email
   *
   * @param email Student email
   * @return Mono<Student>
   */
  Mono<Student> findByEmail(String email);
}
