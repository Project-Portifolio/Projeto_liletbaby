package br.com.liletbaby.back_end.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("liletbaby/api")
public class loginController {

    @RequestMapping(value = "/login", produces = {"text/html", "application/json"})
    @ResponseBody
    public String login(){
        return  "{nome: \"Liletbaby Back-End\"}";
    }
}
