package com.example.ordersystem.controller;

import com.example.ordersystem.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.ordersystem.service.TeacherService;

import java.util.List;

/**
 * Created by CoT on 7/29/18.
 */
@RestController
//@RequestMapping(path = "/")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /*public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }*/

    @RequestMapping(path = "/teachers", method = RequestMethod.GET)
    public List<Teacher> getAllTeachers(){
        return teacherService.getAllTeachers();
    }

    @RequestMapping(path = "/teachers", method = RequestMethod.POST)
    public void saveTeacher(@RequestBody Teacher teacher){
        teacherService.saveTeacher(teacher);
    }

    @RequestMapping(path = "/teachers/{id}", method = RequestMethod.DELETE)
    public void deleteTeacher(@PathVariable Long id){
        teacherService.deleteTeacher(id);
    }
}
