package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
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
        return ResponseEntity.ok(studentService.findByAge(minAge, maxAge));
    }

    @GetMapping("/{studentId}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long studentId) {
        Faculty faculty = studentService.getStudentByFaculty(studentId);
        if (faculty == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalStudentCount() {
        long count = studentService.getTotalStudentCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageStudentAge() {
        double averageAge = studentService.getAverageStudentAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudent() {
        List<Student> lastFive = studentService.getFiveLastStudent();
        return ResponseEntity.ok(lastFive);
    }

    @GetMapping("/name-starting-a")
    public ResponseEntity<List<String>> getStudentNamesStartingWithA() {
        List<String> names = studentService.getStudentNamesStartingWithA();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/average-all-students-age")
    public ResponseEntity<Double> getAverageAllStudentsAge() {
        double average = studentService.getAverageAllStudentsAge();
        return ResponseEntity.ok(average);
    }

    @GetMapping("/sum-slow")
    public int getSumSlow() {

        long startTime = System.currentTimeMillis();

        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);

        long endTime = System.currentTimeMillis();

        return sum;
    }

    @GetMapping("/print-parallel")
    public ResponseEntity<String> printStudentsParallel() {
        List<Student> students = studentService.getAllStudent().stream()
                .limit(6)
                .collect(Collectors.toList());

        if (students.size() < 6) {
            return ResponseEntity.ok("Недостаточно студентов для вывода (нужно 6, есть " + students.size() + ")");
        }


        System.out.println("=== Основной поток (main) ===");
        System.out.println(Thread.currentThread().getName() + ": " + students.get(0).getName());
        System.out.println(Thread.currentThread().getName() + ": " + students.get(1).getName());


        Thread thread1 = new Thread(() -> {
            System.out.println("=== Поток 1 ===");
            System.out.println(Thread.currentThread().getName() + ": " + students.get(2).getName());
            System.out.println(Thread.currentThread().getName() + ": " + students.get(3).getName());
        });
        thread1.start();


        Thread thread2 = new Thread(() -> {
            System.out.println("=== Поток 2 ===");
            System.out.println(Thread.currentThread().getName() + ": " + students.get(4).getName());
            System.out.println(Thread.currentThread().getName() + ": " + students.get(5).getName());
        });
        thread2.start();

        return ResponseEntity.ok("Имена студентов выводятся в консоль");
    }

    private synchronized void printNameSynchronized(String name) {
        System.out.println(Thread.currentThread().getName() + ": " + name);
    }


    @GetMapping("/print-synchronized")
    public ResponseEntity<String> printStudentsSynchronized() {


        List<Student> students = studentService.getAllStudent().stream()
                .limit(6)
                .collect(Collectors.toList());

        if (students.size() < 6) {
            return ResponseEntity.ok("Недостаточно студентов для вывода (нужно 6, есть " + students.size() + ")");
        }


        System.out.println("=== Основной поток (main) ===");
        printNameSynchronized(students.get(0).getName());
        printNameSynchronized(students.get(1).getName());


        Thread thread1 = new Thread(() -> {
            System.out.println("=== Поток 1 ===");
            printNameSynchronized(students.get(2).getName());
            printNameSynchronized(students.get(3).getName());
        });
        thread1.start();


        Thread thread2 = new Thread(() -> {
            System.out.println("=== Поток 2 ===");
            printNameSynchronized(students.get(4).getName());
            printNameSynchronized(students.get(5).getName());
        });
        thread2.start();

        return ResponseEntity.ok("Имена студентов выводятся в консоль с синхронизацией");
    }
}
