package ru.krygin.ophtha.patients;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.materialspinner.MaterialSpinner;
import ru.krygin.ophtha.DateTimeUtils;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.ui.BaseActivity;
import ru.krygin.ophtha.patients.model.Patient;


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
        setTitle("Новый пациент");

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
                        mPatientBirthdayTextInputLayout.getEditText().setText(newDate);
                        mPatientBirthdayTextInputLayout.getEditText().setTag(date);
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
            case R.id.action_save:
                mPresenter.savePatient(
                        mPatientUUID > 0 ? mPatientUUID : System.currentTimeMillis(),
                        mFirstNameTextInputLayout.getEditText().getText().toString(),
                        mLastNameTextInputLayout.getEditText().getText().toString(),
                        mPatronymicTextInputLayout.getEditText().getText().toString(),
                        (Patient.Gender) mGenderSpinner.getSelectedItem(),
                        mPatientIdTextInputLayout.getEditText().getText().toString(),
                        (Date) mPatientBirthdayTextInputLayout.getEditText().getTag()
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showPatient(Patient patient) {
        mFirstNameTextInputLayout.getEditText().setText(patient.getFirstName());
        mLastNameTextInputLayout.getEditText().setText(patient.getLastName());
        mPatronymicTextInputLayout.getEditText().setText(patient.getPatronymic());
        Calendar calendar = DateTimeUtils.getCalendar(patient.getBirthday());
        mPatientBirthdayTextInputLayout.getEditText().setText(DateTimeUtils.getDateString(calendar));
        mPatientBirthdayTextInputLayout.getEditText().setTag(calendar.getTime());
        mPatientIdTextInputLayout.getEditText().setText(patient.getPatientId());

        mGenderSpinner.setSelection(patient.getGender().ordinal());
    }

    @Override
    public void close() {
        finish();
    }
}
