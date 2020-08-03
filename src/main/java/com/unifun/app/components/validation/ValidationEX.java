package com.unifun.app.components.validation;

import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
public class ValidationEX {

    String[] color = {"BLACK", "RED", "GREEN", "YELLOW", "BLUE", "MAROON"};
    boolean valid = false;

    public ValidationEX(String color, String model, int min, int max, String string) {

        validColor(color);
        validModel(model, min, max, string);
    }

    public ValidationEX(ArrayList<Long> id_object, long id) {
        validId(id_object, id);
    }

    public ValidationEX(String firstNmae, String lastName, String string, int min, int max) {
        validNmae(firstNmae, lastName, string, min, max);
    }

    private void validNmae(String firstNmae, String lastName, String string, int min, int max) {
        if (firstNmae.length() <= max && firstNmae.length() >= min && lastName.length() >= min
                && lastName.length() <= max && firstNmae.matches(string) && lastName.matches(string)) {
            valid = true;
        }
    }

    private void validId(ArrayList<Long> id_object, long id) {
        for (long i : id_object) {
            if (i == id) {
                valid = true;
                break;
            }
        }
    }

    private void validColor(String color) {
        for (String a : this.color) {
            if (a.equals(color)) {
                valid = true;
                break;
            }
        }
    }

    private void validModel(String model, int min, int max, String string) {
        if (model.length() <= max && model.length() >= min && model.matches(string) && valid) valid = true;
        else valid = false;
    }

    public boolean getValid() {
        return valid;
    }

}
