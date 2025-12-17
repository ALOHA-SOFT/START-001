package com.aloha.start.service.impl.users;

import org.springframework.stereotype.Service;

import com.aloha.start.domain.users.UserAuth;
import com.aloha.start.mapper.users.UserAuthMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.users.UserAuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserAuthServiceImpl extends BaseServiceImpl<UserAuth, UserAuthMapper> implements UserAuthService {

}
