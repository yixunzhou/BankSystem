package org.is.bupt.banksystemmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.is.bupt.banksystemmanagement.common.R;
import org.is.bupt.banksystemmanagement.entities.Account;

import java.util.Map;

public interface AccountService extends IService<Account> {

    Map<String, String> findFromRedis(long accNo);
    void writeToRedis(String key, Map<String, String> map);
    R<Account> login(AccountService accountService, HttpServletRequest httpServletRequest, long accNo, String pin);
}
