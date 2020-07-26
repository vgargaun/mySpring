package com.unifun.mySpring.repository.models;


import com.unifun.mySpring.models.Cars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/cars")
public class CarsControler {
    @Autowired
    private CarsRepository carsRepository;

    @GetMapping(path="/add")
    public @ResponseBody
    String addNewCars (@RequestParam String model, @RequestParam String color) {

        Cars car = new Cars();
        car.setModel(model);
        car.setColor(color);
        carsRepository.save(car);
        return "Saved";
    }

    @GetMapping(path = "/deleteById")
    public @ResponseBody String deleteById(@RequestParam long id)
    {
        Cars car = new Cars();
        car.setId(id);
        carsRepository.delete(car);
        return "Deleted ";
    }


    @GetMapping(path="/list")
    public @ResponseBody Iterable<Cars> getAllUsers() {

        return carsRepository.findAll();

    }

}
