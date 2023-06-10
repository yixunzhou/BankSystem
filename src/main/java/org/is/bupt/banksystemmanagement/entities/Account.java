package org.is.bupt.banksystemmanagement.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.is.bupt.banksystemmanagement.common.AccountStatus;
import org.is.bupt.banksystemmanagement.common.AccountType;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private long accNo;

    @TableField
    private long userId;
    @TableField
    private AccountType accType;
    @TableField
    private AccountStatus status;
    @TableField
    private String pin;
    @TableField
    private double overdraftLimit;
    @TableField
    private double balance;
    @TableField
    private double unclearedBalance;
    @TableField
    private int savingTime;

    public long getAccNo() {
        return accNo;
    }

    public void setAccNo(long accNo) {
        this.accNo = accNo;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public AccountType getAccType() {
        return accType;
    }

    public void setAccType(AccountType accType) {
        this.accType = accType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getUnclearedBalance() {
        return unclearedBalance;
    }

    public void setUnclearedBalance(double unclearedBalance) {
        this.unclearedBalance = unclearedBalance;
    }

    public int getSavingTime() {
        return savingTime;
    }

    public void setSavingTime(int savingTime) {
        this.savingTime = savingTime;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accNo=" + accNo +
                ", userId=" + userId +
                ", accType='" + accType + '\'' +
                ", status=" + status +
                ", pin='" + pin + '\'' +
                ", overdraftLimit=" + overdraftLimit +
                ", balance=" + balance +
                ", unclearedBalance=" + unclearedBalance +
                ", savingTime=" + savingTime +
                '}';
    }
}
