package com.jku.ceue.group7.bikeconfigurator.entities;

public class Customer{
    private String fname;
    private String lname;
    private int id;
    private String uri;

    public Customer() {
        this.id = (int)(Math.random()*100000);
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id;   }

    public String getUri(){ return uri; }

    public void setUri(String uri) { this.uri = uri; }

}
