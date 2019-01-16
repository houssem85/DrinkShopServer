package com.programasoft.drinkshopserver.Model;

import java.io.Serializable;

/**
 * Created by ASUS on 27/12/2018.
 */

public class menu implements Serializable {

    private int ID;
    private String Name;
    private String Link;

    @Override
    public String toString() {
        return Name;
    }

    public menu() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
