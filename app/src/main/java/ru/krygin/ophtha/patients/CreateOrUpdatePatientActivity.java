package ru.krygin.ophtha.patients;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.materialspinner.MaterialSpinner;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.ui.BaseActivity;

import static ru.krygin.ophtha.patients.PatientsRepository.*;
import static ru.krygin.ophtha.patients.PatientsRepository.Patient.*;

/**
 * Created by krygin on 10.08.17.
 */

public class CreateOrUpdatePatientActivity extends BaseActivity implements PatientView {

    @InjectPresenter
    CreateOrUpdatePatientPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.container)
    LinearLayout mContainer;

    @BindView(R.id.first_name_text_input_layout)
    TextInputLayout mFirstNameTextInputLayout;

    @BindView(R.id.last_name_text_input_layout)
    TextInputLayout mLastNameTextInputLayout;

    @BindView(R.id.patronymic_name_text_input_layout)
    TextInputLayout mPatronymicTextInputLayout;

    @BindView(R.id.spinner)
    MaterialSpinner mGenderSpinner;

    @BindView(R.id.patient_id_text_input_layout)
    TextInputLayout mPatientIdTextInputLayout;

    @BindView(R.id.save_patient_button)
    Button mSavePatientButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_patient);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(Gender.M, Gender.F));
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.genders, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mGenderSpinner.setHint(Gender.UNDEFINDED);
        mGenderSpinner.setAdapter(spinnerAdapter);

//        mSavePatientButton.setOnClickListener(view -> Toast.makeText(this, String.valueOf(mGenderSpinner.getSelectedItemPosition()), Toast.LENGTH_LONG).show());
        mSavePatientButton.setOnClickListener(view -> mPresenter.savePatient(
                0,
                mFirstNameTextInputLayout.getEditText().getText().toString(),
                mLastNameTextInputLayout.getEditText().getText().toString(),
                mPatronymicTextInputLayout.getEditText().getText().toString(),
                (Gender) mGenderSpinner.getSelectedItem(),
                mPatientIdTextInputLayout.getEditText().getText().toString()
        ));
    }

    @Override
    public void showPatient(Patient patient) {

    }
}
