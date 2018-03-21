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
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.core.ui.TitledFragment;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.examination.use_cases.GetExaminationsUseCase;
import ru.krygin.smart_sight.oculus.Oculus;
import ru.krygin.smart_sight.snapshot.ViewSnapshotActivity;
import ru.krygin.smart_sight.snapshot.model.Snapshot;
import ru.krygin.smart_sight.snapshot.use_cases.RemoveSnapshotUseCase;

/**
 * Created by krygin on 14.08.17.
 */

public abstract class OculusExaminationFragment extends TitledFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    FrameLayout mEmptyView;


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
                                        loadSnapshots();
                                    }

                                    @Override
                                    public void onError() {
                                        loadSnapshots();
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
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oculus_examination, container, false);
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
        loadSnapshots();
    }

    private void loadSnapshots() {
        getUseCaseHandler().execute(new GetExaminationsUseCase(), new GetExaminationsUseCase.RequestValues(mExaminationUUIDProvider.getExaminationUUID()), new UseCase.UseCaseCallback<GetExaminationsUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetExaminationsUseCase.ResponseValue response) {
                Examination examination = response.getExamination();
                List<Snapshot> snapshots = examination.getSnapshots();
                if (snapshots.isEmpty()) {
                    showEmptyView();
                } else {
                    showSnapshots(snapshots);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void showSnapshots(List<Snapshot> snapshots) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mOculusSnapshotsAdapter.setSnapshots(snapshots);
        mOculusSnapshotsAdapter.notifyDataSetChanged();
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mExaminationUUIDProvider = null;
    }
}
