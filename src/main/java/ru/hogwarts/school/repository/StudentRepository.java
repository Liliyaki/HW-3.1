package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List <Student> findByAge (int age);
    Collection <Student> findAllByNameContainsIgnoreCase(String part);
    Collection <Student> findByAgeBetween (int minAge, int maxAge);
    Collection <Student> findByFacultyId (Long facultyId);

    @Query("SELECT COUNT (s) FROM STUDENT s")
    long getTotalStudentCount();

    @Query ("SELECT AVG (s.age) FROM STUDENT s.age")
    double getAverageStudentAge();

    @Query ("SELECT s FROM Student s ORDER BY s.id DESC")
    List <Student> getFiveLastStudent();
}
