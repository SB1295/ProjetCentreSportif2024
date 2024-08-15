package be.atc.services;

import be.atc.entities.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    void createAddress(Address address);

    void updateAddress(Address address);

    void deleteAddressById(int id);

    Optional<Address> findById(int id);

    List<Address> findAll();

    List<Address> findByStreetName(String streetName);

    List<Address> findByLocalityId(int localityId);
}


