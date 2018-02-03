package ru.krygin.smart_sight.examination

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import butterknife.ButterKnife
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_create_or_update_examination.*
import ru.krygin.smart_sight.DateTimeUtils
import ru.krygin.smart_sight.R
import ru.krygin.smart_sight.core.ui.BaseActivity
import ru.krygin.smart_sight.examination.model.Examination
import java.util.*

class CreateOrUpdateExaminationActivity : BaseActivity(), CreateOrUpdateExaminationView {

    @InjectPresenter
    internal lateinit var mPresenter: CreateOrUpdateExaminationPresenter

    private var mExaminationUUID: Long = 0
    private var mPatientUUID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_or_update_examination)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        mExaminationUUID = intent.getLongExtra(EXTRA_EXAMINATION_UUID, 0)
        mPatientUUID = intent.getLongExtra(EXTRA_PATIENT_UUID, 0)
        supportActionBar?.setTitle(if (mExaminationUUID == 0L) "Новое обследование" else "Редактирование обследования")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val spinnerAdapter = ArrayAdapter<Examination.Type>(this, android.R.layout.simple_spinner_item, Arrays.asList<Examination.Type>(Examination.Type.OCULAR_FUNDUS, Examination.Type.SEGMENT_ANTERIOR))
        //        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //                R.array.genders, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        examination_type_spinner.hint = Examination.Type.UNDEFINDED
        examination_type_spinner.adapter = spinnerAdapter

        date_text_input_layout.editText?.setOnClickListener {
            val editText = it as EditText
            val taggedDateOfBirth = editText.tag as Calendar?
            val birthdayCalendar = taggedDateOfBirth ?: DateTimeUtils.getCurrentCalendar()
            val datePickerDialog = DatePickerDialog(this@CreateOrUpdateExaminationActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val date = DateTimeUtils.getDate(year, month, dayOfMonth)
                val dateToTag = DateTimeUtils.getCalendar(date)
                val newDate = DateTimeUtils.getDateString(date)
                editText.setText(newDate)
                editText.tag = dateToTag
            }, birthdayCalendar!!.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DATE))
            datePickerDialog.datePicker.maxDate = DateTimeUtils.getCurrentCalendar().timeInMillis
            datePickerDialog.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_create_or_update_examination, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_save -> {
                if (checkDataIsValid())
                    mPresenter.saveExamination(mPatientUUID,
                            examination_type_spinner.selectedItem as Examination.Type,
                            (date_text_input_layout.editText?.tag as Calendar).time,
                            comment_text_input_layout.editText?.text.toString()
                    )
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun checkDataIsValid(): Boolean {
        var result = true
        if (examination_type_spinner.selectedItem == Examination.Type.UNDEFINDED) {
            examination_type_spinner.error = getString(R.string.field_required)
            result = false
        }

        if (date_text_input_layout.editText!!.tag == null) {
            date_text_input_layout.error = getString(R.string.field_required)
            result = false
        }
        return result
    }

    override fun onResume() {
        super.onResume()
        mPresenter.loadExamination(mExaminationUUID)
    }

    override fun showExamination(examination: Examination) {
        examination_type_spinner.setSelection(examination.type)
        val calendar = DateTimeUtils.getCalendar(examination.date)
        date_text_input_layout.editText?.setText(DateTimeUtils.getDateString(calendar))
        date_text_input_layout.editText?.tag = calendar
        comment_text_input_layout.editText?.setText(examination.comment)
    }

    override fun close(patientUUID: Long, examinationUUID: Long) {
        val data = Intent()
        data.putExtra(EXTRA_PATIENT_UUID, patientUUID)
        data.putExtra(EXTRA_EXAMINATION_UUID, examinationUUID)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    companion object {


        @JvmStatic
        public val EXTRA_EXAMINATION_UUID = "EXTRA_EXAMINATION_UUID"

        @JvmStatic
        public val EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID"

        @JvmStatic
        fun newIntent(context: Context, patientUUID: Long): Intent {
            val intent = Intent(context, CreateOrUpdateExaminationActivity::class.java)
            intent.putExtra(EXTRA_PATIENT_UUID, patientUUID)
            return intent
        }

        @JvmStatic
        fun newIntent(context: Context, patientUUID: Long, examinationUUID: Long): Intent {
            val intent = newIntent(context, patientUUID)
            intent.putExtra(EXTRA_EXAMINATION_UUID, examinationUUID)
            return intent
        }
    }
}
