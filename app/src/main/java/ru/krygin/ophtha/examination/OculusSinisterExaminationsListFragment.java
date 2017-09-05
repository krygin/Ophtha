package ru.krygin.ophtha.examination;

import ru.krygin.ophtha.R;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 06.08.17.
 */

public class OculusSinisterExaminationsListFragment extends OculusExaminationsListFragment {

    @Override
    public int getTitleResId() {
        return R.string.oculus_sinister;
    }

    @Override
    protected Oculus getOculus() {
        return Oculus.SINISTER;
    }
}
