package ru.krygin.ophtha.examination;

import ru.krygin.ophtha.R;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 14.08.17.
 */

public class OculusDexterExaminationFragment extends OculusExaminationFragment {
    @Override
    protected Oculus getOculus() {
        return Oculus.DEXTER;
    }

    @Override
    public int getTitleResId() {
        return R.string.oculus_dexter;
    }
}
