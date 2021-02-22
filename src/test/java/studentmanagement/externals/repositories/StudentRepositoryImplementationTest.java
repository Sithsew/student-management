package studentmanagement.externals.repositories;

import lk.dialog.dds.common.exception.CustomErrorAttributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import studentmanagement.domain.entities.Student;

import java.util.ArrayList;
import java.util.List;

@WebFluxTest
@Import({CustomErrorAttributes.class, StudentRepositoryImplementation.class})
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentRepositoryImplementationTest {

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private StudentRepositoryImplementation studentRepositoryImplementation;


    @Test
    public void findAll() {

        Student student = new Student();
//        student.setId("5fb4e4e6ac99505a59f634f3");
        student.setFirstName("firstname");
        student.setLastName("lastname");
        student.setEmail("example@mail.com");
        student.setAge(12.0);
        student.setGrade(5);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student);

        Mockito.when(studentRepository.findAll()).thenReturn(Flux.fromIterable(studentList));

        Flux<Student> all = studentRepositoryImplementation.findAll();

        StepVerifier.create(all)
                .expectNextMatches(allStudents -> allStudents.getFirstName().equals("firstname"))
                .expectComplete()
                .verify();
    }

    @Test
    public void save() {
        Student student = new Student();
//        student.setId("5fb4e4e6ac99505a59f634f3");
        student.setFirstName("firstname");
        student.setLastName("lastname");
        student.setEmail("example@mail.com");
        student.setAge(12.0);
        student.setGrade(5);

        Student savedStudent = new Student();
        student.setId("5fb4e4e6ac99505a59f634f3");

        Mockito.when(studentRepository.save(student)).thenReturn(Mono.just(savedStudent));

        Mono<Student> save = studentRepositoryImplementation.save(student);

        StepVerifier.create(save)
                .expectNextMatches(savedItem -> savedItem.getId().equals("5fb4e4e6ac99505a59f634f3"))
                .expectComplete()
                .verify();


    }

    @Test
    public void findById() {

        Student student = new Student();
        student.setId("12345");

        Mockito.when(studentRepository.findById("12345")).thenReturn(Mono.just(student));

        Mono<Student> byId = studentRepositoryImplementation.findById("12345");

        StepVerifier.create(byId)
                .expectNextMatches(fetchedItem -> fetchedItem.getId().equals("12345"))
                .expectComplete()
                .verify();

    }

    @Test
    public void deleteById() {
        Mockito.when(studentRepository.deleteById("12345")).thenReturn(Mono.empty());
        Mono<Void> voidMono = studentRepositoryImplementation.deleteById("12345");
        StepVerifier.create(voidMono)
                .expectComplete()
                .verify();
    }


    @Test
    public void testExistById() {
        Mockito.when(studentRepository.existsById("5fb4e4e6ac99505a59f634f3"))
                .thenReturn(Mono.just(Boolean.TRUE));

        Mono<Boolean> existById = studentRepositoryImplementation.existById("5fb4e4e6ac99505a59f634f3");

        StepVerifier.create(existById)
                .expectNextMatches(fetchedStudent -> fetchedStudent.equals(Boolean.TRUE))
                .expectComplete()
                .verify();
    }

    @Test
    public void testExistByEmail() {
        Mockito.when(studentRepository.existsById("ENG"))
            .thenReturn(Mono.just(Boolean.TRUE));

        Mono<Boolean> existById = studentRepositoryImplementation.existById("ENG");

        StepVerifier.create(existById)
            .expectNextMatches(fetchedStudent -> fetchedStudent.equals(Boolean.TRUE))
            .expectComplete()
            .verify();
    }
}