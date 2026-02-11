package com.aloha.start.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.start.domain.admin.Popups;
import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.service.inter.admin.PopupService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;





@Slf4j
@Controller
public class HomeController {

  @Autowired private PopupService popupService;

  /**
   * 💻 메인
   * @return
   */
  @GetMapping("/")
  public String home(
    QueryParams queryParams,
    Model model
  ) {
    // 메인 팝업
    List<Popups> mainPopups = popupService.listByTypeOpen("MAIN");
    model.addAttribute("mainPopups", mainPopups);

    return "index";
  }

  /**
   * 💻 회원가입
   * @return
   */
  @GetMapping({"/join", "/signup"})
  public String getMethodName() {
      return "page/join";
  }

  /**
   * 💻 로그인
   * @return
   */
  @GetMapping({"/login", "/signin"})
  public String login(
    @CookieValue(value = "remember-id", required = false) Cookie cookie,
    Model model
  ) {
    log.info(":::::::::: 로그인 페이지 ::::::::::");
    String username = "";
    boolean rememberId = false;
    if( cookie != null ) {
        log.info("CookieName : " + cookie.getName());
        log.info("CookieValue : " + cookie.getValue());
        username = cookie.getValue();
        rememberId = true;
    }
    model.addAttribute("username", username);
    model.addAttribute("rememberId", rememberId);
    return "page/login";
  }

  /**
   * 💻 아이디 찾기
   * @return
   */
  @GetMapping("/find-id")
  public String findId() {
      log.info(":::::::::: 아이디 찾기 페이지 ::::::::::");
      return "page/find-id";
  }

  /**
   * 💻 비밀번호 찾기
   * @return
   */
  @GetMapping("/find-pw")
  public String findPassword() {
      log.info(":::::::::: 비밀번호 찾기 페이지 ::::::::::");
      return "page/find-pw";
  }

  /**
   * 이용약관
   */
  @GetMapping("/terms/{page}")
  public String termsPage(@PathVariable("page") String page) {
      return "page/terms/" + page;
  }
  

  /**
   * 고객안내
   * @return
   */
  @GetMapping("/info")
  public String info() {
      return "page/info";
  }
  
  
  /**
  * 로그아웃
  * @param param
  * @return
  */
  @GetMapping("/logout")
  public String logout(
    HttpSession session,
    HttpServletResponse response,
    @RequestParam("redirect") String redirect,
    @CookieValue(value = "remember-me", required = false) String rememberMe,
    @CookieValue(value = "remember-id", required = false) String rememberId
  ) {
    log.info("로그아웃 요청");
    // 세션 무효화
    if (session != null) {
        session.invalidate();
        Cookie cookie = new Cookie("remember-me", null);
        cookie.setMaxAge(0); // 쿠키 삭제
        Cookie cookie2 = new Cookie("remember-id", null);
        cookie2.setMaxAge(0); // 쿠키 삭제
        response.addCookie(cookie);
        response.addCookie(cookie2);
        log.info("세션 무효화 완료");
    }
    // redirect 파라미터 있으면, 해당 페이지로 이동
    if( redirect != null && !redirect.isEmpty() ) {
      return "redirect:" + redirect;
    }
    return "redirect:/";
  }
  
  // /sample
  @GetMapping("/editor")
  public String sample() {
      log.info("샘플 페이지 요청");
      return "page/editor";
  }  



}


