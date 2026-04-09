package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentService {
    private  final Map <Long, Student> students = new HashMap<>();
    private  long generatedId = 0;

    public  Student createStudent(Student student) {
       student.setId(++generatedId);
       students.put(generatedId,student);
       return student;
    }
    public  Student getStudentById(long studentId) {
        return students.get(studentId);
    }
    public Student updateStudent (long studentId, Student student) {
        students.put(student.getId(),student);
        return student;
    }
    public Student deleteStudent (long studentId) {
        return students.remove(studentId);
    }
    public Collection <Student> getAllStudent (){
        return students.values();
    }
    public Collection <Student> findAge (int age) {
        ArrayList <Student> results = new ArrayList<>();
        for (Student student : students.values()) {
            if (Objects.equals(student.getAge(),age)) {
                results.add(student);
            }
        }
        return results;
    }
}
