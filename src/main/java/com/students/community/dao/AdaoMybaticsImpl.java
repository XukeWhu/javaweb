package com.students.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class AdaoMybaticsImpl implements Adao{

    @Override
    public String select() {
        return "AdaoMybaticsImpl";
    }
}
