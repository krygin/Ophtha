package ru.krygin.smart_sight.patients

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout

import com.arellomobile.mvp.presenter.InjectPresenter

import java.util.Arrays
import java.util.Calendar

import butterknife.BindView
import kotlinx.android.synthetic.main.activity_create_or_update_patient.*
import ru.krygin.materialspinner.MaterialSpinner
import ru.krygin.smart_sight.DateTimeUtils
import ru.krygin.smart_sight.R
import ru.krygin.smart_sight.core.ui.BaseActivity
import ru.krygin.smart_sight.patients.model.Patient


/**
 * Created by krygin on 10.08.17.
 */

class CreateOrUpdatePatientActivity : BaseActivity(), PatientView {

    @InjectPresenter
    internal lateinit var mPresenter: CreateOrUpdatePatientPresenter

    private var mPatientUUID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_or_update_patient)
        mPatientUUID = intent.getLongExtra(EXTRA_PATIENT_UUID, 0)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(if (mPatientUUID == 0L) "Новый пациент" else "Редактирование карты")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val spinnerAdapter = ArrayAdapter<Patient.Gender>(this, android.R.layout.simple_spinner_item, Arrays.asList<Patient.Gender>(Patient.Gender.M, Patient.Gender.F))
        //        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //                R.array.genders, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.hint = Patient.Gender.UNDEFINDED
        spinner.adapter = spinnerAdapter

        patient_birthday_text_input_layout.editText?.setOnClickListener {
            val editText = it as EditText
            val taggedDateOfBirth = editText.tag as Calendar?
            val birthdayCalendar = taggedDateOfBirth ?: DateTimeUtils.getCurrentCalendar()
            val datePickerDialog = DatePickerDialog(this@CreateOrUpdatePatientActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val date = DateTimeUtils.getDate(year, month, dayOfMonth)
                val newDate = DateTimeUtils.getDateString(date)
                val dateToTag = DateTimeUtils.getCalendar(date)
                editText.setText(newDate)
                editText.tag = dateToTag
            }, birthdayCalendar!!.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DATE))
            datePickerDialog.datePicker.maxDate = DateTimeUtils.getCurrentCalendar().timeInMillis
            datePickerDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.loadPatient(mPatientUUID)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_create_or_update_patient, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_save -> if (true) {
                mPresenter.savePatient(
                        first_name_text_input_layout.editText?.text.toString(),
                        last_name_text_input_layout.editText?.text.toString(),
                        patronymic_name_text_input_layout.editText?.text.toString(),
                        spinner.selectedItem as Patient.Gender,
                        patient_id_text_input_layout.editText?.text.toString(),
                        (patient_birthday_text_input_layout.editText?.tag as Calendar).time,
                        patient_diagnosis_text_input_layout.editText?.text.toString()
                )
                return true
            } else
                return false
            else -> return super.onOptionsItemSelected(item)
        }
    }

//    private fun checkDataIsValid(): Boolean {
//        var result = true
//        if (TextUtils.isEmpty(mFirstNameTextInputLayout!!.editText!!.text.toString())) {
//            mFirstNameTextInputLayout!!.error = getString(R.string.field_required)
//            result = false
//        }
//
//        if (TextUtils.isEmpty(mLastNameTextInputLayout!!.editText!!.text.toString())) {
//            mLastNameTextInputLayout!!.error = getString(R.string.field_required)
//            result = false
//        }
//
//        if (TextUtils.isEmpty(mPatronymicTextInputLayout!!.editText!!.text.toString())) {
//            mPatronymicTextInputLayout!!.error = getString(R.string.field_required)
//            result = false
//        }
//
//        if (mGenderSpinner!!.selectedItem == Patient.Gender.UNDEFINDED) {
//            mGenderSpinner!!.error = getString(R.string.field_required)
//            result = false
//        }
//
//        if (TextUtils.isEmpty(mPatientIdTextInputLayout!!.editText!!.text.toString())) {
//            mPatientIdTextInputLayout!!.error = getString(R.string.field_required)
//            result = false
//        }
//
//        if (mPatientBirthdayTextInputLayout!!.editText!!.tag == null) {
//            mPatientBirthdayTextInputLayout!!.error = getString(R.string.field_required)
//            result = false
//        }
//
//        if (TextUtils.isEmpty(mDiagnosisTextInputLayout!!.editText!!.text.toString())) {
//            mDiagnosisTextInputLayout!!.error = getString(R.string.field_required)
//            result = false
//        }
//
//        return result
//    }

    override fun showPatient(patient: Patient) {
        first_name_text_input_layout.editText?.setText(patient.firstName)
        last_name_text_input_layout.editText?.setText(patient.lastName)
        patronymic_name_text_input_layout.editText?.setText(patient.patronymic)
        spinner.setSelection(patient.gender.ordinal)
        val calendar = DateTimeUtils.getCalendar(patient.birthday)
        patient_birthday_text_input_layout.editText?.setText(DateTimeUtils.getDateString(calendar))
        patient_birthday_text_input_layout.editText?.tag = calendar
        patient_id_text_input_layout.editText?.setText(patient.patientId)

    }

    override fun close(uuid: Long) {
        val data = Intent()
        data.putExtra(EXTRA_PATIENT_UUID, uuid)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    companion object {

        private val EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID"

        fun newIntent(context: Context, patientUUID: Long): Intent {
            val intent = newIntent(context)
            intent.putExtra(EXTRA_PATIENT_UUID, patientUUID)
            return intent
        }

        fun newIntent(context: Context): Intent {
            return Intent(context, CreateOrUpdatePatientActivity::class.java)
        }
    }
}

internal val EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID"
