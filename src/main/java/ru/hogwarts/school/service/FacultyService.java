package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository,StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty (Faculty faculty) {
       return facultyRepository.save(faculty);
    }
    public Faculty getFacultyById (long facultyId) {
        return facultyRepository.findById(facultyId).get();
    }
    public Faculty updateFaculty (Faculty faculty) {
        return facultyRepository.save(faculty);
    }
    public void deleteFaculty (long facultyId) {
        facultyRepository.deleteById(facultyId);
    }
    public Collection <Faculty> getAllFaculty(){
        return facultyRepository.findAll();
    }
    public Collection <Faculty> findColor (String color) {
        return facultyRepository.findByColor(color);
    }
    public Collection <Faculty> findByNameOrColor (String query) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(query,query);
    }
    public Collection <Student> getFacultyStudent (Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }
}
