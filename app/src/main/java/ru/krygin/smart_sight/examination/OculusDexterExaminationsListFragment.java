package ru.krygin.smart_sight.examination;

import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.oculus.Oculus;

/**
 * Created by krygin on 06.08.17.
 */

public class OculusDexterExaminationsListFragment extends OculusExaminationsListFragment {


    @Override
    public int getTitleResId() {
        return R.string.oculus_dexter;
    }

    @Override
    protected Oculus getOculus() {
        return Oculus.DEXTER;
    }
}
