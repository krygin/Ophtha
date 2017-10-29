package ru.krygin.smart_sight.examination;

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
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.core.ui.TitledFragment;
import ru.krygin.smart_sight.examination.use_cases.GetExaminationsUseCase;
import ru.krygin.smart_sight.oculus.Oculus;
import ru.krygin.smart_sight.snapshot.ViewSnapshotActivity;
import ru.krygin.smart_sight.snapshot.model.Snapshot;

/**
 * Created by krygin on 14.08.17.
 */

public abstract class OculusExaminationFragment extends TitledFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private OculusSnapshotsAdapter mOculusSnapshotsAdapter;
    private ExaminationUUIDProvider mExaminationUUIDProvider;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mExaminationUUIDProvider = (ExaminationUUIDProvider) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOculusSnapshotsAdapter = new OculusSnapshotsAdapter(getOculus(), new OculusSnapshotsAdapter.OnShapshotClickListener() {
            @Override
            public void onSnapshotClick(Snapshot snapshot) {
                Intent intent = ViewSnapshotActivity.newIntent(getContext(), snapshot.getUUID());
                startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oculus_examinations_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mOculusSnapshotsAdapter);
    }

    protected abstract Oculus getOculus();

    @Override
    public void onResume() {
        super.onResume();
        getUseCaseHandler().execute(new GetExaminationsUseCase(), new GetExaminationsUseCase.RequestValues(mExaminationUUIDProvider.getExaminationUUID()), new UseCase.UseCaseCallback<GetExaminationsUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetExaminationsUseCase.ResponseValue response) {

                mOculusSnapshotsAdapter.setSnapshots(response.getExamination().getSnapshots());
                mOculusSnapshotsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mExaminationUUIDProvider = null;
    }
}
