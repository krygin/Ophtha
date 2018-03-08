package ru.krygin.smart_sight.patients

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_patients.*
import kotlinx.android.synthetic.main.content_patients.*
import ru.krygin.smart_sight.R
import ru.krygin.smart_sight.core.async.UseCase
import ru.krygin.smart_sight.core.ui.BaseActivity
import ru.krygin.smart_sight.patients.model.Patient
import ru.krygin.smart_sight.patients.use_cases.GetPatientsUseCase
import ru.krygin.smart_sight.settings.SettingsActivity

class PatientsActivity : BaseActivity() {

    private lateinit var mPatientsAdapter: PatientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Список пациентов"

        mPatientsAdapter = PatientsAdapter()
        mPatientsAdapter.setOnPatientClickListener { patient ->
            val intent = PatientActivity.newIntent(this@PatientsActivity, patient.uuid)
            startActivity(intent)
        }

        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_view.adapter = mPatientsAdapter
        fab.setOnClickListener({
            val intent = CreateOrUpdatePatientActivity.newIntent(this)
            startActivityForResult(intent, CREATE_PATIENT_REQUEST_CODE)
        })
    }

    override fun onResume() {
        super.onResume()
        getPatients(PatientsFilter(""))
    }

    private fun getPatients(filter: PatientsFilter) {
        useCaseHandler.execute<GetPatientsUseCase.RequestValues, GetPatientsUseCase.ResponseValue>(
                GetPatientsUseCase(),
                GetPatientsUseCase.RequestValues(filter),
                object : UseCase.UseCaseCallback<GetPatientsUseCase.ResponseValue> {

                    override fun onSuccess(response: GetPatientsUseCase.ResponseValue) {
                        val patients = response.patients
                        if (patients.isEmpty()) {
                            showEmpty()
                        } else {
                            showData(response.patients)
                        }
                    }

                    override fun onError() {

                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_patients, menu)
        val searchMenuItem = menu.findItem(R.id.search_menu_item)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.isIconified = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getPatients(PatientsFilter(query))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getPatients(PatientsFilter(newText))
                return true
            }

        })
        searchView.setOnCloseListener {
            getPatients(PatientsFilter(null))
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.settings_menu_item -> {
                    val settingsActivityIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(settingsActivityIntent)
                    true
                }
                R.id.search_menu_item -> {

                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CREATE_PATIENT_REQUEST_CODE -> {
                if (resultCode.equals(Activity.RESULT_OK)) {
                    data?.let {
                        val createdPatientUUID = it.getLongExtra(EXTRA_PATIENT_UUID, 0)
                        if (createdPatientUUID > 0) {
                            val intent = PatientActivity.newIntent(this, createdPatientUUID)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun showEmpty() {
        recycler_view.visibility = View.GONE
        empty_view.visibility = View.VISIBLE
    }

    private fun showData(patients: List<Patient>) {
        recycler_view.visibility = View.VISIBLE
        empty_view.visibility = View.GONE
        mPatientsAdapter.setPatients(patients)
        mPatientsAdapter.notifyDataSetChanged()
    }

}

private val CREATE_PATIENT_REQUEST_CODE = 1
