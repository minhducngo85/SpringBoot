package com.minhduc.tuto.springboot.hibernate.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.minhduc.tuto.springboot.hibernate.entity.BankAccount;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BankAccountDaoTest {
    @Autowired
    private BankAccountDao bankAccountDao;

    @Test
    public void testFindById() {
	BankAccount konto = bankAccountDao.findById(2L);
	System.out.println(konto);
    }

    @Test
    public void testFindByName() {
	BankAccount konto = bankAccountDao.findByAttribute("fullName", "Tom");
	System.out.println(konto);
    }

    @Test
    public void testFindAllAndOrderByFullName() {
	List<BankAccount> konten = bankAccountDao.findAll("balance", "DESC");
	konten.forEach(konto -> {
	    System.out.println(konto);
	});

    }

    @Test
    public void testFindAllWithDefaultSorting() {
	List<BankAccount> konten = bankAccountDao.findAll(null, null);
	konten.forEach(konto -> {
	    System.out.println(konto);
	});

    }
}
