package ru.krygin.smart_sight.examination;

import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.oculus.Oculus;

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
