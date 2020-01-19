package com.jku.ceue.group7.bikeconfigurator.controllers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class ConfigurationController {

    private String lenkertyp_choice;
    private String material_choice;
    private String schaltung_choice;
    private String griff_choice;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public void resetForm() {
        this.griff_choice = null;
        this.schaltung_choice = null;
        this.material_choice = null;
        this.lenkertyp_choice = null;
    }

    @PostMapping("/order")
    public String order (RestTemplate restTemplate, Model model) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("lenkertyp", this.lenkertyp_choice);
        map.add("material", this.material_choice);
        map.add("schaltung", this.schaltung_choice);
        map.add("griff", this.griff_choice);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        URI res = restTemplate.postForLocation("https://www.maripavi.at/bestellung", request);
        model.addAttribute("order_id", res.getPath());
        resetForm();
        return ("order_confirmed");
    }

    @GetMapping("/configurator")
    public String configure(@RequestParam(name="lenkertyp_choice", required=false) String lenkertyp_choice, @RequestParam(name="material_choice", required=false) String material_choice, @RequestParam(name="schaltung_choice", required=false) String schaltung_choice, @RequestParam(name="griff_choice", required=false) String griff_choice, Model model, RestTemplate restTemplate) {
        System.out.println(this.lenkertyp_choice + "(1)");
        String[] lenkertypen = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/lenkertyp", String[].class);
        String[] alleMaterialien = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/material", String[].class);
        String[] alleSchaltungen = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/schaltung", String[].class);
        String[] alleGriffe = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/griff", String[].class);

        Map<String, Boolean> materialien = new HashMap<String, Boolean>();
        Map<String, Boolean> schaltungen = new HashMap<String, Boolean>();
        Map<String, Boolean> griffe = new HashMap<String, Boolean>();

        for(String material:alleMaterialien) {
            materialien.put(material,false);
        }
        for(String schaltung:alleSchaltungen) {
            schaltungen.put(schaltung,false);
        }
        for(String griff:alleGriffe) {
            griffe.put(griff,false);
        }

//Handling input
        for(String t:lenkertypen) {
            if (t.equals(lenkertyp_choice)) this.lenkertyp_choice = lenkertyp_choice;
        }
        for(String t:alleMaterialien) {
            if (t.equals(material_choice)) this.material_choice = material_choice;
        }
        for(String t:alleSchaltungen) {
            if (t.equals(schaltung_choice)) this.schaltung_choice = schaltung_choice;
        }
        for(String t:alleGriffe) {
            if (t.equals(griff_choice)) this.griff_choice = griff_choice;
        }
        //

        //Materialien-Handling
        String[] verf_materialien = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/material?lenkertyp="+this.lenkertyp_choice, String[].class);

        boolean found = false;
        for (String material:verf_materialien) {
            materialien.put(material, true);
            if (this.material_choice == null || this.material_choice.equals(material)) found = true;
        }
        if (!found)  this.material_choice = "-";
        //
        //Schaltungen-Handling
        String[] verf_schaltungen = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/schaltung?lenkertyp="+this.lenkertyp_choice, String[].class);

        found = false;
        for (String schaltung:verf_schaltungen) {
            schaltungen.put(schaltung, true);
            if (this.schaltung_choice == null || this.schaltung_choice.equals(schaltung)) found = true;
        }
        if (!found) this.schaltung_choice = "-";
        //
        //Griff-Handling
        String[] verf_griffe = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/griff?material="+this.material_choice, String[].class);

        found = false;
        for (String griff:verf_griffe) {
            griffe.put(griff, true);
            if (this.griff_choice == null || this.griff_choice.equals(griff)) found = true;
        }
        if (!found) this.griff_choice = "-";
        //

        System.out.println(this.lenkertyp_choice + "(2)");
        model.addAttribute("lenkertyp_choice", this.lenkertyp_choice);
        model.addAttribute("material_choice", this.material_choice);
        model.addAttribute("schaltung_choice", this.schaltung_choice);
        model.addAttribute("griff_choice", this.griff_choice);
        model.addAttribute("materialien", materialien);
        model.addAttribute("schaltungen", schaltungen);
        model.addAttribute("griffe", griffe);
        model.addAttribute("lenkertypen", lenkertypen);
        return "configuration";
    }
}
