package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List <Faculty> findByColor (String color);
    Collection <Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String namePart, String colorPart);

}
