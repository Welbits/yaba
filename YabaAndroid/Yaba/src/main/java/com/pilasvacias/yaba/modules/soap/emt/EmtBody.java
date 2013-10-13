package com.pilasvacias.yaba.modules.soap.emt;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

/**
 * Created by pablo on 10/12/13.
 * welvi-android
 */
@Namespace(reference = "http://tempuri.org/")
public class EmtBody {
    @Element
    private String idClient = "string";
    @Element
    private String PassKey = "string";

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

}
