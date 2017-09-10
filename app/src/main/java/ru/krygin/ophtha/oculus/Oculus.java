package ru.krygin.ophtha.oculus;

import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 06.08.17.
 */

public enum Oculus {
    DEXTER,
    SINISTER,
    UNDEFINDED;


    public Boolean toBoolean() {
        switch (this) {
            case DEXTER:
                return true;
            case SINISTER:
                return false;
            default:
                return null;
        }
    }

    public static Oculus fromBoolean(Boolean oculusBoolean) {
        Oculus oculus = UNDEFINDED;
        if (oculusBoolean == Boolean.TRUE) {
            oculus = DEXTER;
        } else if (oculusBoolean == Boolean.FALSE) {
            oculus = SINISTER;
        } else {
            oculus = UNDEFINDED;
        }
        return oculus;
    }
}
