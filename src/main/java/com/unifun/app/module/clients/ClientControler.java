package com.unifun.app.module.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.unifun.app.components.jsoncomponents.JsonComponent;
import com.unifun.app.components.validation.ValidationMaiSerioasa;
import com.unifun.app.models.Clients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Controller
@RequestMapping(path = "/clients")
public class ClientControler {
    public static Logger logger = Logger.getLogger(ClientControler.class);
    ArrayList<Long> id = new ArrayList<>();
    @Autowired
    private ClientsRepository clientRepository;

    @GetMapping(path = "/add")
    public @ResponseBody
    String addNewClients(@RequestParam(defaultValue = "") String firstName, @RequestParam(defaultValue = "") String lastName,
                         HttpServletResponse resp) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();

        HashMap<String, LinkedList> map = new HashMap<>();
        LinkedList<String> firstN = new LinkedList<>();
        LinkedList<String> lastN = new LinkedList<>();
        firstN.add(firstName);
        firstN.add("min3");
        firstN.add("max9");
        firstN.add("[A-Z][a-z]+");
        map.put("firstName", firstN);
        lastN.add(lastName);
        lastN.add("min3");
        lastN.add("max9");
        lastN.add("[A-Z][a-z]+");
        map.put("lastName", lastN);
        try {

            ValidationMaiSerioasa validation = new ValidationMaiSerioasa();
            HashMap<String, String> keyName = validation.validation(map);
            boolean val = true;
            for (Map.Entry<String, String> map1 : keyName.entrySet()) {
                logger.info(map1.getKey() + "  " + map1.getValue());
                if(map1.getValue()==null) {val = false; break;}
            }
            if (val) {
                Clients client = new Clients();
                client.setFirstName(firstName);
                client.setLastName(lastName);
                clientRepository.save(client);
                this.id.add(client.getId());
                resp.setStatus(200);
                logger.info("Saved new client");
                return j.NoErrorMessage();

            } else {
                logger.warn("Bad request param");
                resp.setStatus(400);
                return j.ErrorMessage(1, "Is bad request", HttpStatus.BAD_REQUEST);
            }
        }
         catch (Exception e) {
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

            ValidationMaiSerioasa validation = new ValidationMaiSerioasa(this.id, Long.parseLong(id));
            if (validation.getValid()) {
                for (int i = 0; i < this.id.size(); i++) {
                    if (Long.parseLong(id) == this.id.get(i)) {
                        this.id.remove(i);
                        break;
                    }
                }
                Clients client = new Clients();
                client.setId(Long.parseLong(id));
                clientRepository.delete(client);
                resp.setStatus(200);
                logger.info("Client, was deleted");
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
            return clientRepository.findAll();
        } catch (Exception e) {
            resp.setStatus(200);
            logger.warn("Error for show all " + e);
            return j.ErrorMessage(4, "Data Base is Emty", HttpStatus.BAD_REQUEST);

        }
    }

}
