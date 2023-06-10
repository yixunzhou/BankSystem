package org.is.bupt.banksystemmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.is.bupt.banksystemmanagement.entities.User;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
