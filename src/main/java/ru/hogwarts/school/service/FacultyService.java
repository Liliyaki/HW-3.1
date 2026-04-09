package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
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
}
