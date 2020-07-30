package com.unifun.app.module.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.unifun.app.components.jsoncomponents.JsonComponent;
import com.unifun.app.components.validation.Validation;
import com.unifun.app.models.Clients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping(path = "/clients")
public class ClientControler {
    public static Logger logger = Logger.getLogger(ClientControler.class);
    ArrayList<Long> id = new ArrayList<>();
    @Autowired
    private ClientsRepository clientRepository;

    @GetMapping(path = "/add")
    public @ResponseBody
    String addNewClients(@RequestParam(defaultValue = "") String firstName, @RequestParam(defaultValue = "") String lastName) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();
        try {

            Validation validation = new Validation(firstName, lastName, "[A-Z][a-z]+", 3, 10);
            if (validation.getValid()) {
                Clients client = new Clients();
                client.setFirstName(firstName);
                client.setLastName(lastName);
                clientRepository.save(client);
                this.id.add(client.getId());
                logger.info("Saved new client");
                return j.NoErrorMessage();
            } else {
                logger.warn("Bad request param");
                return j.ErrorMessage(1, "Is bad request", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            logger.warn("Don't saved " + e);
            return j.ErrorMessage(1, "Is bad request", HttpStatus.BAD_REQUEST);

        }

    }

    @GetMapping(path = "/deleteById")
    public @ResponseBody
    String deleteById(@RequestParam(defaultValue = "") String id) throws JsonProcessingException {
        JsonComponent j = new JsonComponent();
        try {

            Validation validation = new Validation(this.id, Long.parseLong(id));
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
                logger.info("Client, was deleted");
                return j.NoErrorMessage();
            } else {
                logger.warn("Was not deleted ");
                return j.ErrorMessage(2, "Was not deleted", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            logger.warn("Was not deleted " + e);
            return j.ErrorMessage(2, "Was not deleted", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "/list")
    public @ResponseBody
    Object getAllUsers() {

        try {
            JsonComponent j = new JsonComponent();
            if (this.id.isEmpty()) {
                logger.warn("Data Base is Emty");
                return j.ErrorMessage(4, "Data Base is Emty", HttpStatus.BAD_REQUEST);
            }
            logger.info("Showed list ");
            return clientRepository.findAll();
        } catch (Exception e) {
            logger.warn("Error for show all " + e);
        }
        return null;
    }

}
