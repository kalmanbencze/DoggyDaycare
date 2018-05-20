package me.kalmanbncz.doggydaycare.domain.dog;

import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.domain.dog.api.DogJSONResult;
import me.kalmanbncz.doggydaycare.domain.dog.persistence.DogEntity;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
class DogConverter {

    public static DogEntity toEntity(Dog dog) {
        DogEntity entity = new DogEntity();
        entity.id = dog.getId();
        entity.name = dog.getName();
        entity.breed = dog.getBreed();
        entity.gender = dog.getGender();
        entity.size = dog.getSize();
        entity.yearOfBirth = dog.getYearOfBirth();
        entity.vaccinated = dog.isVaccinated();
        entity.neutered = dog.isNeutered();
        entity.friendly = dog.isFriendly();
        entity.commands = dog.getCommands();
        entity.sleepSched = dog.getSleepSched();
        entity.walkSched = dog.getWalkSched();
        entity.timestamp = System.currentTimeMillis();
        return entity;
    }

    public static DogJSONResult toJsonObject(Dog dog) {
        DogJSONResult result = new DogJSONResult();
        result.id = dog.getId();
        result.name = dog.getName();
        result.breed = dog.getBreed();
        result.gender = dog.getGender();
        result.size = dog.getSize();
        result.yearOfBirth = dog.getYearOfBirth();
        result.vaccinated = dog.isVaccinated();
        result.neutered = dog.isNeutered();
        result.friendly = dog.isFriendly();
        result.commands = dog.getCommands();
        result.sleepSched = dog.getSleepSched();
        result.walkSched = dog.getWalkSched();
        return result;
    }
}
