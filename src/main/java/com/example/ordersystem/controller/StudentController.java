package com.example.ordersystem.controller;

import com.example.ordersystem.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.ordersystem.service.StudentService;

import java.util.List;

/**
 * Created by CoT on 7/29/18.
 */
@RestController
//@RequestMapping(path = "/")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /*public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }*/

    @GetMapping(path = "/students")
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @RequestMapping(path = "/students", method = RequestMethod.POST)
    public void saveStudent(@RequestBody Student student){
        studentService.saveStudent(student);
    }

    @RequestMapping(path = "/students/{id}", method = RequestMethod.DELETE)
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }

}
