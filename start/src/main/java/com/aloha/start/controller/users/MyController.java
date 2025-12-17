package com.aloha.start.controller.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.start.domain.users.CustomUser;
import com.aloha.start.domain.users.Users;
import com.aloha.start.service.inter.users.UserService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/my")
public class MyController {

  @Autowired UserService userService;

  @GetMapping("")
  public String my() {
    return "page/my/my";
  }




  @GetMapping("/edit")
  public String edit(
    @AuthenticationPrincipal CustomUser customUser,
    Model model
  ) {
    // 사용자 정보 조회
    if (customUser == null || customUser.getUser() == null) {
      log.warn("사용자 정보가 없습니다.");
      model.addAttribute("error", "사용자 정보를 찾을 수 없습니다.");
      return "page/my/error";
    }
    Users user = userService.select(customUser.getUser().getNo());
    model.addAttribute("user", user);
    log.info("Editing user: {}", user);
    return "page/my/edit";
  }

  @GetMapping("/delete")
  public String delete(
    @AuthenticationPrincipal CustomUser customUser,
    Model model
  ) {
    // 사용자 정보 조회
    if (customUser == null || customUser.getUser() == null) {
      log.warn("사용자 정보가 없습니다.");
      model.addAttribute("error", "사용자 정보를 찾을 수 없습니다.");
      return "page/my/error";
    }
    Users user = userService.select(customUser.getUser().getNo());
    model.addAttribute("user", user);
    log.info("Deleting user: {}", user);
    
    return "page/my/delete";
  }


}
