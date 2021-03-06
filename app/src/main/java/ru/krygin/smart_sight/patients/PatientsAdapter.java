package ru.krygin.smart_sight.patients;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.krygin.smart_sight.DateTimeUtils;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.patients.model.Patient;

/**
 * Created by krygin on 02.08.17.
 */

public class PatientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Patient> mPatients = new ArrayList<>();
    private OnPatientClickListener mOnPatientClickListener;

    public void setOnPatientClickListener(OnPatientClickListener onPatientClickListener) {
        mOnPatientClickListener = onPatientClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(root, mOnPatientClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Patient patient = mPatients.get(position);
        viewHolder.patientNameTextView.setText(String.format("%s %s", patient.getLastName(), patient.getFirstName()));
        Date birthday = patient.getBirthday();
        if (birthday != null) {
            viewHolder.patientBirthdayTextView.setText(DateTimeUtils.getDateString(patient.getBirthday()));
        } else {
            viewHolder.patientBirthdayTextView.setText(null);
        }
    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }

    public void setPatients(List<Patient> patients) {
        mPatients.clear();
        if (patients != null) {
            mPatients.addAll(patients);
        }
    }

    public interface OnPatientClickListener {
        void onPatientClick(Patient patient);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.patient_name_text_view)
        protected TextView patientNameTextView;

        @BindView(R.id.patient_birthday_text_view)
        protected TextView patientBirthdayTextView;

        @OnClick(R.id.item_view)
        protected void OnClick(View view) {
            Patient patient = mPatients.get(getAdapterPosition());
            if (mOnPatientClickListener != null) {
                mOnPatientClickListener.onPatientClick(patient);
            }
        }

        public ViewHolder(View itemView, OnPatientClickListener onPatientClickListener) {
            super(itemView);
            mOnPatientClickListener = onPatientClickListener;
            ButterKnife.bind(this, itemView);
        }
    }
}
