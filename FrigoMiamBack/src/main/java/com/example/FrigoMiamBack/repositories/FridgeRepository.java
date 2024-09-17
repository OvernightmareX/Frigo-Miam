package com.example.FrigoMiamBack.repositories;

import com.example.FrigoMiamBack.entities.Fridge;
import com.example.FrigoMiamBack.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, UUID> {
}
