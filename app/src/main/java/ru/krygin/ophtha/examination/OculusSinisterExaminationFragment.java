package ru.krygin.ophtha.examination;

import ru.krygin.ophtha.R;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 14.08.17.
 */

public class OculusSinisterExaminationFragment extends OculusExaminationFragment {
    @Override
    protected Oculus getOculus() {
        return Oculus.SINISTER;
    }

    @Override
    public int getTitleResId() {
        return R.string.oculus_sinister;
    }
}
