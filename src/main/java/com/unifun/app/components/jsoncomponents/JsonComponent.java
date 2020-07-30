package com.unifun.app.components.jsoncomponents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;


public class JsonComponent {

    ObjectMapper mapper = new ObjectMapper();

    public String NoErrorMessage () throws JsonProcessingException {

        JsonType jsonType = new JsonType(0,"No error",HttpStatus.OK );
        return mapper.writeValueAsString(jsonType);

    }

    public String ErrorMessage (int error_code, String error_message, HttpStatus http) throws JsonProcessingException {

        JsonType jsonType = new JsonType(error_code, error_message, http);
        return mapper.writeValueAsString(jsonType);

    }
}
