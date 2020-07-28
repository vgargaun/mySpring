package com.unifun.app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Cars {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String model;
    private String color;
    private long id_client;

//    private int price;

    @ManyToOne(cascade = CascadeType.ALL)
    public Clients clients;

    public void setClients(Clients clients) {
        this.clients = clients;
    }

    public Cars(){}

    public String getModel() {
        return model;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
