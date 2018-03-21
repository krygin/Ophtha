package ru.krygin.smart_sight.examination;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.core.ui.TitledFragment;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.snapshot.model.Snapshot;
import ru.krygin.smart_sight.oculus.Oculus;
import ru.krygin.smart_sight.patients.use_cases.GetPatientUseCase;
import ru.krygin.smart_sight.patients.model.Patient;
import ru.krygin.smart_sight.snapshot.ViewSnapshotActivity;
import ru.krygin.smart_sight.snapshot.use_cases.RemoveSnapshotUseCase;


public abstract class OculusExaminationsListFragment extends TitledFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    FrameLayout mEmptyView;


    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    private PatientUUIDProvider mPatientUUIDProvider;

    private ExaminationSection.OnShapshotClickListener sOnShapshotClickListener  = new ExaminationSection.OnShapshotClickListener() {
        @Override
        public void onSnapshotClick(Snapshot snapshot) {
            Intent intent = ViewSnapshotActivity.newIntent(getContext(), snapshot.getUUID());
            startActivity(intent);
        }

        @Override
        public void onRemoveSnapshot(Snapshot snapshot) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Удаление снимка")
                    .setMessage("Вы действительно хотите удалить снимок?")
                    .setCancelable(true)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getUseCaseHandler().execute(new RemoveSnapshotUseCase(), new RemoveSnapshotUseCase.RequestValues(snapshot), new UseCase.UseCaseCallback<RemoveSnapshotUseCase.ResponseValue>() {
                                @Override
                                public void onSuccess(RemoveSnapshotUseCase.ResponseValue response) {
                                    loadPatient();
                                }

                                @Override
                                public void onError() {
                                    loadPatient();
                                }
                            });
                        }
                    })
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    };

    private ExaminationSection.OnExaminationClickListener onExaminationClickListener = new ExaminationSection.OnExaminationClickListener() {
        @Override
        public void onExaminationClick(Examination examination) {
            Intent intent = ExaminationActivity.newIntent(getContext(), mPatientUUIDProvider.getPatientUUID(), examination.getUUID());
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPatientUUIDProvider = (PatientUUIDProvider) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oculus_examinations_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 4;
                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);
    }

    protected abstract Oculus getOculus();

    @Override
    public void onResume() {
        super.onResume();
        loadPatient();
    }

    private void loadPatient() {
        getUseCaseHandler().execute(new GetPatientUseCase(), new GetPatientUseCase.RequestValues(mPatientUUIDProvider.getPatientUUID()), new UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetPatientUseCase.ResponseValue response) {
                Patient patient = response.getPatient();
                mSectionedRecyclerViewAdapter.removeAllSections();
                if (patient.getExaminations().isEmpty()) {
                    showEmptyView();
                } else {
                    showExaminations(patient.getExaminations());
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void showExaminations(List<Examination> examinations) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);

        for (Examination examination: examinations) {
            ExaminationSection examinationSection = new ExaminationSection(examination, getOculus());
            examinationSection.setOnShapshotClickListener(sOnShapshotClickListener);
            examinationSection.setOnSectionClickListener(onExaminationClickListener);
            mSectionedRecyclerViewAdapter.addSection(examinationSection);
        }
        mSectionedRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPatientUUIDProvider = null;
    }
}
