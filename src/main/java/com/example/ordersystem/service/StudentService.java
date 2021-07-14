package com.example.ordersystem.service;

import com.example.ordersystem.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ordersystem.repository.StudentRepository;

import java.util.List;

/**
 * Created by CoT on 10/14/17.
 */
@Transactional
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public void saveStudent(Student student){
        studentRepository.save(student);
    }

    public Student getStudent(Long id){
        return studentRepository.getById(id);
    }


    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    /*public List<Student> findStudents(String name){
       Query query = sessionFactory.getCurrentSession().createQuery("from Student s where s.name like :name");
       query.setString("name", "%"+name+"%");
       return query.list();
    }*/

    public void deleteStudent(Long id){
        studentRepository.delete(getStudent(id));
    }

}
