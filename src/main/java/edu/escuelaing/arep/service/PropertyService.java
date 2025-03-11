package edu.escuelaing.arep.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.escuelaing.arep.model.Property;
import edu.escuelaing.arep.repository.PropertyRepository;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository= propertyRepository;
    }

    public Property createProperty(Property property){
        return propertyRepository.save(property);
    }

    public List<Property> findAllProperties(){
        return propertyRepository.findAll();
    }

    public Optional<Property> findById(Long id){
        return propertyRepository.findById(id);
    }

    public void deletePropertyById(Long id){
        propertyRepository.deleteById(id);
    }

    
    public Property updateProperty(Property property){
        Optional<Property> optionalProperty = propertyRepository.findById(property.getId());
        if (optionalProperty.isPresent()) {
            Property propertyToUpdate = optionalProperty.get();
            propertyToUpdate.setAddress(property.getAddress());
            propertyToUpdate.setPrice(property.getPrice());
            propertyToUpdate.setSize(property.getSize());
            propertyToUpdate.setDescription(property.getDescription());

            return propertyRepository.save(propertyToUpdate);
        }else{
            return null;
        }
    }
    
}
