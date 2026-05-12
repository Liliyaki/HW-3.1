package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student("Гарри Поттер", 14);
        testStudent.setId(1L);
    }


    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/student/addStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(14));

        verify(studentService, times(1)).createStudent(any(Student.class));
    }

    @Test
    void getStudentById_ShouldReturnStudent() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(testStudent);

        mockMvc.perform(get("/student/findID/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(14));

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    void getStudentById_WithNonExistentId_ShouldReturn404() throws Exception {
        when(studentService.getStudentById(999L)).thenReturn(null);

        mockMvc.perform(get("/student/findID/999"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getStudentById(999L);
    }

    @Test
    void updateStudent_ShouldReturnUpdatedStudent() throws Exception {
        when(studentService.updateStudent(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(put("/student/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"));

        verify(studentService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    void deleteStudent_ShouldRemoveStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/student/delete/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    void getAllStudents_ShouldReturnCollectionOfStudents() throws Exception {
        Collection<Student> students = Arrays.asList(testStudent);
        when(studentService.getAllStudent()).thenReturn(students);

        mockMvc.perform(get("/student/getAllStudent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"));

        verify(studentService, times(1)).getAllStudent();
    }

    @Test
    void filterByAge_ShouldReturnStudentsWithSpecifiedAge() throws Exception {
        Collection<Student> students = Collections.singletonList(testStudent);
        when(studentService.findAge(14)).thenReturn(students);

        mockMvc.perform(get("/student/filterByAge?age=14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(studentService, times(1)).findAge(14);
    }

    @Test
    void filterStudentByPart_ShouldReturnStudentsWithMatchingName() throws Exception {
        Collection<Student> students = Collections.singletonList(testStudent);
        when(studentService.findStudentPart("Гарри")).thenReturn(students);

        mockMvc.perform(get("/student/filterStudentByPart?part=Гарри"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(studentService, times(1)).findStudentPart("Гарри");
    }

    @Test
    void filterStudentBetweenAge_ShouldReturnStudentsInAgeRange() throws Exception {
        Collection<Student> students = Collections.singletonList(testStudent);
        when(studentService.findByAge(10, 18)).thenReturn(students);

        mockMvc.perform(get("/student/filterStudentBetweenAge?minAge=10&maxAge=18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(studentService, times(1)).findByAge(10, 18);
    }
}
