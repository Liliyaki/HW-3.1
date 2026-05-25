package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Create student: name = {}, age = {}", student.getName(), student.getAge());
        if (student.getAge() > 100) {
            logger.warn("The student's age is to big: age{}", student.getAge());
        }
        return studentRepository.save(student);
    }

    public Student getStudentById(long studentId) {
        logger.info("Was invoked method for find student by id");
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            logger.error("Student with id{} is not found", studentId);
        }
        return student;
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student");
        return studentRepository.save(student);
    }

    public void deleteStudent(long studentId) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(studentId);
    }

    public Collection<Student> getAllStudent() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findAge(int age) {
        logger.info("Was invoked method for find student by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentPart(String part) {
        logger.info("Was invoked method for find students by part");
        return studentRepository.findAllByNameContainsIgnoreCase(part);
    }

    public Collection<Student> findByAge(int minAge, int maxAge) {
        logger.info("Was invoked method for min and max age student");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getStudentByFaculty(Long studentId) {
        logger.info("Was invoked method for find faculty by student id = {}", studentId);
        Student student = getStudentById(studentId);
        if (student == null) {
            logger.error("Student with id = {} not found for getting faculty", studentId);
            return null;
        }
        return student.getFaculty();
    }

    public Collection<Student> findByFaculty(Long facultyId) {
        logger.info("Was invoked method for find student by faculty id");
        return studentRepository.findByFacultyId(facultyId);
    }

    public long getTotalStudentCount() {
        logger.info("Was invoked method for get total student count");
        return studentRepository.getTotalStudentCount();
    }

    public double getAverageStudentAge() {
        logger.info("Was invoked method for average student age");
        return studentRepository.getAverageStudentAge();
    }

    public List<Student> getFiveLastStudent() {
        logger.info("Was invoked method for get five last student");
        return studentRepository.getFiveLastStudent();
    }

    public List<String> getStudentNamesStartingWithA() {
        logger.info("Was invoked method for get student names starting with A");

        List<Student> students = studentRepository.findAll();

        List<String> result = students.stream()
                .filter(student -> student.getName().toUpperCase().startsWith("А"))
                .map(student -> student.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
        return result;
    }
    public double getAverageAllStudentsAge () {
        logger.info("Was invoked method for average all students age");
        List<Student> students = studentRepository.findAll();
        double average = students.stream()
                .mapToInt(Student :: getAge)
                .average()
                .orElse(0.0);
        return average;
    }
}
