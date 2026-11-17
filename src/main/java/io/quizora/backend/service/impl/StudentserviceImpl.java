package io.quizora.backend.service.impl;

import io.quizora.backend.entity.Student;
import io.quizora.backend.repository.StudentRepository;
import io.quizora.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentserviceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Student found with Id: " + id));
    }

    @Override
    public String deleteStudent(Integer id) {
        Student student = studentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("No student found with id: "+id));
        studentRepository.deleteById(id);
        return "Student Deleted Successfully";
    }

    @Override
    public Student updateStudent(Integer id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No student found with id: "+id));
        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setRollNumber(student.getRollNumber());
        return studentRepository.save(existingStudent);
    }

    @Override
    public Student updateStudentByPatch(Integer id, Map<String, Object> updates) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "rollNumber" ->
                    student.setStudentName((String) value);
                case "studentName" ->
                    student.setStudentName((String) value);
            }
        });
        return studentRepository.save(student);
    }
}
