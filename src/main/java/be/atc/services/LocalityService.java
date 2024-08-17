package be.atc.services;

import be.atc.entities.Locality;
import java.util.List;
import java.util.Optional;

public interface LocalityService {
    Optional<Locality> findById(int id);
    List<Locality> findAll();
}

