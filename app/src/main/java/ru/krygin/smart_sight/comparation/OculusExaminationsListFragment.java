package ru.krygin.smart_sight.comparation;

import android.content.Context;
import android.content.DialogInterface;
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
import ru.krygin.smart_sight.examination.PatientUUIDProvider;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.snapshot.model.Snapshot;
import ru.krygin.smart_sight.oculus.Oculus;
import ru.krygin.smart_sight.patients.use_cases.GetPatientUseCase;
import ru.krygin.smart_sight.snapshot.use_cases.RemoveSnapshotUseCase;

/**
 * Created by krygin on 06.08.17.
 */

public abstract class OculusExaminationsListFragment extends TitledFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    FrameLayout mEmptyView;

    private SectionedRecyclerViewAdapter mOculusExaminationsAdapter;
    private OnOculusSnapshotPreviewClickListener mOnOculusSnapshotPreviewClickListener;
    private PatientUUIDProvider mPatientUUIDProvider;
    private ExaminationSection.OnShapshotClickListener mOnShapshotClickListener = new ExaminationSection.OnShapshotClickListener() {
        @Override
        public void onSnapshotClick(Snapshot snapshot) {
            mOnOculusSnapshotPreviewClickListener.onOculusSnapshotPreviewClick(snapshot);
        }
    };



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnOculusSnapshotPreviewClickListener = (OnOculusSnapshotPreviewClickListener) getParentFragment();
        mPatientUUIDProvider = (PatientUUIDProvider) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnOculusSnapshotPreviewClickListener = null;
        mPatientUUIDProvider = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOculusExaminationsAdapter = new SectionedRecyclerViewAdapter();
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
                switch (mOculusExaminationsAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 4;
                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mOculusExaminationsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPatient();
    }

    private void loadPatient(){
        getUseCaseHandler().execute(new GetPatientUseCase(), new GetPatientUseCase.RequestValues(mPatientUUIDProvider.getPatientUUID()), new UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetPatientUseCase.ResponseValue response) {
                List<Examination> examinations = response.getPatient().getExaminations();
                if (examinations.isEmpty()) {
                    showEmptyView();
                } else {
                    showExaminations(examinations);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showExaminations(List<Examination> examinations) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mOculusExaminationsAdapter.removeAllSections();
        for (Examination examination : examinations) {
            ExaminationSection examinationSection = new ExaminationSection(examination, getOculus());
            examinationSection.setOnShapshotClickListener(mOnShapshotClickListener);
            mOculusExaminationsAdapter.addSection(examinationSection);
        }
        mOculusExaminationsAdapter.notifyDataSetChanged();
    }

    protected abstract Oculus getOculus();
}
