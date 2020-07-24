package com.unifun.mySpring.repository.models;

import com.unifun.mySpring.models.Cars;
import org.springframework.data.repository.CrudRepository;



public interface CarsRepository extends CrudRepository<Cars, Integer> {
}
