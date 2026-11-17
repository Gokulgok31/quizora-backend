package io.quizora.backend.service;

import io.quizora.backend.entity.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {

    Student createStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(Integer id);

    String deleteStudent(Integer id);

    Student updateStudent(Integer id, Student student);

    Student updateStudentByPatch(Integer id, Map<String, Object> updates);
}
