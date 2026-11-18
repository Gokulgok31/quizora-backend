package io.quizora.backend.repository;

import io.quizora.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByRollNumber(String rollNumber);

    Student findByStudentName(String name);

    String existsBystudentName(String studentName);

    @Query("SELECT s.studentName FROM Student s WHERE s.id = :id")
    String findUsernameById(@Param("id") Integer id);
}
