package com.example.EHotel.controllers;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/customer")
public class CustomerController {

    @GetMapping("")
    public String showCustomerHome() {
        return "customer";
    }


}
