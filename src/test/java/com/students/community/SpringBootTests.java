package com.students.community;


import com.students.community.entity.DiscussPost;
import com.students.community.service.DiscussPostService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.util.Assert;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SpringBootTests {

    @Autowired
    private DiscussPostService discussPostService;

    private DiscussPost data;

    @BeforeAll
    public static void beforeClass(){
        System.out.println("BeforeClass");
    }

    @AfterAll
    public static void afterClass(){
        System.out.println("AfterClass");
    }

    @BeforeEach
    public void before(){
        System.out.println("Before");

        // 初始化测试数据
        data = new DiscussPost();
        data.setUserId(111);
        data.setTitle("Test Title");
        data.setContent("Test Content");
        data.setCreateTime(new Date());

        discussPostService.addDiscussPost(data);
    }

    @AfterEach
    public void after(){
        System.out.println("After");

        // 删除测试数据
        discussPostService.updateStatus(data.getId(), 2);
    }

    @Test
    public void test1(){
        System.out.println("Test1");
    }

    @Test
    public void test2(){
        System.out.println("Test2");
    }

    @Test
    public void testFindById(){
        DiscussPost post = discussPostService.findDiscussPostById(data.getId());
        Assertions.assertNotNull(post);
        Assertions.assertEquals(data.getTitle(),post.getTitle());
        Assertions.assertEquals(data.getContent(),post.getContent());
    }

    @Test
    public void testUpdateScore(){
        int rows = discussPostService.updateScore(data.getId(), 2000);
        Assertions.assertEquals(1, rows);
        DiscussPost post = discussPostService.findDiscussPostById(data.getId());
        Assertions.assertEquals(2000, post.getScore(), 2);
    }
}
