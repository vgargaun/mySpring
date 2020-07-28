package com.unifun.app.module.cars;

import com.unifun.app.models.Cars;
import org.springframework.data.repository.CrudRepository;



public interface CarsRepository extends CrudRepository<Cars, Integer> {
}
