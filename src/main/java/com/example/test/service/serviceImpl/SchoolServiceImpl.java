package com.example.test.service.serviceImpl;

import com.example.test.dao.SchoolMapper;
import com.example.test.pojo.School;
import com.example.test.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    SchoolMapper schoolMapper;

    @Transactional
    @Override
    public Integer insert() throws RuntimeException{
        School school = new School();
        school.setSchoolName("myschool-insert");
        school.setCreateTime(new Date());
        school.setStatus("00A");
        school.setStatusTime(new Date());
        return schoolMapper.insert(school);
    }
    @Transactional
    @Override
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
