package com.minhduc.tuto.springboot.hibernate.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.minhduc.tuto.springboot.hibernate.entity.BankAccount;
import com.minhduc.tuto.springboot.hibernate.exception.BankTransactionException;

@Repository
@Transactional
public class BankAccountDao extends AbstractDao<BankAccount, Long> {

    /**
     * Support a current transaction, create new one if none exists.
     * 
     * @param id
     * @param amount
     * @throws BankTransactionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAmount(long id, double amount) throws BankTransactionException {
	BankAccount account = this.findById(id);
	if (account == null) {
	    throw new BankTransactionException("Account not found " + id);
	}
	double newBalance = account.getBalance() + amount;
	if (account.getBalance() + amount < 0) {
	    throw new BankTransactionException("The money in the account '" + id + "' is not enough (" + account.getBalance() + ")");
	}
	account.setBalance(newBalance);
    }

    /**
     * create a new session for this transaction
     * 
     * Do not catch BankTransactionException in this method.
     * 
     * Rollback if BankTransactionException occurred
     *
     * 
     * @param fromAccountId
     * @param toAccountId
     * @param amount
     * @throws BankTransactionException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BankTransactionException.class)
    public void sendMoney(long fromAccountId, long toAccountId, double amount) throws BankTransactionException {
	addAmount(toAccountId, amount);
	addAmount(fromAccountId, -amount);
    }
}
