package ru.krygin.ophtha.core.ui;

import android.support.annotation.StringRes;

/**
 * Created by krygin on 06.08.17.
 */

public abstract class TitledFragment extends BaseFragment {

    @StringRes
    public int getTitleResId() {
        return 0;
    }
}
