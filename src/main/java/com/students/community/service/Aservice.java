package com.students.community.service;

import com.students.community.dao.Adao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")
public class Aservice {
    @Autowired
    private Adao adao;
    public Aservice(){
        System.out.println("实例化");
    }
    @PostConstruct
    public void init(){
        System.out.println("初始化service");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("销毁");
    }

    public String find(){
        return adao.select();
    }
}
