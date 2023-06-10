package org.is.bupt.banksystemmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.is.bupt.banksystemmanagement.entities.Account;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {

}