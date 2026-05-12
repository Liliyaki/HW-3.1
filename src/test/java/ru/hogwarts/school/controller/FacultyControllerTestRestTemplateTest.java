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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    private String baseUrl;
    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/faculty";
        testFaculty = new Faculty("Красный", "Гриффиндор");
        testFaculty = facultyRepository.save(testFaculty);
    }

    @AfterEach
    void tearDown() {
        facultyRepository.deleteAll();
    }


    @Test
    void getFacultyById_ShouldReturnFaculty() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/findID/" + testFaculty.getId(),
                Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testFaculty.getId(), response.getBody().getId());
        assertEquals(testFaculty.getName(), response.getBody().getName());
        assertEquals(testFaculty.getColor(), response.getBody().getColor());
    }


    @Test
    void getFacultyById_WithNonExistentId_ShouldReturn404() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/findID/999",
                Faculty.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void createFaculty_ShouldReturnCreatedFaculty() {
        Faculty newFaculty = new Faculty("Слизерин", "Зеленый");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                baseUrl + "/createFaculty",
                newFaculty,
                Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Слизерин", response.getBody().getName());
        assertEquals("Зеленый", response.getBody().getColor());
    }


    @Test
    void updateFaculty_ShouldReturnUpdatedFaculty() {
        testFaculty.setName("Гриффиндор (Основатель: Годрик Гриффиндор)");
        testFaculty.setColor("Красно-золотой");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(testFaculty, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl + "/updateFaculty",
                HttpMethod.PUT,
                requestEntity,
                Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Гриффиндор (Основатель: Годрик Гриффиндор)", response.getBody().getName());
        assertEquals("Красно-золотой", response.getBody().getColor());
    }


    @Test
    void deleteFaculty_ShouldRemoveFaculty() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/delete/" + testFaculty.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());


        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                baseUrl + "/findID/" + testFaculty.getId(),
                Faculty.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }


    @Test
    void getAllFaculties_ShouldReturnCollectionOfFaculties() {
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                baseUrl + "/getAllFaculty",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 1);
    }


    @Test
    void filterByColor_ShouldReturnFacultiesWithSpecifiedColor() {
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                baseUrl + "/filterColor?color=Красный",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void findByNameOrColor_ShouldReturnFacultiesMatchingQuery() {
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                baseUrl + "/findByNameOrColor?query=Гриффиндор",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
