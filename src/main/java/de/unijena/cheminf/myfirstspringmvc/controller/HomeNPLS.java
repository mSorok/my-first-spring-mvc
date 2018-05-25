package de.unijena.cheminf.myfirstspringmvc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeNPLS {

    @RequestMapping
    public String home(){
        return "index" ;
    }
}
