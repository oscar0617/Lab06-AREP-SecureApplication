package edu.escuelaing.arep.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.escuelaing.arep.model.Property;

@Repository
public interface PropertyRepository extends CrudRepository<Property, Long> {

    Optional<Property> findById(Long id);

    List<Property> findAll();
}
