package studentmanagement.externals.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import studentmanagement.domain.boundary.repositories.StudentRepositoryInterface;
import studentmanagement.domain.entities.Student;


@Component
@RequiredArgsConstructor
public class StudentRepositoryImplementation implements StudentRepositoryInterface {

    private final StudentRepository studentRepository;

    @Override
    public Flux<Student> findAll() {
        System.out.println("+++++++++++++++++++++++++++++++++++++ 1 StudentRepositoryImplementation");
//        System.out.println(page);
        return studentRepository.findAll();
    }

    @Override
    public Mono<Student> save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Mono<Student> findById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public Mono<Boolean> existById(String id) {
        return studentRepository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return studentRepository.deleteById(id);
    }

    @Override
    public Mono<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}
