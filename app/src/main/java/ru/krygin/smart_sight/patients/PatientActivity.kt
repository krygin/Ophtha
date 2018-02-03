package ru.krygin.smart_sight.patients

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_patient.*
import ru.krygin.smart_sight.DateTimeUtils
import ru.krygin.smart_sight.R
import ru.krygin.smart_sight.comparation.ExaminationComparisionActivity
import ru.krygin.smart_sight.core.async.UseCase
import ru.krygin.smart_sight.core.ui.BaseActivity
import ru.krygin.smart_sight.examination.CreateOrUpdateExaminationActivity
import ru.krygin.smart_sight.examination.ExaminationActivity
import ru.krygin.smart_sight.examination.ExaminationsPerOculusPagerAdapter
import ru.krygin.smart_sight.examination.PatientUUIDProvider
import ru.krygin.smart_sight.patients.model.Patient
import ru.krygin.smart_sight.patients.use_cases.GetPatientUseCase

class PatientActivity : BaseActivity(), PatientUUIDProvider {
    private lateinit var mExaminationPerOculusPagerAdapter: ExaminationsPerOculusPagerAdapter
    private var mPatientUUID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)
        ButterKnife.bind(this)

        mPatientUUID = intent.getLongExtra(EXTRA_PATIENT_UUID, 0)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Карта пациента"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mExaminationPerOculusPagerAdapter = ExaminationsPerOculusPagerAdapter(resources, supportFragmentManager)
        view_pager.adapter = mExaminationPerOculusPagerAdapter
        tab_layout.setupWithViewPager(view_pager)

        fab.setOnClickListener({
            val intent = CreateOrUpdateExaminationActivity.newIntent(this, mPatientUUID)
            startActivityForResult(intent, CREATE_EXAMINATION_REQUEST_CODE)
        })
    }

    override fun onResume() {
        super.onResume()
        useCaseHandler.execute<GetPatientUseCase.RequestValues, GetPatientUseCase.ResponseValue>(GetPatientUseCase(), GetPatientUseCase.RequestValues(mPatientUUID), object : UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue> {
            override fun onSuccess(response: GetPatientUseCase.ResponseValue) {
                val patient = response.patient
                patient_name_text_view.text = String.format("%s %s %s", patient.lastName, patient.firstName, patient.patronymic)
                patient_birthday_text_view.text = DateTimeUtils.getDateString(patient.birthday)
                patient_id_text_view.text = patient.patientId
                patient_diagnosis_text_view.text = patient.diagnosis
            }

            override fun onError() {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_patient, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.compare_menu_item -> {
                val compareExaminationsIntent = ExaminationComparisionActivity.newIntent(this, mPatientUUID)
                startActivity(compareExaminationsIntent)
                return true
            }
            R.id.edit_menu_item -> {
                val editPatientIntent = CreateOrUpdatePatientActivity.newIntent(this, mPatientUUID)
                startActivity(editPatientIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            CREATE_EXAMINATION_REQUEST_CODE -> {
                val patientUUID = data?.getLongExtra(CreateOrUpdateExaminationActivity.EXTRA_PATIENT_UUID, 0)!!
                val examinationUUID = data?.getLongExtra(CreateOrUpdateExaminationActivity.EXTRA_EXAMINATION_UUID, 0)!!
                val intent = ExaminationActivity.newIntent(this, patientUUID, examinationUUID)
                startActivity(intent)
            }
        }
    }

    override fun getPatientUUID(): Long {
        return mPatientUUID
    }

    companion object {

        private val EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID"

        fun newIntent(context: Context, patientUUID: Long): Intent {
            val intent = Intent(context, PatientActivity::class.java)
            intent.putExtra(EXTRA_PATIENT_UUID, patientUUID)
            return intent
        }
    }
}

private val CREATE_EXAMINATION_REQUEST_CODE = 1