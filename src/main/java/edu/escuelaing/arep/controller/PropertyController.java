package edu.escuelaing.arep.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.escuelaing.arep.model.Property;
import edu.escuelaing.arep.service.PropertyService;

@CrossOrigin(origins = "https://backendoscar.duckdns.org", allowCredentials = "true")
@RestController
@RequestMapping("/v1/property")
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.findById(id);
        return property.isPresent() ? ResponseEntity.ok(property.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Property> createProperty(@RequestBody Property property){
        Property propertyCreated = propertyService.createProperty(property);
        return ResponseEntity.status(201).body(propertyCreated);
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> property = propertyService.findAllProperties();
        return ResponseEntity.ok(property);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id){
        Boolean exist = false;
        try{
            propertyService.deletePropertyById(id);
            exist = true;
        }catch(Exception e){
            e.getMessage();
        }
        return exist ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Property> updateProperty(@RequestBody Property property) {
        try {
            Property updatedProperty = propertyService.updateProperty(property);
            return ResponseEntity.ok(updatedProperty);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}
