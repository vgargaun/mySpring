package com.unifun.app.components.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Validation {

    String[] color = {"BLACK", "RED", "GREEN", "YELLOW", "BLUE", "MAROON"};
    private boolean valid = false;

    public Validation() {
    }

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


    private String val(LinkedList<String> list) {
        String required = "";
        if (validName(list)) return list.get(0);
        else if (validColor(list)) return list.get(0);
        else {
            for (String st : list) {
                if (st.equals("required")) required = st;
            }
            if (required.isEmpty()) return "no required";
        }
        return null;
    }

    private boolean validName(LinkedList<String> listFirstName) {
        int min = 0;
        int max = 0;
        String reg = "";
        String name = "";
        String type = "";
        boolean vali = true;
        for (String st : listFirstName) {
            if (st.matches("^min\\d{1,}$")) {
                String[] aux = st.split("min");
                min = Integer.parseInt(aux[1]);
            } else if (st.matches("^max\\d{1,}$")) {
                String[] aux = st.split("max");
                max = Integer.parseInt(aux[1]);
            } else if (st.equals("required")) {
            } else if (st.equals("color")) {
                vali = false;
            } else if (st.equals("String")) type = st;
            else if (st.equals("Integer")) type = st;
            else if (st.matches("\\w+") || st.matches("\\d+")) name = st;
            else reg = st;
        }
        if (min > 0) {
            if (name.length() >= min) vali = true;
            else vali = false;
        }
        if (max > 0 && vali) {
            if (name.length() <= max) vali = true;
            else vali = false;
        }
        if (!reg.isEmpty() && vali) {
            if (name.matches(reg)) vali = true;
            else vali = false;
        }
        if (type.equals("String") && vali) {
            vali = name.matches("^\\w\\D+");
            System.out.println("valid " + vali);
        }
        if (type.equals("Integer")) {
            if (name.matches("\\d+")) vali = true;
            else vali = false;
        }
        return vali;
    }

    private boolean validColor(LinkedList<String> colorList) {

        boolean test = false;
        String color = "";
        boolean reg = false;
        for (String st : colorList) {
            if (st.equals("color")) reg = true;
            else if (st.equals("required")) {
            } else color = st;
        }
        if (reg) for (String st : this.color) {
            if (st.equals(color)) test = true;
        }
        return test;
    }

    public long validId(String id) {
        try {
            return Long.parseLong(id);

        } catch (Exception e) {
            return 0;
        }
    }

    public boolean getValid() {
        return valid;
    }

}
