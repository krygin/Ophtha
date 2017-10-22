package ru.krygin.smart_sight.core;

import ru.krygin.smart_sight.SmartSightAppComponent;

/**
 * Created by krygin on 20.08.17.
 */

public class Injector {


    private static SmartSightAppComponent sAppComponent;

    public static SmartSightAppComponent getAppComponent() {
        return sAppComponent;
    }

    public static void setAppComponent(SmartSightAppComponent appComponent) {
        sAppComponent = appComponent;
    }
}
