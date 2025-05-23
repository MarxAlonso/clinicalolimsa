package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Integer> {

    public AppUser findByEmail(String email);
}

