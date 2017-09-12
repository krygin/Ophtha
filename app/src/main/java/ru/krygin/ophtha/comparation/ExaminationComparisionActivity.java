package ru.krygin.ophtha.comparation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.ui.BaseActivity;
import ru.krygin.ophtha.examination.PatientUUIDProvider;

/**
 * Created by krygin on 06.08.17.
 */

public class ExaminationComparisionActivity extends BaseActivity implements PatientUUIDProvider {

    private static final String EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID";

    public static Intent newIntent(Context context, long patientUUID) {
        Intent intent = new Intent(context, ExaminationComparisionActivity.class);
        intent.putExtra(EXTRA_PATIENT_UUID, patientUUID);
        return intent;
    }

//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;

    @BindView(R.id.first_oculus_in_comparision_container)
    FrameLayout mFirstOculusInComparisionContainer;

    @BindView(R.id.second_oculus_in_comparision_container)
    FrameLayout mSecondOculusInComparisionContainer;
    private long mPatientUUID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_comparision);
        ButterKnife.bind(this);
//        setSupportActionBar(mToolbar);
        mPatientUUID = getIntent().getLongExtra(EXTRA_PATIENT_UUID, 0);

        getSupportFragmentManager().beginTransaction().replace(R.id.first_oculus_in_comparision_container, new ExaminationComparisionWrapperFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.second_oculus_in_comparision_container, new ExaminationComparisionWrapperFragment()).commit();
    }

    @Override
    public long getPatientUUID() {
        return mPatientUUID;
    }
}
