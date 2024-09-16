package com.example.FrigoMiamBack.factories;

import com.example.FrigoMiamBack.entities.Ingredient;
import com.example.FrigoMiamBack.utils.enums.Allergy;
import com.example.FrigoMiamBack.utils.enums.TypeIngredient;
import com.example.FrigoMiamBack.utils.enums.Unit;

import java.util.UUID;

public class IngredientFactory {
    private static final String DEFAULT_NAME = "name";
    private static final Unit DEFAULT_UNIT = Unit.CL;
    private static final TypeIngredient DEFAULT_TYPEINGREDIENT = TypeIngredient.VEGETABLE;
    private static final Allergy DEFAULT_ALLERGY = Allergy.GLUTEN;

    public static Ingredient createDefaultIngredient() {
        return Ingredient.builder()
                .name("Default Ingredient")
                .unit(Unit.GR)
                .typeIngredient(TypeIngredient.CEREALS)
                .allergy(Allergy.GLUTEN)
                .build();
    }

    public static Ingredient createIngredient(String name, Unit unit, TypeIngredient type, Allergy allergy) {
        return Ingredient.builder()
                .id(UUID.randomUUID())
                .name(name)
                .unit(unit)
                .typeIngredient(type)
                .allergy(allergy)
                .build();
    }

    public static Ingredient createIngredientWithCustomId(UUID id) {
        return Ingredient.builder()
                .id(id)
                .name("Default Ingredient")
                .unit(Unit.GR)
                .typeIngredient(TypeIngredient.CEREALS)
                .allergy(Allergy.GLUTEN)
                .build();
    }
}
