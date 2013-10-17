package com.pilasvacias.yaba.modules.emt.models;

/**
 * Created by pablo on 10/15/13.
 * welvi-android
 */
public enum EmtStatusCode {

    UNKNOWN(-1),
    PASSKEY_OK(0),
    PASSKEY_OK_NOT_NEEDED(1),
    PASSKEY_NOT_MATCHING(2),
    PASSKEY_EXPIRED(3),
    UNAUTHORIZED(4),
    ACCESS_DENIED(5),
    LOCKED(6),
    FAILED_AUTH(9);

    private final int code;

    EmtStatusCode(int code) {
        this.code = code;
    }

    public static EmtStatusCode getFromResponse(EmtInfo info) {
        for (EmtStatusCode emtStatusCode : EmtStatusCode.values()) {
            if (emtStatusCode.code == info.getResultCode())
                return emtStatusCode;
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

}
