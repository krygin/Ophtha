package ru.krygin.smart_sight.patients;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.materialspinner.MaterialSpinner;
import ru.krygin.smart_sight.DateTimeUtils;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.ui.BaseActivity;
import ru.krygin.smart_sight.patients.model.Patient;


/**
 * Created by krygin on 10.08.17.
 */

public class CreateOrUpdatePatientActivity extends BaseActivity implements PatientView {

    private static final String EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID";

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

    @BindView(R.id.patient_birthday_text_input_layout)
    TextInputLayout mPatientBirthdayTextInputLayout;

    @BindView(R.id.patient_id_text_input_layout)
    TextInputLayout mPatientIdTextInputLayout;

    @BindView(R.id.patient_diagnosis_text_input_layout)
    TextInputLayout mDiagnosisTextInputLayout;

    private long mPatientUUID;

    public static Intent newIntent(Context context, long patientUUID) {
        Intent intent = newIntent(context);
        intent.putExtra(EXTRA_PATIENT_UUID, patientUUID);
        return intent;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CreateOrUpdatePatientActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_patient);
        mPatientUUID = getIntent().getLongExtra(EXTRA_PATIENT_UUID, 0);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mPatientUUID == 0 ? "Новый пациент" : "Редактирование карты");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(Patient.Gender.M, Patient.Gender.F));
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.genders, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mGenderSpinner.setHint(Patient.Gender.UNDEFINDED);
        mGenderSpinner.setAdapter(spinnerAdapter);

        mPatientBirthdayTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar taggedDateOfBirth = (Calendar) mPatientBirthdayTextInputLayout.getEditText().getTag();
                Calendar birthdayCalendar = taggedDateOfBirth != null ? taggedDateOfBirth : DateTimeUtils.getCurrentCalendar();
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOrUpdatePatientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = DateTimeUtils.getDate(year, month, dayOfMonth);
                        String newDate = DateTimeUtils.getDateString(date);
                        Calendar dateToTag = DateTimeUtils.getCalendar(date);
                        mPatientBirthdayTextInputLayout.getEditText().setText(newDate);
                        mPatientBirthdayTextInputLayout.getEditText().setTag(dateToTag);
                    }
                }, birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMaxDate(DateTimeUtils.getCurrentCalendar().getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadPatient(mPatientUUID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_or_update_patient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                if (checkDataIsValid()) {
                    mPresenter.savePatient(
                            mFirstNameTextInputLayout.getEditText().getText().toString(),
                            mLastNameTextInputLayout.getEditText().getText().toString(),
                            mPatronymicTextInputLayout.getEditText().getText().toString(),
                            (Patient.Gender) mGenderSpinner.getSelectedItem(),
                            mPatientIdTextInputLayout.getEditText().getText().toString(),
                            ((Calendar) mPatientBirthdayTextInputLayout.getEditText().getTag()).getTime(),
                            mDiagnosisTextInputLayout.getEditText().getText().toString()
                    );
                    return true;
                } else return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkDataIsValid() {
        boolean result = true;
        if (TextUtils.isEmpty(mFirstNameTextInputLayout.getEditText().getText().toString())) {
            mFirstNameTextInputLayout.setError(getString(R.string.field_required));
            result = false;
        }

        if (TextUtils.isEmpty(mLastNameTextInputLayout.getEditText().getText().toString())) {
            mLastNameTextInputLayout.setError(getString(R.string.field_required));
            result = false;
        }

        if (TextUtils.isEmpty(mPatronymicTextInputLayout.getEditText().getText().toString())) {
            mPatronymicTextInputLayout.setError(getString(R.string.field_required));
            result = false;
        }

        if (mGenderSpinner.getSelectedItem().equals(Patient.Gender.UNDEFINDED)) {
            mGenderSpinner.setError(getString(R.string.field_required));
            result = false;
        }

        if (TextUtils.isEmpty(mPatientIdTextInputLayout.getEditText().getText().toString())) {
            mPatientIdTextInputLayout.setError(getString(R.string.field_required));
            result = false;
        }

        if (mPatientBirthdayTextInputLayout.getEditText().getTag() == null) {
            mPatientBirthdayTextInputLayout.setError(getString(R.string.field_required));
            result = false;
        }

        if (TextUtils.isEmpty(mDiagnosisTextInputLayout.getEditText().getText().toString())) {
            mDiagnosisTextInputLayout.setError(getString(R.string.field_required));
            result = false;
        }

        return result;
    }

    @Override
    public void showPatient(Patient patient) {
        mFirstNameTextInputLayout.getEditText().setText(patient.getFirstName());
        mLastNameTextInputLayout.getEditText().setText(patient.getLastName());
        mPatronymicTextInputLayout.getEditText().setText(patient.getPatronymic());
        Calendar calendar = DateTimeUtils.getCalendar(patient.getBirthday());
        mPatientBirthdayTextInputLayout.getEditText().setText(DateTimeUtils.getDateString(calendar));
        mPatientBirthdayTextInputLayout.getEditText().setTag(calendar);
        mPatientIdTextInputLayout.getEditText().setText(patient.getPatientId());

        mGenderSpinner.setSelection(patient.getGender().ordinal());
    }

    @Override
    public void close() {
        finish();
    }
}
