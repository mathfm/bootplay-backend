package br.com.sysmap.bootcamp.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

}
