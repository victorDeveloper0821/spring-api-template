package idv.victor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String loginUrl(){
        return "";
    }
}
