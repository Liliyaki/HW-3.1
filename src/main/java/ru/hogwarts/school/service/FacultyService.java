package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository,StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty (Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        logger.debug("Was create faculty: name{}, color{}", faculty.getName(), faculty.getColor());
       return facultyRepository.save(faculty);
    }
    public Faculty getFacultyById (long facultyId) {
        logger.info("Was invoked method for get faculty by id");
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        logger.error("The faculty with id{} is not found", facultyId);
        return faculty;
    }
    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Was invoked method for update faculty with id = {}", faculty.getId());
        if (faculty.getId() == null) {
            logger.warn("Update faculty called with null id");
            return null;
        }
        Faculty existingFaculty = facultyRepository.findById(faculty.getId()).orElse(null);
        if (existingFaculty == null) {
            logger.error("Cannot update: faculty with id = {} not found", faculty.getId());
            return null;
        }
        return facultyRepository.save(faculty);
    }
    public void deleteFaculty (long facultyId) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(facultyId);
    }
    public Collection <Faculty> getAllFaculty(){
        logger.info("Was invoked method for get all faculty");
        return facultyRepository.findAll();
    }
    public Collection <Faculty> findColor (String color) {
        logger.info("Was invoked method for find faculty by color");
        return facultyRepository.findByColor(color);
    }
    public Collection <Faculty> findByNameOrColor (String query) {
        logger.info("Was invoked method for find faculty by name or color");
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(query,query);
    }
    public Collection <Student> getFacultyStudent (Long facultyId) {
        logger.info("Was invoked method for find faculty");
        return studentRepository.findByFacultyId(facultyId);
    }
    public String getLongestName () {
        logger.info("Was invoked method for get longest name");
        List<Faculty> faculties = facultyRepository.findAll();
        String longestName = faculties.stream()
                .max(Comparator.comparingInt(faculty -> faculty.getName().length()))
                .map(Faculty::getName)
                .orElse("");
        return  longestName;
    }
}
