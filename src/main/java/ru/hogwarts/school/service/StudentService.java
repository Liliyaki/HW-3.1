package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(long studentId) {
        return studentRepository.findById(studentId).get();
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long studentId) {
        studentRepository.deleteById(studentId);
    }

    public Collection<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public Collection<Student> findAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentPart(String part) {
        return studentRepository.findAllByNameContainsIgnoreCase(part);
    }

    public Collection<Student> findByAge(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getStudentBuFaculty(Long studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            return null;
        }
        return student.getFaculty();
    }

    public Collection<Student> findByFaculty(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }
    public long getTotalStudentCount() {
        return studentRepository.getTotalStudentCount();
    }
    public double getAverageStudentAge () {
        return studentRepository.getAverageStudentAge();
    }
    public List <Student> getFiveLastStudent() {
        return studentRepository.getFiveLastStudent();
    }
}
