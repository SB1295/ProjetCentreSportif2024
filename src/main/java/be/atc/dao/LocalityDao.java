package be.atc.dao;

import be.atc.entities.Locality;
import java.util.List;
import java.util.Optional;

public interface LocalityDao {

    // Méthode de recherche localité par Id
    Optional<Locality> findById(int id);

    // Méthode de récupération de toutes les localités
    List<Locality> findAll();
}

