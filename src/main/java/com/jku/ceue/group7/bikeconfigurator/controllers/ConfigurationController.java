package com.jku.ceue.group7.bikeconfigurator.controllers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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

    @GetMapping("/configurator")
    public String greeting(@RequestParam(name="lenkertyp_choice", required=false) String lenkertyp_choice, @RequestParam(name="material_choice", required=false) String material_choice, @RequestParam(name="schaltung_choice", required=false) String schaltung_choice, @RequestParam(name="griff_choice", required=false) String griff_choice, Model model, RestTemplate restTemplate) {
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

        for (String material:verf_materialien) {
            materialien.put(material, true);
        }
        //
        //Schaltungen-Handling
        String[] verf_schaltungen = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/schaltung?lenkertyp="+this.lenkertyp_choice, String[].class);

        for (String schaltung:verf_schaltungen) {
            schaltungen.put(schaltung, true);
        }
        //
        //Griff-Handling
        String[] verf_griffe = restTemplate.getForObject(
                "https://www.maripavi.at/produkt/griff?material="+this.material_choice, String[].class);

        for (String griff:verf_griffe) {
            griffe.put(griff, true);
        }
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
