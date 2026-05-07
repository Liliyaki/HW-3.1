package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        testFaculty = new Faculty("Гриффиндор", "Красный");
        testFaculty.setId(1L);
    }

    @Test
    void getFacultyById_ShouldReturnFaculty() throws Exception {
        when(facultyService.getFacultyById(1L)).thenReturn(testFaculty);

        mockMvc.perform(get("/faculty/findID/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));

        verify(facultyService, times(1)).getFacultyById(1L);
    }

    @Test
    void getFacultyById_WithNonExistentId_ShouldReturn404() throws Exception {
        when(facultyService.getFacultyById(999L)).thenReturn(null);

        mockMvc.perform(get("/faculty/findID/999"))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).getFacultyById(999L);
    }

    @Test
    void createFaculty_ShouldReturnCreatedFaculty() throws Exception {
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(testFaculty);

        mockMvc.perform(post("/faculty/createFaculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));

        verify(facultyService, times(1)).createFaculty(any(Faculty.class));
    }

    @Test
    void updateFaculty_ShouldReturnUpdatedFaculty() throws Exception {
        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(testFaculty);

        mockMvc.perform(put("/faculty/updateFaculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"));

        verify(facultyService, times(1)).updateFaculty(any(Faculty.class));
    }

    @Test
    void deleteFaculty_ShouldRemoveFaculty() throws Exception {
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(delete("/faculty/delete/1"))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).deleteFaculty(1L);
    }

    @Test
    void getAllFaculties_ShouldReturnCollectionOfFaculties() throws Exception {
        Collection<Faculty> faculties = Arrays.asList(testFaculty);
        when(facultyService.getAllFaculty()).thenReturn(faculties);

        mockMvc.perform(get("/faculty/getAllFaculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Грифиндор"));

        verify(facultyService, times(1)).getAllFaculty();
    }

    @Test
    void filterByColor_ShouldReturnFacultiesWithSpecifiedColor() throws Exception {
        Collection<Faculty> faculties = Collections.singletonList(testFaculty);
        when(facultyService.findColor("Красный")).thenReturn(faculties);

        mockMvc.perform(get("/faculty/filterColor?color=Красный"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(facultyService, times(1)).findColor("Красный");
    }

    @Test
    void findByNameOrColor_ShouldReturnFacultiesMatchingQuery() throws Exception {
        Collection<Faculty> faculties = Collections.singletonList(testFaculty);
        when(facultyService.findByNameOrColor("Гриффиндор")).thenReturn(faculties);

        mockMvc.perform(get("/faculty/findByNameOrColor?query=Гриффиндор"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(facultyService, times(1)).findByNameOrColor("Гриффиндор");
    }
}