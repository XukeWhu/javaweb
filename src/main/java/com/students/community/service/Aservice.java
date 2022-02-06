package com.students.community.service;

import com.students.community.dao.Adao;
import com.students.community.dao.DiscussPostMapper;
import com.students.community.dao.UserMapper;
import com.students.community.entity.DiscussPost;
import com.students.community.entity.User;
import com.students.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
//@Scope("prototype")
public class Aservice {

    private static final Logger logger = LoggerFactory.getLogger(Aservice.class);

    @Autowired
    private Adao adao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate template;

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


    // REQUIRED: 支持当前事务（外部事务），A调用B，则A为当前事务，若A无事务，则按B创建事务
    // REQUIRES_NEW：创建新事务，并暂停当前事务
    // NESTED：如果当前事务存在，则嵌套到当前事务中执行（具有独立的提交和回滚），否则创建事务
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1(){
        // 新增用户
        User user = new User();
        user.setUsername("tt");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
        user.setEmail("11@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("hello");
        post.setContent("新人来咯");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("sda");

        return "ok";
    }

    public Object save2(){
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return template.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 新增用户
                User user = new User();
                user.setUsername("tb");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
                user.setEmail("tb@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/999.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("哈哈");
                post.setContent("你知道我吗？");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("sda");
                return "ok";
            }
        });
    }

    // 让该方法可以在多线程环境下被异步调用
    @Async
    public void execute1(){
        logger.debug("execute1!");
    }

    //直接触发定时线程池
    //@Scheduled(initialDelay = 10000,fixedDelay = 1000)
    public void execute2(){
        logger.debug("execute2!");
    }

}
