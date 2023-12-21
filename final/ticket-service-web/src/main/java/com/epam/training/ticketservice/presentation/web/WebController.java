package com.epam.training.ticketservice.presentation.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//TODO could split these into separate modules
@Controller
public class WebController {
    @GetMapping("/")
    public String getHomepage(){
        return "index";
    }
}
