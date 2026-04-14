package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("/findID/{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/update")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student updateStudent = studentService.updateStudent(student);
        if (updateStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllStudent")
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping("/filterByAge")
    public ResponseEntity<Collection<Student>> findStudent(@RequestParam(required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/filterStudentByPart")
    public ResponseEntity<Collection<Student>> findStudentByPart(@RequestParam(required = false) String part) {
        if (part != null && !part.isBlank()) {
            return ResponseEntity.ok(studentService.findStudentPart(part));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/filterStudentBetweenAge")
    public ResponseEntity<Collection<Student>> findStudentBetweenAge(@RequestParam(required = false) int minAge, int maxAge) {
        return ResponseEntity.ok(studentService.findByAge(minAge,maxAge)) ;
    }
    @GetMapping ("/{stidentId}/faculty")
    public ResponseEntity <Faculty> getStudentFaculty (@PathVariable Long studentId) {
        Faculty faculty = studentService.getStudentBuFaculty(studentId);
        if (faculty == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }
}
