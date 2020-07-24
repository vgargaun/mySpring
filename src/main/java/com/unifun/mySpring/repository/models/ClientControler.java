package com.unifun.mySpring.repository.models;


import com.unifun.mySpring.models.Clients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/clients")
public class ClientControler {
    @Autowired
    private ClientsRepository clientRepository;

    @GetMapping(path="/add")
    public @ResponseBody
    String addNewCars (@RequestParam String firstName, @RequestParam String lastName) {

        Clients client = new Clients();


        client.setFirstName(firstName);
        client.setLastName(lastName);
        clientRepository.save(client);
        return "Saved";
    }

    @GetMapping(path="/list")
    public @ResponseBody
    Iterable<Clients> getAllUsers() {

        return clientRepository.findAll();

    }

}
