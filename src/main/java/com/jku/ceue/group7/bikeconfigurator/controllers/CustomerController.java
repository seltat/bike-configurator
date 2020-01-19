package com.jku.ceue.group7.bikeconfigurator.controllers;
import com.jku.ceue.group7.bikeconfigurator.entities.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {
    public static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private Customer customer;


    @PostMapping(value = "/login")
    public String loginCustomer(@ModelAttribute Customer customer, Model model){
        this.customer = customer;
        log.info(customer.getFname() +" "+ customer.getLname() + " is created.");
        model.addAttribute("fname", customer.getFname());
        model.addAttribute("lname", customer.getLname());
        return "loginResult";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("customer", new Customer());
        return "login";
    }



}
