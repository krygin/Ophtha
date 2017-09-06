package ru.krygin.ophtha.patients;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.materialspinner.MaterialSpinner;
import ru.krygin.ophtha.DateTimeUtils;
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

    @BindView(R.id.patient_birthday_text_input_layout)
    TextInputLayout mPatientBirthdayTextInputLayout;

    @BindView(R.id.patient_id_text_input_layout)
    TextInputLayout mPatientIdTextInputLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_patient);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle("Новый пациент");

        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(Gender.M, Gender.F));
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.genders, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mGenderSpinner.setHint(Gender.UNDEFINDED);
        mGenderSpinner.setAdapter(spinnerAdapter);

        mPatientBirthdayTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDateCalendar = DateTimeUtils.getCurrentCalendar();
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOrUpdatePatientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = DateTimeUtils.getDate(year, month, dayOfMonth);
                        String newDate = DateTimeUtils.getDateString(date);
                        mPatientBirthdayTextInputLayout.getEditText().setText(newDate);
                        mPatientBirthdayTextInputLayout.getEditText().setTag(date);
                    }
                }, currentDateCalendar.get(Calendar.YEAR), currentDateCalendar.get(Calendar.MONTH), currentDateCalendar.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMaxDate(currentDateCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
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
                        0,
                        mFirstNameTextInputLayout.getEditText().getText().toString(),
                        mLastNameTextInputLayout.getEditText().getText().toString(),
                        mPatronymicTextInputLayout.getEditText().getText().toString(),
                        (Gender) mGenderSpinner.getSelectedItem(),
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

    }

    @Override
    public void close() {
        finish();
    }
}
