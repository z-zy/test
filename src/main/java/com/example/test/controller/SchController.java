package com.example.test.controller;

import com.example.test.dao.SchoolMapper;
import com.example.test.datasouce.TargetDataSource;
import com.example.test.pojo.School;
import com.example.test.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SchController {

    @Autowired
    SchoolMapper schoolMapper;

    @Autowired
    SchoolService schoolService;

    @RequestMapping("test")
    public School school1(){
        return schoolMapper.selectByPrimaryKey(1);
    }

    @RequestMapping("test2")
    @TargetDataSource(name = "test")
    public School school2(){
        return schoolMapper.selectByPrimaryKey(2);
    }

    @RequestMapping("test3")
    @TargetDataSource(name = "test")
    public void school3(){
        try {
            schoolService.insert();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @RequestMapping("test4")
    @TargetDataSource(name = "test")
    public void school4(){
        try {
            schoolService.insertThrows();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @RequestMapping("test5")
    public void school5(){
        try {
            insertThrows();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Transactional()
    public Integer insert(){
        School school = new School();
        school.setSchoolName("myschool-insert");
        school.setCreateTime(new Date());
        school.setStatus("00A");
        school.setStatusTime(new Date());
        return schoolMapper.insert(school);
    }
    @Transactional
    public Integer insertThrows() throws RuntimeException{
        School school = new School();
        school.setSchoolName("myschool-insertThrows");
        school.setCreateTime(new Date());
        school.setStatus("00A");
        school.setStatusTime(new Date());
        schoolMapper.insert(school);
        throw new RuntimeException();
    }
}
