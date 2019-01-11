package com.programasoft.drinkshopserver.Model;

/**
 * Created by ASUS on 27/12/2018.
 */

public class menu {

    private int ID;
    private String Name;
    private String Link;

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
