package studentmanagement.domain.boundary.repositories;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import studentmanagement.domain.entities.Student;

import java.awt.print.Pageable;

public interface StudentRepositoryInterface {
    /**
     * Get all students
     *
     * @return a flux of Student objects
     */
    Flux<Student> findAll();

    /**
     * Save Student Detail
     *
     * @param student object to be saved
     * @return saved Student object mono
     */
    Mono<Student> save(Student student);

    /**
     * Find Student Detail by Id
     *
     * @param id of the saved Student
     * @return Student object mono
     */
    Mono<Student> findById(String id);

    /**
     * Delete Student by id
     *
     * @param id of the Student to be deleted
     * @return a void mono
     */
    Mono<Void> deleteById(String id);


    /**
     * Check whether a student record is available for the provided email
     * @param email student id
     * @return
     */
    Mono<Student> findByEmail(String email);


    /**
     * Check whether a student record is available for the provided id
     * @param id student id
     * @return
     */
    Mono<Boolean> existById(String id);
}
