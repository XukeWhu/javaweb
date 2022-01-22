package com.students.community;

import com.students.community.service.Aservice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionTests {
    @Autowired
    private Aservice aservice;
    @Test
    public void testSave1(){
        Object obj = aservice.save1();
        System.out.println(obj);
    }

    @Test
    public void testSave2(){
        Object obj = aservice.save2();
        System.out.println(obj);
    }
}
