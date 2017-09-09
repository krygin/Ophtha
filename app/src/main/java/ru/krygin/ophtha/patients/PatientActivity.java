package ru.krygin.ophtha.patients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.krygin.ophtha.DateTimeUtils;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.comparation.ExaminationComparisionActivity;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.ui.BaseActivity;
import ru.krygin.ophtha.examination.CreateOrUpdateExaminationActivity;
import ru.krygin.ophtha.examination.ExaminationsPerOculusPagerAdapter;
import ru.krygin.ophtha.examination.OculusExaminationsListFragment;
import ru.krygin.ophtha.patients.model.Patient;

/**
 * Created by krygin on 05.08.17.
 */

public class PatientActivity extends BaseActivity implements OculusExaminationsListFragment.PatientUUIDProvider {

    private static final String EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @BindView(R.id.patient_name_text_view)
    TextView mPatientNameTextView;

    @BindView(R.id.patient_birthday_text_view)
    TextView mPatientBirthdayTextView;

    @BindView(R.id.patient_id_text_view)
    TextView mPatientIdTextView;

    private ExaminationsPerOculusPagerAdapter mExaminationPerOculusPagerAdapter;
    private long mPatientUUID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        ButterKnife.bind(this);

        mPatientUUID = getIntent().getLongExtra(EXTRA_PATIENT_UUID, 0);

        setSupportActionBar(mToolbar);
        setTitle("Карта пациента");
        mExaminationPerOculusPagerAdapter = new ExaminationsPerOculusPagerAdapter(getResources(), getSupportFragmentManager());
        mViewPager.setAdapter(mExaminationPerOculusPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUseCaseHandler().execute(new GetPatientUseCase(), new GetPatientUseCase.RequestValues(mPatientUUID), new UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetPatientUseCase.ResponseValue response) {
                Patient patient = response.getPatient();
                mPatientNameTextView.setText(String.format("%s %s %s", patient.getLastName(), patient.getFirstName(), patient.getPatronymic()));
                mPatientBirthdayTextView.setText(DateTimeUtils.getDateString(patient.getBirthday()));
                mPatientIdTextView.setText(patient.getPatientId());
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_patient, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compare_menu_item:
                Intent compareExaminationsIntent = new Intent(this, ExaminationComparisionActivity.class);
                startActivity(compareExaminationsIntent);
                return true;
            case R.id.edit_menu_item:
                Intent editPatientIntent = CreateOrUpdatePatientActivity.newIntent(this, mPatientUUID);
                startActivity(editPatientIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab)
    void onClick(View view) {
        Intent intent = CreateOrUpdateExaminationActivity.newIntent(this);
        startActivity(intent);
    }

    public static Intent newIntent(Context context, long patientUUID) {
        Intent intent = new Intent(context, PatientActivity.class);
        intent.putExtra(EXTRA_PATIENT_UUID, patientUUID);
        return intent;
    }

    @Override
    public long getPatientUUID() {
        return mPatientUUID;
    }
}
