package ru.krygin.smart_sight.comparation;

import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.oculus.Oculus;

/**
 * Created by krygin on 06.08.17.
 */

public class OculusSinisterExaminationsListFragment extends OculusExaminationsListFragment {
    @Override
    protected Oculus getOculus() {
        return Oculus.SINISTER;
    }

    @Override
    public int getTitleResId() {
        return R.string.oculus_sinister;
    }
}
