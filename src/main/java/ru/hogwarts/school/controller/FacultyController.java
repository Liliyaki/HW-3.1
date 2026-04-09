package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RequestMapping ("/faculty")
@RestController
public class FacultyController {
    private final FacultyService facultyService;
    public FacultyController (FacultyService facultyService) {
        this.facultyService = facultyService;
    }
    @GetMapping("/findID")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        Faculty faculty = facultyService.getFacultyById(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }
    @PostMapping ("/createFaculty")
    public ResponseEntity <Faculty> createFaculty (@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(faculty);
    }
    @PutMapping ("/updateFaculty")
    public ResponseEntity<Faculty> editFaculty (@RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.updateFaculty(faculty.getId(), faculty);
        if (updatedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }
    @DeleteMapping ("/delete")
    public ResponseEntity <Faculty> deleteFaculty (@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping ("/getAllFaculty")
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculty());
    }
    @GetMapping("/filterColor")
    public  ResponseEntity <Collection<Faculty>> findFaculties (@RequestParam (required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}
