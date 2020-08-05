package com.unifun.app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String model;
    private String color;

    @ManyToOne(cascade = CascadeType.ALL)
    public Clients id_client;

    public Cars(long id, String model, String color) {
        this.id = id;
        this.model = model;
        this.color = color;
    }
}


