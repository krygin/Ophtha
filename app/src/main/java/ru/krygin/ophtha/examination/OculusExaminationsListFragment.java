package ru.krygin.ophtha.examination;

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
import ru.krygin.ophtha.oculus.Oculus;
import ru.krygin.ophtha.snapshot.ViewSnapshotActivity;

/**
 * Created by krygin on 06.08.17.
 */

public abstract class OculusExaminationsListFragment extends TitledFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
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
        getUseCaseHandler().execute(new GetExaminationsUseCase(), new GetExaminationsUseCase.RequestValues(0, getOculus()), new UseCase.UseCaseCallback<GetExaminationsUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetExaminationsUseCase.ResponseValue response) {
                mSectionedRecyclerViewAdapter.removeAllSections();
                ExaminationSection.OnShapshotClickListener onShapshotClickListener = new ExaminationSection.OnShapshotClickListener() {
                    @Override
                    public void onSnapshotClick(GetExaminationsUseCase.Snapshot snapshot) {
                        Intent intent = new Intent(getContext(), ViewSnapshotActivity.class);
                        startActivity(intent);
                    }
                };

                for (GetExaminationsUseCase.Examination examination: response.getExaminations()) {
                    ExaminationSection examinationSection = new ExaminationSection(examination.getTitle(), examination.getDate(), examination.getSnapshots());
                    examinationSection.setOnShapshotClickListener(onShapshotClickListener);
                    mSectionedRecyclerViewAdapter.addSection(examinationSection);
                }
                mSectionedRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }



}
