package com.checkers.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Kutsyk on 03.10.2015.
 */

@Controller
public class IndexController{

    @RequestMapping(value = {"/"})
    public String desktop() {
        return "main/index";
    }

}
