package ru.krygin.ophtha.patients;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.krygin.ophtha.R;

/**
 * Created by krygin on 02.08.17.
 */

public class PatientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PatientsRepository.Patient> mPatients = new ArrayList<>();
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
        PatientsRepository.Patient patient = mPatients.get(position);
        viewHolder.patientsNameTextView.setText(String.format("%s %s", patient.getLastName(), patient.getFirstName()));
    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }

    public void setPatients(List<PatientsRepository.Patient> patients) {
        mPatients.clear();
        if (patients != null) {
            mPatients.addAll(patients);
        }
    }

    public interface OnPatientClickListener {
        void onPatientClick(PatientsRepository.Patient patient);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.patient_name_text_view)
        protected TextView patientsNameTextView;

        @OnClick(R.id.item_view)
        protected void OnClick(View view) {
            PatientsRepository.Patient patient = mPatients.get(getAdapterPosition());
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
