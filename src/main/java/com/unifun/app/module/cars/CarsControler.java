package com.unifun.app.module.cars;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unifun.app.components.jsoncomponents.JsonComponent;
import com.unifun.app.components.validation.ValidationMaiSerioasa;
import com.unifun.app.models.Cars;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@Controller
@RequestMapping(path = "/cars")
public class CarsControler {
    public static Logger logger = Logger.getLogger(CarsControler.class);
    ArrayList<Long> id = new ArrayList<>();
    @Autowired
    private CarsRepository carsRepository;

    @GetMapping(path = "/add")
    public @ResponseBody
    Object addNewCars(@RequestParam(defaultValue = "") String model, @RequestParam(defaultValue = "") String color,
                      HttpServletResponse resp) throws IOException, ServletException {

        JsonComponent j = new JsonComponent();

        HashMap<String, LinkedList> map = new HashMap<>();
        LinkedList<String> carName = new LinkedList<String>();
        LinkedList<String> colorName = new LinkedList<>();
        carName.add(model);
        carName.add("max6");
        carName.add("min2");
        carName.add("[A-Z]+");
        map.put("model",carName);
        colorName.add(color);
        map.put("color", colorName);

        try {

//            Validation validation = new Validation(color, model, 2, 6, "[A-Z]+");
            ValidationMaiSerioasa validation = new ValidationMaiSerioasa();
            if (validation.getValid()) {
                Cars car = new Cars();
                car.setModel(model);
                car.setColor(color);
                carsRepository.save(car);
                this.id.add(car.getId());
                resp.setStatus(200);
                logger.info("Saved new car ");
                return j.NoErrorMessage();

            } else {
                logger.warn("Bad request param");
                resp.setStatus(400);
                resp.getWriter().write(j.ErrorMessage(1, "Is bad request", HttpStatus.BAD_REQUEST));
                logger.info("1");
            }

        } catch (Exception e) {
            resp.setStatus(400);
            logger.warn("Don't saved " + e);
            return j.ErrorMessage(1, "Is bad request", HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    @GetMapping(path = "/deleteById")
    public @ResponseBody
    String deleteById(@RequestParam(defaultValue = "") String id, HttpServletResponse resp) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();
        try {

            ValidationMaiSerioasa validation = new ValidationMaiSerioasa(this.id, Long.parseLong(id));
            if (validation.getValid()) {
                for (int i = 0; i < this.id.size(); i++) {
                    if (Long.parseLong(id) == this.id.get(i)) {
                        this.id.remove(i);
                        break;
                    }
                }
                Cars car = new Cars();
                car.setId(Long.parseLong(id));
                carsRepository.delete(car);
                resp.setStatus(200);
                logger.info("Car, was deleted");
                return j.NoErrorMessage();
            } else {
                resp.setStatus(400);
                logger.warn("Was not deleted ");
                return j.ErrorMessage(2, "Was not deleted", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            resp.setStatus(400);
            logger.warn("Was not deleted " + e);
            return j.ErrorMessage(2, "Was not deleted", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/list")
    public @ResponseBody
    Object getAllUsers(HttpServletResponse resp) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();
        try {
            if (this.id.isEmpty()) {
                resp.setStatus(400);
                logger.warn("Data Base is Emty");
                return j.ErrorMessage(4, "Data Base is Emty", HttpStatus.BAD_REQUEST);
            }
            resp.setStatus(200);
            logger.info("Showed list ");
            return carsRepository.findAll();
        } catch (Exception e) {
            resp.setStatus(200);
            logger.warn("Error for show all " + e);
            return j.ErrorMessage(4, "Data Base is Emty", HttpStatus.BAD_REQUEST);
        }
    }
}
