package com.pilasvacias.yaba.modules.emt.models;


import com.google.gson.Gson;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
public class EmtBody {

    @XStreamOmitField
    private static final Gson gson = new Gson();

    @XStreamAsAttribute
    String xmlns = "http://tempuri.org/";

    private String idClient = "WEB.PORTALMOVIL.OTROS";
    private String PassKey = "0810DDE4-02FC-4C0E-A440-1BD171B397C8";

    public String getPassKey() {
        return PassKey;
    }

    public void setPassKey(String passKey) {
        PassKey = passKey;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getSoapAction() {
        String action = getClass().getSimpleName();
        if (action.isEmpty())
            throw new IllegalStateException("You must define the body class (not anonymous) " +
                    "with the same name as the request or override this method");
        return action;
    }

    public String getCacheKey(){
        return getSoapAction() + gson.toJson(this);
    }

}
