package com.unifun.app.module.Repository;

import com.unifun.app.models.Clients;
import org.springframework.data.repository.CrudRepository;

public interface ClientsRepository extends CrudRepository<Clients, Integer> {
}
