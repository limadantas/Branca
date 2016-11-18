package com.douglasdantas.branca.model;

/**
 * Created by oceanmanaus on 18/11/2016.
 */

public class Mensagem implements java.io.Serializable {
    public String id;
    public String msg;

    public Mensagem (String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public Mensagem () {}
}
