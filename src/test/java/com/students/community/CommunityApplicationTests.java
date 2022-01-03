package com.students.community;

import com.students.community.dao.Adao;
import com.students.community.service.Aservice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	@Test
	public void testspring(){
		System.out.println(applicationContext);
		Adao ad=applicationContext.getBean(Adao.class);
		System.out.println(ad.select());
		ad=applicationContext.getBean("aHibernate",Adao.class);
		System.out.println(ad.select());
	}

	@Test
	public void testbean(){
		Aservice aservice=applicationContext.getBean(Aservice.class);
		System.out.println(aservice);

		aservice=applicationContext.getBean(Aservice.class);
		System.out.println(aservice);
	}
	@Test
	public void testbeanconfig(){
		SimpleDateFormat simpleDateFormat=
				applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired
	@Qualifier("aHibernate")
	private Adao ad;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Autowired
	private Aservice aservice;
	@Test
	public void testdi(){
		System.out.println(ad);
		System.out.println(aservice);
		System.out.println(simpleDateFormat);

	}

}
