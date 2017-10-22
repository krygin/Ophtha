package ru.krygin.smart_sight.examination;

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
import ru.krygin.smart_sight.examination.model.Examination;

/**
 * Created by krygin on 13.09.17.
 */

public class CreateOrUpdateExaminationActivity extends BaseActivity implements CreateOrUpdateExaminationView {

    @InjectPresenter
    CreateOrUpdateExaminationPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.examination_type_spinner)
    MaterialSpinner mExaminationTypeSpinner;

    @BindView(R.id.date_text_input_layout)
    TextInputLayout mDateTextInputLayout;

    @BindView(R.id.comment_text_input_layout)
    TextInputLayout mCommentTextInputLayout;


    private static final String EXTRA_EXAMINATION_UUID = "EXTRA_EXAMINATION_UUID";
    private static final String EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID";

    private long mExaminationUUID;
    private long mPatientUUID;

    public static Intent newIntent(Context context, long patientUUID) {
        Intent intent = new Intent(context, CreateOrUpdateExaminationActivity.class);
        intent.putExtra(EXTRA_PATIENT_UUID, patientUUID);
        return intent;
    }

    public static Intent newIntent(Context context, long patientUUID, long examinationUUID) {
        Intent intent = newIntent(context, patientUUID);
        intent.putExtra(EXTRA_EXAMINATION_UUID, examinationUUID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_examination);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mExaminationUUID = getIntent().getLongExtra(EXTRA_EXAMINATION_UUID, 0);
        mPatientUUID = getIntent().getLongExtra(EXTRA_PATIENT_UUID, 0);
        getSupportActionBar().setTitle(mExaminationUUID == 0 ? "Новое обследование" : "Редактирование обследования");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(Examination.Type.OCULAR_FUNDUS, Examination.Type.SEGMENT_ANTERIOR));
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.genders, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mExaminationTypeSpinner.setHint(Examination.Type.UNDEFINDED);
        mExaminationTypeSpinner.setAdapter(spinnerAdapter);

        mDateTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar taggedDateOfBirth = (Calendar) mDateTextInputLayout.getEditText().getTag();
                Calendar birthdayCalendar = taggedDateOfBirth != null ? taggedDateOfBirth : DateTimeUtils.getCurrentCalendar();
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOrUpdateExaminationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = DateTimeUtils.getDate(year, month, dayOfMonth);
                        Calendar dateToTag = DateTimeUtils.getCalendar(date);
                        String newDate = DateTimeUtils.getDateString(date);
                        mDateTextInputLayout.getEditText().setText(newDate);
                        mDateTextInputLayout.getEditText().setTag(dateToTag);
                    }
                }, birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMaxDate(DateTimeUtils.getCurrentCalendar().getTimeInMillis());
                datePickerDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_or_update_examination, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                mPresenter.saveExamination(mPatientUUID,
                        (Examination.Type)mExaminationTypeSpinner.getSelectedItem(),
                        ((Calendar) mDateTextInputLayout.getEditText().getTag()).getTime(),
                        mCommentTextInputLayout.getEditText().getText().toString()
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadExamination(mExaminationUUID);
    }

    @Override
    public void showExamination(Examination examination) {
        mExaminationTypeSpinner.setSelection(examination.getType());
        Calendar calendar = DateTimeUtils.getCalendar(examination.getDate());
        mDateTextInputLayout.getEditText().setText(DateTimeUtils.getDateString(calendar));
        mDateTextInputLayout.getEditText().setTag(calendar);
        mCommentTextInputLayout.getEditText().setText(examination.getComment());
    }

    @Override
    public void close() {
        finish();
    }
}
