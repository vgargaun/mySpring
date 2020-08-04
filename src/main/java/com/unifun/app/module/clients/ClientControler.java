package com.unifun.app.module.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.unifun.app.components.jsoncomponents.JsonComponent;
import com.unifun.app.components.validation.Validation;
import com.unifun.app.models.Clients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping(path = "/clients")
public class ClientControler {
    public static Logger logger = Logger.getLogger(ClientControler.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping(path = "/add")
    public @ResponseBody
    String addNewClients(@RequestParam(defaultValue = "") String firstName, @RequestParam(defaultValue = "") String lastName,
                         HttpServletResponse resp) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();

        HashMap<String, LinkedList> map = new HashMap<>();
        LinkedList<String> firstN = new LinkedList<>();
        LinkedList<String> lastN = new LinkedList<>();


        firstN.add(firstName);
        firstN.add("required");
        firstN.add("String");
        firstN.add("min2");
        firstN.add("max7");
        firstN.add("[A-Z][a-z]+");
        map.put("firstName", firstN);


        lastN.add(lastName);
        lastN.add("min2");
        lastN.add("max9");
        lastN.add("[A-Z][a-z]+");
        map.put("lastName", lastN);

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
                Clients client = new Clients();
                client.setFirstName(keyName.get("firstName"));
                client.setLastName(keyName.get("lastName"));

                jdbcTemplate.update("INSERT INTO clients (first_name, last_name) VALUES (?,?)", client.getFirstName(), client.getLastName());

                resp.setStatus(200);
                logger.info("Saved new client");
                return j.NoErrorMessage();

            } else {
                logger.warn("Bad request param");
                resp.setStatus(400);
                return j.ErrorMessage(1, "Is bad request", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(400);
            logger.warn("Don't saved " + e);
            return j.ErrorMessage(1, "Is bad request", HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping(path = "/deleteById")
    public @ResponseBody
    String deleteById(@RequestParam(defaultValue = "") String id,
                      HttpServletResponse resp) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();

        try {
            Validation validation = new Validation();
            long get_id = validation.validId(id);
            if (get_id > 0) {
                int aux = jdbcTemplate.update("DELETE FROM clients WHERE id = ?", Long.parseLong(id));
                if (aux > 0) {
                    resp.setStatus(200);
                    logger.info("Client, was deleted");
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
    Object listById(@RequestParam(defaultValue = "") String id, @RequestParam(defaultValue = "") String firstName, @RequestParam(defaultValue = "") String lastName,
                    HttpServletResponse resp) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();
        HashMap<String, LinkedList> map = new HashMap<>();
        LinkedList<String> firstN = new LinkedList<>();
        LinkedList<String> lastN = new LinkedList<>();


        firstN.add(firstName);
//        firstN.add("required");
        firstN.add("String");
        firstN.add("min2");
        firstN.add("max7");
        firstN.add("[A-Z][a-z]+");
        map.put("firstName", firstN);


        lastN.add(lastName);
//        firstN.add("required");
        lastN.add("String");
        lastN.add("min2");
        lastN.add("max9");
        lastN.add("[A-Z][a-z]+");
        map.put("lastName", lastN);

        try {
            Validation validation = new Validation();
            HashMap<String, String> keyName = validation.validation(map);

            for (Map.Entry<String, String> map1 : keyName.entrySet()) {
                if (map1.getValue().equals("no required")) map1.setValue("");
            }

            Clients client = new Clients();
            client.setFirstName(keyName.get("firstName"));
            client.setLastName(keyName.get("lastName"));
            RowMapper<Clients> rowMapper = new RowMapper<Clients>() {
                @Override
                public Clients mapRow(ResultSet resultSet, int row) throws SQLException {
                    long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    return new Clients(id, firstName, lastName);
                }
            };

            List<Clients> listClients = null;
//            if (client.getFirstName().isEmpty() && client.getLastName().isEmpty() && validation.validId(id) == 0) {
//                listClients = jdbcTemplate.query("SELECT * FROM clients", rowMapper);
//            }
//            if (!client.getFirstName().isEmpty() && !client.getLastName().isEmpty()) {
//                listClients = jdbcTemplate.query("SELECT * FROM clients WHERE first_name= ? AND last_name = ?", rowMapper, client.getFirstName(), client.getLastName());
//            }
//            if (validation.validId(id) > 0) {
//                listClients = jdbcTemplate.query("SELECT * FROM clients WHERE id = ?", rowMapper, validation.validId(id));
//            }
//            if (!client.getFirstName().isEmpty()) {
//                listClients = jdbcTemplate.query("SELECT * FROM clients WHERE first_name = ?", rowMapper, client.getFirstName());
//            }
//            if (!(client.getLastName().isEmpty())) {
//                listClients = jdbcTemplate.query("SELECT * FROM clients WHERE last_name= ?", rowMapper, client.getLastName());
//            }



            for (Map.Entry<String, String> map1 : keyName.entrySet()) {
                if (map1.getValue().equals("no required")) map1.setValue("");
            }
            String idString, firstNameString, lastNameString, where;
            if(validation.validId(id)>0) idString = " id = ? ";
            else idString = "";
            if(!client.getFirstName().isEmpty()) firstNameString = " first_name = ? ";
            else firstNameString = "";
            if(!client.getLastName().isEmpty()) lastNameString = " last_name = ? ";
            else lastNameString = "";
            if (!client.getFirstName().isEmpty() || !client.getLastName().isEmpty() || validation.validId(id) > 0) where = " WHERE ";
            else where = "";

            List<Object> list = new ArrayList<>();



            list.add(id);
            list.add(client.getFirstName());
            list.add(client.getLastName());

            listClients = jdbcTemplate.query("(SELECT * FROM clients "+where+idString+")", rowMapper, id);

            resp.setStatus(200);
            logger.info("List");
            return listClients;

        } catch (Exception e) {
            resp.setStatus(400);
            logger.warn("Error for show all " + e);
            return j.ErrorMessage(4, "ERROR", HttpStatus.BAD_REQUEST);
        }

    }
}