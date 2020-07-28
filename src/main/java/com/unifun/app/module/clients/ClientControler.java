package com.unifun.app.module.clients;


import com.unifun.app.models.Clients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/clients")
public class ClientControler {
    public static Logger logger = Logger.getLogger(ClientControler.class);

    @Autowired
    private ClientsRepository clientRepository;

    @GetMapping(path = "/add")
    public @ResponseBody
    String addNewClients(@RequestParam String firstName, @RequestParam String lastName) {

        try {
            Clients client = new Clients();
            client.setFirstName(firstName);
            client.setLastName(lastName);
            clientRepository.save(client);
            logger.info("Saved new client");
        } catch (Exception e) {
            logger.warn("Don't saved " + e);

        }
        return "Saved";
    }

    @GetMapping(path = "/deleteById")
    public @ResponseBody
    String deleteById(@RequestParam long id) {
        try {
            Clients client = new Clients();
            client.setId(id);
            clientRepository.delete(client);
            logger.info("Client, was deleted");
        } catch (Exception e) {
            logger.warn("Was not deleted " + e);
        }
        return "Deleted ";

    }

    @GetMapping(path = "/list")
    public @ResponseBody
    Iterable<Clients> getAllUsers() {

        try {
            logger.info("Showed list");
            return clientRepository.findAll();
        } catch (Exception e) {
            logger.warn("error");
        }
        return null;
    }

}
