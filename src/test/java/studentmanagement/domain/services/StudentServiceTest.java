package studentmanagement.domain.services;

import lk.dialog.dds.common.exception.types.domain.DomainException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import studentmanagement.domain.boundary.repositories.StudentRepositoryInterface;
import studentmanagement.domain.entities.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepositoryInterface studentRepositoryInterface;

    @Test
    public void getAllStudents() {

        Student student = new Student();
        student.setFirstName("firstname");
        student.setLastName("lastname");
        student.setEmail("example@mail.com");
        student.setAge(12.0);
        student.setGrade(5);


        List<Student> studentList = new ArrayList<>();
        studentList.add(student);

        Mockito.when(studentRepositoryInterface.findAll()).thenReturn(Flux.fromIterable(studentList));

        Flux<Student> allStudents = studentService.getAllStudents();

        StepVerifier.create(allStudents)
                .expectNextMatches(students -> students.getFirstName().equals("firstname"))
                .expectComplete()
                .verify();
    }

    @Test
    public void addStudent() {

        Student student = new Student();
        student.setFirstName("firstname");
        student.setLastName("lastname");
        student.setEmail("example@mail.com");
        student.setAge(12.0);
        student.setGrade(5);

        Student savedStudent =new Student();
        savedStudent.setId("5fb4e4e6ac99505a59f634f3");

        Mockito.when(studentRepositoryInterface.save(student)).thenReturn(Mono.just(savedStudent));

        Mono<Student> stringMono = studentService.addStudent(student);

        StepVerifier.create(stringMono)
                .expectNextMatches(studentSaved -> studentSaved.getId().equals("5fb4e4e6ac99505a59f634f3"))
                .expectComplete()
                .verify();
    }

    @Test
    public void addLanguageDuplicateKey() {

        Student student = new Student();
        student.setFirstName("firstname");
        student.setLastName("lastname");
        student.setEmail("example@mail.com");
        student.setAge(12.0);
        student.setGrade(5);

        Student savedStudent =new Student();
        savedStudent.setId("5fb4e4e6ac99505a59f634f3");
        savedStudent.setEmail("example@mail.com");

        Mockito.when(studentRepositoryInterface.save(student)).thenReturn(Mono.error(new DomainException("Email already exists")));

        Mono<Student> stringMono = studentService.addStudent(student);

        StepVerifier.create(stringMono)
                .expectErrorMessage("Email already exists")
                .verify();
    }

    @Test
    public void getStudentById() {

        Student student = new Student();
        student.setId("5fb4e4e6ac99505a59f634f3");
        student.setFirstName("firstname");
        student.setLastName("lastname");
        student.setEmail("example@mail.com");
        student.setAge(12.0);
        student.setGrade(5);

        Mockito.when(studentRepositoryInterface.findById("5fb4e4e6ac99505a59f634f3")).thenReturn(Mono.just(student));

        Mono<Student> studentById = studentService.getStudentById("5fb4e4e6ac99505a59f634f3");

        StepVerifier.create(studentById)
                .expectNextMatches(fetchedStudent -> fetchedStudent.getId().equals("5fb4e4e6ac99505a59f634f3"))
                .expectComplete()
                .verify();
    }

    @Test
    public void editStudent() {
        Student student = new Student();
        student.setId("5fb4e4e6ac99505a59f634f3");
        student.setFirstName("firstname");
        student.setLastName("lastname");
        student.setEmail("example@mail.com");
        student.setAge(12.0);
        student.setGrade(5);

        Mockito.when(studentRepositoryInterface.save(student)).thenReturn(Mono.just(student));
        Mockito.when(studentRepositoryInterface.existById("5fb4e4e6ac99505a59f634f3")).thenReturn(Mono.just(Boolean.TRUE));

        Mono<Void> voidMono = studentService.edit(student);

        StepVerifier.create(voidMono)
                .expectComplete()
                .verify();
    }
//
    @Test
    public void editLanguageNotFoundId() {
        Student student = new Student();
        student.setId("5fb4e4e6ac99505a59f634f3");
        student.setFirstName("firstname");
        student.setLastName("lastname");
        student.setEmail("example@mail.com");
        student.setAge(12.0);
        student.setGrade(5);

        Mockito.when(studentRepositoryInterface.save(student)).thenReturn(Mono.just(student));
        Mockito.when(studentRepositoryInterface.existById("5fb4e4e6ac99505a59f634f3")).thenReturn(Mono.just(Boolean.FALSE));

        Mono<Void> voidMono = studentService.edit(student);

        StepVerifier.create(voidMono)
                .expectError(DomainException.class)
                .verify();
    }

    @Test
    public void deleteStudent() {

        Mockito.when(studentRepositoryInterface.deleteById("5fb4e4e6ac99505a59f634f3")).thenReturn(Mono.empty());
        Mockito.when(studentRepositoryInterface.existById("5fb4e4e6ac99505a59f634f3")).thenReturn(Mono.just(Boolean.TRUE));

        Mono<Void> voidMono = studentService.delete("5fb4e4e6ac99505a59f634f3");

        StepVerifier.create(voidMono)
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteStudentNotFoundId() {

        Mockito.when(studentRepositoryInterface.deleteById("5fb4e4e6ac99505a59f634f3")).thenReturn(Mono.empty());
        Mockito.when(studentRepositoryInterface.existById("5fb4e4e6ac99505a59f634f3")).thenReturn(Mono.just(Boolean.FALSE));

        Mono<Void> voidMono = studentService.delete("5fb4e4e6ac99505a59f634f3");

        StepVerifier.create(voidMono)
                .expectError(DomainException.class)
                .verify();
    }
}