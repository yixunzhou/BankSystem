package org.is.bupt.banksystemmanagement.control;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.is.bupt.banksystemmanagement.common.AccountStatus;
import org.is.bupt.banksystemmanagement.common.AccountType;
import org.is.bupt.banksystemmanagement.common.R;
import org.is.bupt.banksystemmanagement.entities.Account;
import org.is.bupt.banksystemmanagement.mapper.AccountMapper;
import org.is.bupt.banksystemmanagement.mapper.UserMapper;
import org.is.bupt.banksystemmanagement.service.AccountService;
import org.is.bupt.banksystemmanagement.service.UserService;
import org.is.bupt.banksystemmanagement.service.impl.AccountServiceImpl;
import org.is.bupt.banksystemmanagement.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private AccountMapper accountMapper;

//    @PostMapping("/login")
//    public R<Account> login(HttpServletRequest request, @RequestBody Account account){
//
//        String pin = account.getPin();
//
//        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(Account::getAccNo, account.getAccNo());
//        Account acc = accountService.getOne(lambdaQueryWrapper);
//
//        if(acc==null) return R.error("acc dose not exist.");
//        if(!acc.getPin().equals(pin)) return R.error("wrong pin");
//        if(acc.getStatus()==AccountStatus.SUSPENDED) return R.error("acc suspended.");
//
//        request.getSession().setAttribute("account", acc.getAccNo());
//
//        return R.success(acc);
//    }


    @PostMapping("/login")
    public R<Account> login(HttpServletRequest request, @RequestParam long accNo, @RequestParam String pin){
      return this.accountService.login(accountService,request,accNo,pin);

    }


    @RequestMapping("/logout")
    public String logout(HttpSession session){
        return new AccountServiceImpl().logout(session);
    }


    @RequestMapping("/test")
    public Object test(){
        return httpServletRequest.getSession().getAttribute("accNo");
    }

    @RequestMapping("/withdraw")
    public R<Account> withdraw(@RequestParam double amount){
        return new AccountServiceImpl().withdraw(httpServletRequest,accountService,amount);
    }

    @RequestMapping("/register")
    public R<Account> register(@RequestBody Account account) throws Exception {
        return new AccountServiceImpl().register(account, userService, accountMapper);
    }


    @RequestMapping("/deposit")
    public R<Account> deposit(@RequestParam double amount){
        return new AccountServiceImpl().deposit(httpServletRequest, accountService, amount);

    }


}
