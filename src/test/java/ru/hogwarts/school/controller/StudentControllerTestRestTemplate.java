package ru.hogwarts.school.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/student";
        testStudent = new Student("Гарри Поттер", 14);
        testStudent = studentRepository.save(testStudent);
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }


    @Test
    void createStudent_ShouldReturnCreatedStudent() {
        Student newStudent = new Student("Гермиона Грейнджер", 15);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl + "/addStudent",
                newStudent,
                Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Гермиона Грейнджер", response.getBody().getName());
        assertEquals(15, response.getBody().getAge());
    }

    @Test
    void getStudentById_ShouldReturnStudent() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/findID/" + testStudent.getId(),
                Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testStudent.getId(), response.getBody().getId());
        assertEquals(testStudent.getName(), response.getBody().getName());
        assertEquals(testStudent.getAge(), response.getBody().getAge());
    }


    @Test
    void getStudentById_WithNonExistentId_ShouldReturn404() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/findID/999",
                Student.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void updateStudent_ShouldReturnUpdatedStudent() {
        testStudent.setName("Гарри Джеймс Поттер");
        testStudent.setAge(15);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> requestEntity = new HttpEntity<>(testStudent, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl + "/update",
                HttpMethod.PUT,
                requestEntity,
                Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Гарри Джеймс Поттер", response.getBody().getName());
        assertEquals(15, response.getBody().getAge());
    }


    @Test
    void deleteStudent_ShouldRemoveStudent() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/delete/" + testStudent.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());


        ResponseEntity<Student> getResponse = restTemplate.getForEntity(
                baseUrl + "/findID/" + testStudent.getId(),
                Student.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }


    @Test
    void getAllStudents_ShouldReturnCollectionOfStudents() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl + "/getAllStudent",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 1);
    }


    @Test
    void filterByAge_ShouldReturnStudentsWithSpecifiedAge() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl + "/filterByAge?age=14",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void filterStudentByPart_ShouldReturnStudentsWithMatchingName() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl + "/filterStudentByPart?part=Гарри",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void filterStudentBetweenAge_ShouldReturnStudentsInAgeRange() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl + "/filterStudentBetweenAge?minAge=10&maxAge=18",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}



