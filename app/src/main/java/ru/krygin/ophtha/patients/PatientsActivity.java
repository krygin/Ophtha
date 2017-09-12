package ru.krygin.ophtha.patients;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.krygin.ophtha.core.ui.BaseActivity;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.async.UseCase;

public class PatientsActivity extends BaseActivity {

    private PatientsAdapter mPatientsAdapter;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    protected FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Список пациентов");

        mPatientsAdapter = new PatientsAdapter();
        mPatientsAdapter.setOnPatientClickListener(patient -> {
            Intent intent = PatientActivity.newIntent(PatientsActivity.this, patient.getUUID());
            startActivity(intent);
        });

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mPatientsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUseCaseHandler().execute(
                new GetPatientsUseCase(),
                new GetPatientsUseCase.RequestValues(),
                new UseCase.UseCaseCallback<GetPatientsUseCase.ResponseValue>() {

                    @Override
                    public void onSuccess(GetPatientsUseCase.ResponseValue response) {
                        mPatientsAdapter.setPatients(response.getPatients());
                        mPatientsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }


    @OnClick(R.id.fab)
    void onFloatingActionButtonClick(View view) {
        Intent intent = new Intent(this, CreateOrUpdatePatientActivity.class);
        startActivity(intent);
    }

}
