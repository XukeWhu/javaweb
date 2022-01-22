package com.students.community;

import com.students.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter filter;

    @Test
    public void testSensitiveFilter(){
        String text = "这里可以吸毒开票,以及嫖娼赌博，嘻嘻！";
        text = filter.filter(text);
        System.out.println(text);

        text = "这里可以🔣吸%?毒?/开🔣票,以及嫖.娼赌_博，嘻嘻！";
        text = filter.filter(text);
        System.out.println(text);
    }
}
