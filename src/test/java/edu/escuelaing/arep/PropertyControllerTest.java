package edu.escuelaing.arep;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import edu.escuelaing.arep.controller.PropertyController;
import edu.escuelaing.arep.model.Property;
import edu.escuelaing.arep.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPropertyById_WhenPropertyExists() {
        Long id = 1L;
        Property property = new Property("Cra 10 #20-30", 100000000, "80m2", "Apartamento bien ubicado");
        when(propertyService.findById(id)).thenReturn(Optional.of(property));
        ResponseEntity<Property> response = propertyController.getPropertyById(id);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cra 10 #20-30", response.getBody().getAddress());
    }

    @Test
    void testGetPropertyById_WhenPropertyDoesNotExist() {
        Long id = 99L;
        when(propertyService.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Property> response = propertyController.getPropertyById(id);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateProperty() {
        Property property = new Property("Calle 50 #20-15", 150000000, "100m2", "Casa de 2 pisos");
        when(propertyService.createProperty(any(Property.class))).thenReturn(property);
        ResponseEntity<Property> response = propertyController.createProperty(property);
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Calle 50 #20-15", response.getBody().getAddress());
    }

    @Test
    void testGetAllProperties() {
        List<Property> properties = Arrays.asList(
                new Property("Cra 30 #40-50", 200000000, "120m2", "Apartamento moderno"),
                new Property("Calle 80 #25-30", 180000000, "90m2", "Remodelado con vista panorámica")
        );
        when(propertyService.findAllProperties()).thenReturn(properties);
        ResponseEntity<List<Property>> response = propertyController.getAllProperties();
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testDeleteProperty_WhenExists() {
        Long id = 2L;
        doNothing().when(propertyService).deletePropertyById(id);
        ResponseEntity<Void> response = propertyController.deleteProperty(id);
        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteProperty_WhenNotExists() {
        Long id = 99L;
        doThrow(new RuntimeException("Not Found")).when(propertyService).deletePropertyById(id);
        ResponseEntity<Void> response = propertyController.deleteProperty(id);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateProperty_WhenExists() {
        Property property = new Property("Avenida 19 #45-60", 220000000, "110m2", "Dúplex con terraza");
        when(propertyService.updateProperty(any(Property.class))).thenReturn(property);
        ResponseEntity<Property> response = propertyController.updateProperty(property);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Avenida 19 #45-60", response.getBody().getAddress());
    }

    @Test
    void testUpdateProperty_WhenNotExists() {
        Property property = new Property("Calle 60 #10-20", 170000000, "85m2", "Apartamento en buen estado");
        when(propertyService.updateProperty(any(Property.class))).thenThrow(new RuntimeException("Not Found"));
        ResponseEntity<Property> response = propertyController.updateProperty(property);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}
