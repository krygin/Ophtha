package ru.krygin.smart_sight.core;

import ru.krygin.smart_sight.OphthaAppComponent;

/**
 * Created by krygin on 20.08.17.
 */

public class Injector {


    private static OphthaAppComponent sAppComponent;

    public static OphthaAppComponent getAppComponent() {
        return sAppComponent;
    }

    public static void setAppComponent(OphthaAppComponent appComponent) {
        sAppComponent = appComponent;
    }
}
