package edu.escuelaing.arep.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private Integer price;
    private String size;
    private String description;

    private Property() {
    }

    public Property(String address, Integer price, String size, String description) {
        this.address = address;
        this.price = price;
        this.size = size;
        this.description = description;
    }

    public Long getId(){
        return id;
    }

    public String getAddress() {
        return address;
    }
    
    public Integer getPrice(){
        return price;
    }

    public String getSize(){
        return size;
    }

    public String getDescription(){
        return description;
    }

    public void setAddress(String address) {
        this.address=address;
    }
    
    public void setPrice(Integer price){
        this.price = price;
    }

    public void setSize(String size){
        this.size = size;
    }

    public void setDescription(String description){
        this.description=description;
    }
}
