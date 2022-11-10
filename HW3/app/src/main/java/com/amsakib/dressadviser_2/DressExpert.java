package com.amsakib.dressadviser_2;

import java.util.ArrayList;
import java.util.List;

public class DressExpert {
    List<String> getDressSuggestion(String type) {
        List<String> suggestions = new ArrayList<String>();
        if(type.equals("Formal - Male")) {
            suggestions.add("Formal Shirt");
            suggestions.add("Formal Pant");
        } else if(type.equals("Formal - Female")) {
            suggestions.add("Sharee");
            suggestions.add("Salwar Kameez");
        } else if(type.equals("Casual - Male")) {
            suggestions.add("Casual Shirt");
            suggestions.add("Polo Shirt");
            suggestions.add("T Shirt");
            suggestions.add("Jeans Pant");
            suggestions.add("Mobile Pant");
        } else if(type.equals("Casual - Female")) {
            suggestions.add("Sharee");
            suggestions.add("Salwar Kameez");
            suggestions.add("Kurti");
            suggestions.add("Tops");
            suggestions.add("Jeans");
            suggestions.add("Lahenga");
            suggestions.add("Skirt");
        } else {
            suggestions.add("Invalid type!");
        }
        return suggestions;
    }
}
