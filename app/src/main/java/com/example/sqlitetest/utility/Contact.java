package com.example.sqlitetest.utility;

import java.util.ArrayList;
import java.util.List;

public class Contact {
    int _id;
    String _name;
    int _lastmodified;
    List<Description> _descriptions;


    public Contact(){}

    public Contact(int id, String name){
        this._id = id;
        this._name = name;
    }

    public Contact(int id, String name, int lastmodified, List<Description> descriptions){
        this._id = id;
        this._name = name;
        this._lastmodified = lastmodified;
        this._descriptions = descriptions;
    }

    public Contact(String name){
        this._name = name;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public void setLastmodified(int lastmodified) {
        this._lastmodified = lastmodified;
    }

    public int getLastmodified() {
        return this._lastmodified;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public void setDescriptions(List<Description> descriptions) {
        this._descriptions = descriptions;
    }

    public void addDescription(Description description) {
        this._descriptions.add(description);
    }

    public void removeDescriptions(List<Description> descriptions) {
        for(Description currDescription : descriptions) {
            _descriptions.remove(currDescription);
        }
    }

    public List<Description> getDescriptions() {
        return _descriptions;
    }
}