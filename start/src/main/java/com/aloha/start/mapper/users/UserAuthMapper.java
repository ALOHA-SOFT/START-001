package com.aloha.start.mapper.users;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aloha.start.domain.users.UserAuth;

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {

}
