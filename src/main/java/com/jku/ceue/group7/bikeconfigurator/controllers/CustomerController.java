package com.jku.ceue.group7.bikeconfigurator.controllers;
import com.jku.ceue.group7.bikeconfigurator.entities.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("customer")
public class CustomerController {
    public static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private Customer customer;

    @ModelAttribute("customer")
    public Customer getCustomer() {
        return this.customer;
    }

    @Bean
    public RestTemplate rt(RestTemplateBuilder builder) {
        return builder.build();
    }

    @PostMapping(value = "/login")
    public String loginCustomer(@ModelAttribute Customer customer, Model model, RestTemplate rt){
        this.customer = customer;

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("fName", customer.getFname());
        map.add("lName", customer.getLname());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        URI res = rt.postForLocation("http://localhost:8080/CRM/customer", request);
        this.customer.setUri(res.toString());
        this.customer.setId(getIdFromUri(res));
        log.info(customer.getFname() +" "+ customer.getLname() + " is created.");
        model.addAttribute("fname", customer.getFname());
        model.addAttribute("lname", customer.getLname());
        return "redirect:configurator";
    }

    public int getIdFromUri (URI uri) {
        String u = uri.toString();
        String[] parts = u.split("/");
        return Integer.parseInt(parts[parts.length-1]);
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("customer", new Customer());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate();
        return("redirect:login");
    }
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        session.invalidate();
        return("redirect:login");
    }



}
