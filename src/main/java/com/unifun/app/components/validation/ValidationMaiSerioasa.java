package com.unifun.app.components.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ValidationMaiSerioasa {

    String[] color = {"BLACK", "RED", "GREEN", "YELLOW", "BLUE", "MAROON"};
    private boolean valid = false;

    public ValidationMaiSerioasa() {}

    public HashMap<String, String> validation(HashMap<String, LinkedList> map) {
        HashMap<String, String> con = new HashMap<String, String>();
        for (Map.Entry<String, LinkedList> map1 : map.entrySet()) {

            String key = map1.getKey();
            LinkedList<String> list = map1.getValue();
            String controler = val(list);
            con.put(key, controler);
        }
        return con;
    }


    private String val(LinkedList<String> list){
        if(validName(list)) return list.get(0);
        else if(validColor(list.get(0))) return list.get(0);
        return null;
    }

    private boolean validName(LinkedList<String> listFirstName) {
        int min = 0;
        int max = 0 ;
        String reg = "";
        String name = "";
        boolean vali = true;
        for (String st : listFirstName) {
            if (st.matches("^min\\d{1,}$")){
                String[] aux = st.split("min");
                min = Integer.parseInt(aux[1]);
            }
            else if (st.matches("^max\\d{1,}$")){
                String[] aux = st.split("max");
                max = Integer.parseInt(aux[1]);
            }
            else if (st.matches("\\w+"))  name = st;
            else reg = st;
        }

        if (min > 0) {
            if (name.length() >= min) vali = true;
            else vali = false;
        }
        if (max > 0&&vali) {
            if (name.length() <= max) vali = true;
            else vali = false;
        }
        if (!reg.isEmpty()&&vali) {
            if (name.matches(reg)) vali = true;
            else vali = false;
        }
        if(min<0&&max<0&&reg.isEmpty()) vali = false;
        return vali;
    }
    public ValidationMaiSerioasa(ArrayList<Long> id_object, long id) {
        validId(id_object, id);
    }

    private boolean validColor(String color){

        boolean test = false;

        for (String a : this.color) {
            if (a.equals(color)) {
                test = true;
                break;
            } else test = false;
        }
        return test;
    }

    private boolean validModel(LinkedList<String> list) {
        int min = 0;
        int max = 0 ;
        String reg = "";
        String modelName = "";

        for (String st : list) {
            if (st.matches("^min\\d{1,}$")){
                String[] aux = st.split("min");
                min = Integer.parseInt(aux[1]);
            }
            else if (st.matches("^max\\d{1,}$")){
                String[] aux = st.split("max");
                max = Integer.parseInt(aux[1]);
            }
            else if (st.matches("\\w+"))  modelName = st;
            else reg = st;
        }

        if (modelName.length() <= max && modelName.length() >= min && modelName.matches(reg)) return true;
        else return false;
    }
    private void validId(ArrayList<Long> id_object, long id) {
        for (long i : id_object) {
            if (i == id) {
                valid = true;
                break;
            }
        }
    }

    public boolean getValid() {
        return true;
    }

}
