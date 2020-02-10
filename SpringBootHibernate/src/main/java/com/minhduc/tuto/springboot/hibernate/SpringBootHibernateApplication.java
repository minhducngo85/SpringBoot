package com.minhduc.tuto.springboot.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.minhduc.tuto.springboot.hibernate.dao.BankAccountDao;
import com.minhduc.tuto.springboot.hibernate.entity.BankAccount;

@SpringBootApplication
public class SpringBootHibernateApplication implements ApplicationRunner {

    @Autowired
    BankAccountDao bankAccountDao;

    public static void main(String[] args) {
	SpringApplication.run(SpringBootHibernateApplication.class, args);
    }

    /**
     * used to execute the code after the Spring Boot application started.
     */
    @Override
    public void run(ApplicationArguments arg0) throws Exception {
	for (int i = 0; i <= 10; i++) {
	    BankAccount anAccount = new BankAccount();
	    anAccount.setFullName("Minh Duc[" + i + "]");
	    anAccount.setBalance(30000d);
	    if (bankAccountDao.findByAttribute("fullName", anAccount.getFullName()) == null) {
		System.out.println(bankAccountDao.save(anAccount));
	    }
	}
    }
}
