package org.is.bupt.banksystemmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.is.bupt.banksystemmanagement.common.AccountStatus;
import org.is.bupt.banksystemmanagement.common.AccountType;
import org.is.bupt.banksystemmanagement.common.R;
import org.is.bupt.banksystemmanagement.entities.Account;
import org.is.bupt.banksystemmanagement.mapper.AccountMapper;
import org.is.bupt.banksystemmanagement.service.AccountService;
import org.is.bupt.banksystemmanagement.service.UserService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {


    public AccountServiceImpl() {
    }

    @Override
    public Map<String, String> findFromRedis(long accNo) {
        Map<String, String> res;
        try (JedisPool pool = new JedisPool("localhost", 6379);Jedis jedis = pool.getResource()) {
            res = jedis.hgetAll(String.valueOf(accNo));
        }
        return res;
    }

    @Override
    public void writeToRedis(String key, Map<String, String> hmap){
        try (JedisPool pool = new JedisPool("localhost", 6379);Jedis jedis = pool.getResource()) {
            if(hmap!=null) jedis.hset(key, hmap);
        }
    }

    @Override
    public R<Account> login(AccountService accountService, HttpServletRequest httpServletRequest, long accNo, String pin) {
        Map<String, String> res = findFromRedis(accNo);
        Account acc;
        if (res.isEmpty()) {
            System.out.println("\033[1;94m"+"Loading from MySQL...");
            LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Account::getAccNo, accNo);
            acc = accountService.getOne(lambdaQueryWrapper);
            if (acc == null) {
                System.out.println("\033[1;31m"+"Account does not exist");
                return R.error("acc dose not exist.");
            }
            if (!acc.getPin().equals(pin)) {
                System.out.println("\033[1;31m"+"PIN is wrong");
                return R.error("wrong pin");
            }
            if (acc.getStatus() == AccountStatus.SUSPENDED) {
                System.out.println("\033[1;31m"+"Account is suspended");
                return R.error("acc suspended.");
            }
            res = new HashMap<>();
            res.put("accNo",String.valueOf(accNo));
            res.put("userId", String.valueOf(acc.getUserId()));
            res.put("accType",acc.getAccType().toString());
            res.put("status", acc.getStatus().toString());
            res.put("pin", acc.getPin());
            res.put("overdraftLimit",String.valueOf(acc.getOverdraftLimit()));
            res.put("balance",String.valueOf(acc.getBalance()));
            res.put("unclearedBalance",String.valueOf(acc.getUnclearedBalance()));
            res.put("savingTime",String.valueOf(acc.getSavingTime()));
            writeToRedis(String.valueOf(accNo), res);
            writeToRedis(String.valueOf(acc.getUserId()),res);
        }else{
            System.out.println("\033[1;94m"+"Loading from Redis...");
            acc = new Account();
            acc.setAccNo(Long.parseLong(res.get("accNo")));
            acc.setUserId(Long.parseLong(res.get("userId")));
            acc.setAccType(AccountType.valueOf(res.get("accType")));
            acc.setStatus(AccountStatus.valueOf(res.get("status")));
            acc.setPin(res.get("pin"));
            acc.setOverdraftLimit(Double.parseDouble(res.get("overdraftLimit")));
            acc.setBalance(Double.parseDouble(res.get("balance")));
            acc.setUnclearedBalance(Double.parseDouble(res.get("unclearedBalance")));
            acc.setSavingTime(Integer.parseInt(res.get("savingTime")));
        }

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("accNo", acc.getAccNo());
        session.setAttribute("pin", acc.getPin());
        session.setAttribute("userId", acc.getUserId());

        return R.success(acc);
    }

    public R<Account> register(Account account, UserService userService, AccountMapper accountMapper) throws Exception {
        if(account.getUserId()==0||account.getPin()==null) return R.error("AccNo/Pin needed");
        var wrapper2 = new UserServiceImpl().findById(account.getUserId());
        var user = userService.getOne(wrapper2);
//        var user = new UserController().findById(account.getUserId());
        if(user==null) return R.error("User does not exist. Register user first");

        int age = new UserServiceImpl().getAge(user.getBirth());
        if(age<16) account.setAccType(AccountType.JUNIOR_ACCOUNT);
        account.setAccNo(System.currentTimeMillis());
        account.setStatus(AccountStatus.ACTIVE);


        if(account.getAccType()==null) account.setAccType(AccountType.CURRENT_ACCOUNT);
        if(account.getAccType().equals(AccountType.CURRENT_ACCOUNT)){
            account.setSavingTime(0);
            account.setUnclearedBalance(0);
            account.setOverdraftLimit(1000);
        } else if(account.getAccType().equals(AccountType.SAVING_ACCOUNT)){
            account.setUnclearedBalance(0);
            account.setOverdraftLimit(0);
        } else {
            account.setUnclearedBalance(0);
            account.setOverdraftLimit(0);
            account.setSavingTime(0);
        }
        accountMapper.insert(account);
        return R.success(account);
    }


    public R<Account> withdraw(HttpServletRequest httpServletRequest, AccountService accountService, double amount){
        var session = httpServletRequest.getSession();
        if(session==null) return R.error("Session expired");
        var accNo = session.getAttribute("accNo");
        var pin = session.getAttribute("pin");
        var lambdaQueryWrapper = new LambdaQueryWrapper<Account>();
        lambdaQueryWrapper.eq(Account::getAccNo,accNo);
        var acc = accountService.getOne(lambdaQueryWrapper);
        double balance = acc.getBalance();
        double limit = acc.getOverdraftLimit();
        if(balance+limit<amount) return R.error("not enough balance.");
        balance-=amount;
        if(balance<0) limit+=balance;
        var account = new Account();
        account.setAccNo(acc.getAccNo());
        account.setUserId(acc.getUserId());
        account.setAccType(acc.getAccType());
        account.setStatus(acc.getStatus());
        account.setPin(acc.getPin());
        account.setOverdraftLimit(limit);
        account.setBalance(balance);
        account.setUnclearedBalance(acc.getUnclearedBalance());
        account.setSavingTime(acc.getSavingTime());
        accountService.update(account,lambdaQueryWrapper);
        return R.success(account);
    }


    public String logout(HttpSession session){
//        var accNo = new Object();
        if(session!=null){
            var accNo = session.getAttribute("accNo");
            session.removeAttribute("accNo");
            session.removeAttribute("pin");
            session.removeAttribute("userId");
            return "Account "+accNo.toString()+" log out.";
        }else return "No session";

    }

    public R<Account> deposit(HttpServletRequest httpServletRequest, AccountService accountService, double amount){
        var session = httpServletRequest.getSession();
        if(session==null) return R.error("Session expired");
        var accNo = session.getAttribute("accNo");
        var lambdaQueryWrapper = new LambdaQueryWrapper<Account>();
        lambdaQueryWrapper.eq(Account::getAccNo,accNo);
        var acc = accountService.getOne(lambdaQueryWrapper);
        var account = new Account();
        account.setAccNo(acc.getAccNo());
        account.setUserId(acc.getUserId());
        account.setAccType(acc.getAccType());
        account.setStatus(acc.getStatus());
        account.setPin(acc.getPin());
        account.setOverdraftLimit(acc.getOverdraftLimit());
        account.setBalance(acc.getBalance()+amount);
        account.setUnclearedBalance(acc.getUnclearedBalance());
        account.setSavingTime(acc.getSavingTime());
        accountService.update(account,lambdaQueryWrapper);
        return R.success(account);
    }
}
