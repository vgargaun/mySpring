package com.unifun.app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return "Clients{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
