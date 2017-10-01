package ru.krygin.ophtha.examination;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.ui.TitledFragment;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.examination.model.Snapshot;
import ru.krygin.ophtha.oculus.Oculus;
import ru.krygin.ophtha.patients.use_cases.GetPatientUseCase;
import ru.krygin.ophtha.patients.model.Patient;
import ru.krygin.ophtha.snapshot.ViewSnapshotActivity;

/**
 * Created by krygin on 06.08.17.
 */

public abstract class OculusExaminationsListFragment extends TitledFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    private PatientUUIDProvider mPatientUUIDProvider;

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
        getUseCaseHandler().execute(new GetPatientUseCase(), new GetPatientUseCase.RequestValues(mPatientUUIDProvider.getPatientUUID()), new UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetPatientUseCase.ResponseValue response) {
                Patient patient = response.getPatient();
                mSectionedRecyclerViewAdapter.removeAllSections();

                ExaminationSection.OnShapshotClickListener onShapshotClickListener = new ExaminationSection.OnShapshotClickListener() {
                    @Override
                    public void onSnapshotClick(Snapshot snapshot) {
                        Intent intent = new Intent(getContext(), ViewSnapshotActivity.class);
                        startActivity(intent);
                    }
                };

                ExaminationSection.OnExaminationClickListener onExaminationClickListener = new ExaminationSection.OnExaminationClickListener() {
                    @Override
                    public void onExaminationClick(Examination examination) {
                        Intent intent = ExaminationActivity.newIntent(getContext(), mPatientUUIDProvider.getPatientUUID(), examination.getUUID());
                        startActivity(intent);
                    }
                };

                for (Examination examination: patient.getExaminations()) {
                    ExaminationSection examinationSection = new ExaminationSection(examination, getOculus());
                    examinationSection.setOnShapshotClickListener(onShapshotClickListener);
                    examinationSection.setOnSectionClickListener(onExaminationClickListener);
                    mSectionedRecyclerViewAdapter.addSection(examinationSection);
                }
                mSectionedRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError() {

            }
        });


//        getUseCaseHandler().execute(new GetExaminationsUseCase(), new GetExaminationsUseCase.RequestValues(0, getOculus()), new UseCase.UseCaseCallback<GetExaminationsUseCase.ResponseValue>() {
//            @Override
//            public void onSuccess(GetExaminationsUseCase.ResponseValue response) {
//                mSectionedRecyclerViewAdapter.removeAllSections();
//                ExaminationSection.OnShapshotClickListener onShapshotClickListener = new ExaminationSection.OnShapshotClickListener() {
//                    @Override
//                    public void onSnapshotClick(Snapshot snapshot) {
//                        Intent intent = new Intent(getContext(), ViewSnapshotActivity.class);
//                        startActivity(intent);
//                    }
//                };
//
//                for (Examination examination: response.getExamination()) {
//                    ExaminationSection examinationSection = new ExaminationSection(examination.getTitle(), examination.getDate(), examination.getSnapshots());
//                    examinationSection.setOnShapshotClickListener(onShapshotClickListener);
//                    mSectionedRecyclerViewAdapter.addSection(examinationSection);
//                }
//                mSectionedRecyclerViewAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPatientUUIDProvider = null;
    }
}
