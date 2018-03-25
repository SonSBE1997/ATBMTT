/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author SBE
 */
public class UserWithPublicKey {
//Attribute

    String name;
    String publicKey;

//Constuctor
    public UserWithPublicKey() {
    }

    public UserWithPublicKey(String name, String publicKey) {
        this.name = name;
        this.publicKey = publicKey;
    }

//Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return name;
    }

}
