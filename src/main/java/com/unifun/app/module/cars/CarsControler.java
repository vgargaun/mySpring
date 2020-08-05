package com.unifun.app.module.cars;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unifun.app.components.jsoncomponents.JsonComponent;
import com.unifun.app.components.validation.Validation;
import com.unifun.app.models.Cars;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping(path = "/cars")
public class CarsControler {
    public static Logger logger = Logger.getLogger(CarsControler.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping(path = "/add")
    public @ResponseBody
    Object addNewCars(@RequestParam(defaultValue = "") String model, @RequestParam(defaultValue = "") String color,
                      HttpServletResponse resp) throws IOException, ServletException {

        JsonComponent j = new JsonComponent();

        HashMap<String, LinkedList> map = new HashMap<>();
        LinkedList<String> carName = new LinkedList<String>();
        LinkedList<String> colorName = new LinkedList<>();
        carName.add(model);
        carName.add("required");
        carName.add("max6");
        carName.add("min2");
        carName.add("[A-Z]+");
        map.put("model", carName);

        colorName.add(color);
        colorName.add("required");
        colorName.add("color");
        map.put("color", colorName);

        try {

            Validation validation = new Validation();

            HashMap<String, String> keyName = validation.validation(map);
            boolean val = true;
            for (Map.Entry<String, String> map1 : keyName.entrySet()) {
                if (map1.getValue() == null) {
                    val = false;
                    break;
                } else if (map1.getValue().equals("no required")) map1.setValue(null);
            }

            if (val) {
                Cars car = new Cars();
                car.setModel(keyName.get("model"));
                car.setColor(keyName.get("color"));

                jdbcTemplate.update("INSERT INTO cars (model, color) VALUES (?,?)", car.getModel(), car.getColor());

                resp.setStatus(200);
                logger.info("Saved new car ");
                return j.NoErrorMessage();

            } else {
                logger.warn("Bad request param");
                resp.setStatus(400);
                resp.getWriter().write(j.ErrorMessage(1, "Is bad request", HttpStatus.BAD_REQUEST));
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

            Validation validation = new Validation();
            long get_id = validation.validId(id);
            if (get_id > 0) {
                int aux = jdbcTemplate.update("DELETE FROM cars WHERE id = ?", Long.parseLong(id));
                if (aux > 0) {
                    resp.setStatus(200);
                    logger.info("Car, was deleted");
                    return j.NoErrorMessage();
                } else {
                    resp.setStatus(400);
                    logger.warn("This row don't exist ");
                    return j.ErrorMessage(2, "This row don't exist", HttpStatus.BAD_REQUEST);
                }
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
    Object list(@RequestParam(defaultValue = "") String id, @RequestParam(defaultValue = "") String model, @RequestParam(defaultValue = "") String color,
                    HttpServletResponse resp) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();

        HashMap<String, LinkedList> map = new HashMap<>();
        LinkedList<String> carName = new LinkedList<String>();
        LinkedList<String> colorName = new LinkedList<>();
        carName.add(model);
//        carName.add("required");
        carName.add("max6");
        carName.add("min2");
        carName.add("[A-Z]+");
        map.put("model", carName);

        colorName.add(color);
//        colorName.add("required");
        colorName.add("color");
        map.put("color", colorName);

        try {
            Validation validation = new Validation();
            HashMap<String, String> keyName = validation.validation(map);
            for (Map.Entry<String, String> map1 : keyName.entrySet()) {
                if (map1.getValue().equals("no required")) map1.setValue("");
            }
            Cars car = new Cars();
            car.setModel(keyName.get("model"));
            car.setColor(keyName.get("color"));
            RowMapper<Cars> rowMapper = new RowMapper<Cars>() {
                @Override
                public Cars mapRow(ResultSet resultSet, int row) throws SQLException {
                    long id = resultSet.getLong("id");
                    String model = resultSet.getString("model");
                    String color = resultSet.getString("color");
                    return new Cars(id, model, color);
                }
            };

            List<Cars> listCars = null;
            keyName.put("id ", id);
            for (Map.Entry<String, String> map1 : keyName.entrySet()) {
                if (map1.getValue().equals("no required") || map1.getValue() == null) map1.setValue("");
            }

            String sql = "SELECT * FROM cars ";
            if (!car.getModel().isEmpty() || !car.getColor().isEmpty() || validation.validId(id) > 0) {
                sql = sql + " WHERE 1=1 ";

                for (Map.Entry<String, String> map1 : keyName.entrySet()) {
                    if (!map1.getValue().isEmpty())
                        sql = sql + " AND " + map1.getKey() + " = '" + map1.getValue() + "'";

                }
            }

            listCars = jdbcTemplate.query(sql, rowMapper);

            resp.setStatus(200);
            logger.info("Displaying the list");
            return listCars;

        } catch (Exception e) {
            resp.setStatus(400);
            logger.warn("Error for displaying the list " + e);
            return j.ErrorMessage(4, "Error for displaying the list", HttpStatus.BAD_REQUEST);
        }

    }
}
