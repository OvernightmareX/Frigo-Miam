package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Fridge;
import com.example.FrigoMiamBack.entities.Fridge_Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Fridge_Id> {
}
