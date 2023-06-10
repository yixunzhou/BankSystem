package org.is.bupt.banksystemmanagement.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.is.bupt.banksystemmanagement.entities.User;

import java.util.Date;

public interface UserService extends IService<User> {

    public LambdaQueryWrapper<User> findById(long userId);
//
    public int getAge(Date birth) throws Exception;

}
