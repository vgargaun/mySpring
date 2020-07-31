package com.unifun.app.components.jsoncomponents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JsonType {

    private int error_code;
    private String error_message;
    private HttpStatus http_status;

}
