package com.minhduc.tuto.springboot.hibernate.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.minhduc.tuto.springboot.hibernate.dao.BankAccountDao;
import com.minhduc.tuto.springboot.hibernate.entity.BankAccount;
import com.minhduc.tuto.springboot.hibernate.exception.BankTransactionException;
import com.minhduc.tuto.springboot.hibernate.web.model.AddBankAccoutForm;
import com.minhduc.tuto.springboot.hibernate.web.model.BankAccountModel;
import com.minhduc.tuto.springboot.hibernate.web.model.SendMoneyForm;

@Controller
public class WebController {

    @Autowired
    private BankAccountDao bankAccountDao;

    /**
     * index page to view list of all accounts
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showBankAccounts(Model model) {
	List<BankAccount> entityList = bankAccountDao.findAll(null, null);
	List<BankAccountModel> list = new ArrayList<>();
	entityList.forEach(konto -> {
	    BankAccountModel webModel = new BankAccountModel(konto);
	    list.add(webModel);
	});
	model.addAttribute("accountInfos", list);
	return "accountsPage";
    }

    /**
     * request to open send money page
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/sendMoney", method = RequestMethod.GET)
    public String viewSendMoneyPage(Model model) {
	SendMoneyForm form = new SendMoneyForm(1L, 2L, 7000d);
	model.addAttribute("sendMoneyForm", form);
	return "sendMoneyPage";
    }

    /**
     * form submit
     * 
     * @param model
     * @param sendMoneyForm
     * @return
     */
    @RequestMapping(value = "/sendMoney", method = RequestMethod.POST)
    public String processSendMoney(Model model, SendMoneyForm sendMoneyForm) {
	System.out.println("Send Money::" + sendMoneyForm.getAmount());
	try {
	    bankAccountDao.sendMoney(sendMoneyForm.getFromAccountId(), //
	            sendMoneyForm.getToAccountId(), //
	            sendMoneyForm.getAmount());
	} catch (BankTransactionException e) {
	    model.addAttribute("errorMessage", "Error: " + e.getMessage());
	    return "/sendMoneyPage";
	}
	return "redirect:/";
    }

    /**
     * request to open send money page
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/addBankAccount", method = RequestMethod.GET)
    public String viewAddBankAccountPage(Model model) {
	AddBankAccoutForm form = new AddBankAccoutForm();
	model.addAttribute("addBankAccountForm", form);
	return "/addBankAccountPage";
    }

    /**
     * form submit
     * 
     * @param model
     * @param newBankAccountForm
     * @return
     */
    @RequestMapping(value = "/addBankAccount", method = RequestMethod.POST)
    public String processNewBankAccount(Model model, AddBankAccoutForm addBankAccountForm) {
	System.out.println("processNewBankAccount::" + addBankAccountForm.getBalance());
	try {
	    model.addAttribute("addBankAccountForm", addBankAccountForm);
	    BankAccount newAccount = new BankAccount();
	    newAccount.setFullName(addBankAccountForm.getFullName());
	    newAccount.setBalance(addBankAccountForm.getBalance());
	    if (newAccount.getFullName() == null || newAccount.getFullName().isEmpty()) {
		model.addAttribute("errorMessage", "Error: Name must not be empty!");
		return "/addBankAccountPage";
	    } else if (bankAccountDao.findByAttribute("fullName", newAccount.getFullName()) != null) {
		model.addAttribute("errorMessage", "Error: Name is duplicated!");
		return "/addBankAccountPage";
	    }
	    bankAccountDao.save(newAccount);
	} catch (HibernateException e) {
	    model.addAttribute("errorMessage", "Error: " + e.getMessage());
	    return "/addBankAccountPage";
	}
	return "redirect:/";
    }
}
