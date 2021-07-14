package com.example.ordersystem.service;

import com.example.ordersystem.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ordersystem.repository.TeacherRepository;

import java.util.List;

@Transactional
@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public void saveTeacher(Teacher teacher){
        teacherRepository.save(teacher);
    }

    public Teacher getTeacher(Long id){
        return teacherRepository.getById(id);
    }

    public List<Teacher> getAllTeachers(){
        return teacherRepository.findAll();
    }

    /*public List<Teacher> findTeachers(String name){
       Query query = sessionFactory.getCurrentSession().createQuery("from Teacher s where s.name like :name");
       query.setString("name", "%"+name+"%");
       return query.list();
    }*/

    public void deleteTeacher(Long id){
        teacherRepository.delete(getTeacher(id));
    }

}