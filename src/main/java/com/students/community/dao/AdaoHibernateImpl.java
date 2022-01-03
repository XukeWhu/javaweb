package com.students.community.dao;

import org.springframework.stereotype.Repository;

@Repository("aHibernate")
public class AdaoHibernateImpl implements Adao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
