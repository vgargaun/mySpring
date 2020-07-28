package com.unifun.app.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonComponent {

    ObjectMapper mapper = new ObjectMapper();

    public String NoErrorMessage () throws JsonProcessingException {

        JsonType jsonType = new JsonType(0,"No error" );
        return mapper.writeValueAsString(jsonType);

    }

    public String ErrorMessage (int error_code, String error_message) throws JsonProcessingException {

        JsonType jsonType = new JsonType(error_code, error_message);
        return mapper.writeValueAsString(jsonType);

    }
}
