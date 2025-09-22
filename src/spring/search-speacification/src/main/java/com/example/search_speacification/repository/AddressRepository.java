package com.example.search_speacification.repository;

import com.example.search_speacification.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
