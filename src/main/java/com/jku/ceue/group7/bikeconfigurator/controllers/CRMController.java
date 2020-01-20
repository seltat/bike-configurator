package com.jku.ceue.group7.bikeconfigurator.controllers;
import com.jku.ceue.group7.bikeconfigurator.entities.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

@RestController
public class CRMController {

    private ArrayList<Customer> customers = new ArrayList<>();

    @PostMapping("/CRM/customerASJSON")
    public Customer order (Model model, @RequestParam(value = "fName", defaultValue = "Max") String firstName, @RequestParam(value = "lName", defaultValue = "Mustermann") String lastName) {
        for (Customer c: this.customers) {
            if (c.getLname().equals(lastName) && c.getFname().equals(firstName)) {
                return c;
            }
        }
        Customer nCust = new Customer();
        nCust.setFname(firstName);
        nCust.setLname(lastName);
        customers.add(nCust);
        return (nCust);
    }

    @PostMapping(path= "/CRM/customer")
    public ResponseEntity<Object> addEmployee(
            @RequestParam(value = "fName", defaultValue = "Max") String firstName,
            @RequestParam(value = "lName", defaultValue = "Mustermann") String lastName
            )
            throws Exception
    {

        Customer nCust = new Customer();
        nCust.setFname(firstName);
        nCust.setLname(lastName);
        boolean found = false;
        for (Customer c: this.customers) {
            if (c.getLname().equals(lastName) && c.getFname().equals(firstName)) {
                nCust = c;
                found = true;

            }
        }
        if (!found) customers.add(nCust);

        //Create resource location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nCust.getId())
                .toUri();
        System.out.println(location.toString());
        //Send location in response
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/CRM/customer")
    public ArrayList<Customer> order (Model model) {
        return (this.customers);
    }

}
