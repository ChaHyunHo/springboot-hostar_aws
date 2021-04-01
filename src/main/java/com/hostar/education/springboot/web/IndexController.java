package com.hostar.education.springboot.web;

import com.hostar.education.springboot.config.auth.LoginUser;
import com.hostar.education.springboot.config.auth.dto.SessionUser;
import com.hostar.education.springboot.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession session;


    /**
     *
     * @param model
     * @param user 기존에 seesion.getAttribute("user")로 가져오던 세션 정보 값이 개선, 어느 컨트롤러든 @LoginUser만 사용하면 세션 정보를 가져올수 있음
     * @return
     */
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());

        if(user != null) model.addAttribute("userName", user.getName());

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("post", postsService.findById(id));
        return "posts-update";
    }
}
