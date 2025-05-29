package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser,Integer> {

    public AppUser findByEmail(String email);
    List<AppUser> findByRole(String role);

}

