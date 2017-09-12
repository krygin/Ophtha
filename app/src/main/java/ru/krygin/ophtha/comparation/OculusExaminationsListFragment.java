package ru.krygin.ophtha.comparation;

import android.content.Context;
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
import ru.krygin.ophtha.examination.PatientUUIDProvider;
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.examination.model.Snapshot;
import ru.krygin.ophtha.oculus.Oculus;
import ru.krygin.ophtha.patients.GetPatientUseCase;

/**
 * Created by krygin on 06.08.17.
 */

public abstract class OculusExaminationsListFragment extends TitledFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private SectionedRecyclerViewAdapter mOculusExaminationsAdapter;
    private OnOculusSnapshotPreviewClickListener mOnOculusSnapshotPreviewClickListener;
    private PatientUUIDProvider mPatientUUIDProvider;

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
        getUseCaseHandler().execute(new GetPatientUseCase(), new GetPatientUseCase.RequestValues(mPatientUUIDProvider.getPatientUUID()), new UseCase.UseCaseCallback<GetPatientUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetPatientUseCase.ResponseValue response) {
                mOculusExaminationsAdapter.removeAllSections();
                ExaminationSection.OnShapshotClickListener onShapshotClickListener = new ExaminationSection.OnShapshotClickListener() {
                    @Override
                    public void onSnapshotClick(Snapshot snapshot) {
                        mOnOculusSnapshotPreviewClickListener.onOculusSnapshotPreviewClick(snapshot);
                    }
                };

                for (Examination examination : response.getPatient().getExaminations()) {
                    ExaminationSection examinationSection = new ExaminationSection(examination, getOculus());
                    examinationSection.setOnShapshotClickListener(onShapshotClickListener);
                    mOculusExaminationsAdapter.addSection(examinationSection);
                }
                mOculusExaminationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }

    protected abstract Oculus getOculus();
}
