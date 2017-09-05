package ru.krygin.ophtha.comparation;

import ru.krygin.ophtha.R;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 06.08.17.
 */

public class OculusDexterExaminationsListFragment extends OculusExaminationsListFragment {
    @Override
    protected Oculus getOculus() {
        return Oculus.DEXTER;
    }

    @Override
    public int getTitleResId() {
        return R.string.oculus_dexter;
    }
}
