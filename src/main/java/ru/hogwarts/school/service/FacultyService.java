package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.*;

@Service
public class FacultyService {
    private final Map <Long, Faculty> facultyMap = new HashMap<>();
    private long generatedId = 0;
    public Faculty createFaculty (Faculty faculty) {
       faculty.setId(++generatedId);
       facultyMap.put(generatedId,faculty);
        return faculty;
    }
    public Faculty getFacultyById (long facultyId) {
        return facultyMap.get(facultyId);
    }
    public Faculty updateFaculty (long facultyId, Faculty faculty) {
        facultyMap.put(facultyId, faculty);
        return faculty;
    }
    public Faculty deleteFaculty (long facultyId) {
        return facultyMap.remove(facultyId);
    }
    public Collection <Faculty> getAllFaculty(){
        return facultyMap.values();
    }
    public Collection <Faculty> findColor (String color) {
        ArrayList <Faculty> results = new ArrayList<>();
        for (Faculty faculty : facultyMap.values()){
            if (Objects.equals(faculty.getColor(),color)) {
                results.add(faculty);
            }
        }
        return results;
    }
}
