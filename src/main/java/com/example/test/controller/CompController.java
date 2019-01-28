package com.example.test.controller;

import com.example.test.dao.CompanyMapper;
import com.example.test.datasouce.TargetDataSource;
import com.example.test.pojo.Company;
import com.example.test.pojo.CompanyExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompController {

   /* @Autowired
    CompanyMapper companyMapper;

    @RequestMapping("test1")
    public Company company1(){
        CompanyExample example = new CompanyExample();
        CompanyExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2L);
        return  companyMapper.selectByPrimaryKey(2L);
    }
    @RequestMapping("test2")
    @TargetDataSource(name = "test")
    public Company company2(){
        CompanyExample example = new CompanyExample();
        CompanyExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2L);
        return  companyMapper.selectByPrimaryKey(2L);
    }*/
}
