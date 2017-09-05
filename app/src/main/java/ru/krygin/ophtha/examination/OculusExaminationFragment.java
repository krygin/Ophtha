package ru.krygin.ophtha.examination;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.core.ui.TitledFragment;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 14.08.17.
 */

public abstract class OculusExaminationFragment extends TitledFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.add_snapshot_button)
    Button mAddSnapshotButton;


    private OculusSnapshotsAdapter mOculusSnapshotsAdapter;
    private OnAddSnapshotButtonClickListener mOnAddSnapshotButtonClickListener;
    private Uri mPhotoUri;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnAddSnapshotButtonClickListener = (OnAddSnapshotButtonClickListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOculusSnapshotsAdapter = new OculusSnapshotsAdapter();
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
        getUseCaseHandler().execute(new GetExaminationsUseCase(), new GetExaminationsUseCase.RequestValues(0, getOculus()), new UseCase.UseCaseCallback<GetExaminationsUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetExaminationsUseCase.ResponseValue response) {

                mOculusSnapshotsAdapter.setSnapshots(response.getExaminations().get(0).getSnapshots());
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
        mOnAddSnapshotButtonClickListener = null;
    }

    @OnClick(R.id.add_snapshot_button)
    void onClick(View view) {
        dispatchTakePictureIntent();
        if (mOnAddSnapshotButtonClickListener != null) {
            //mOnAddSnapshotButtonClickListener.onAddSnapshotButtonClick(getOculus());
        }
    }

    interface OnAddSnapshotButtonClickListener {
        void onAddSnapshotButtonClick(Oculus oculus);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        ComponentName componentName = takePictureIntent.resolveActivity(getContext().getPackageManager());
        if (componentName != null) {
            // Create the File where the photo should go

            // Continue only if the File was successfully created
            File newOculusSnapshotFile = getFileUriProvider().getNewOculusSnapshotFile("eee", getOculus());
            mPhotoUri = FileProvider.getUriForFile(getContext(), "ru.krygin.ophtha.fileprovider", newOculusSnapshotFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            startActivityForResult(takePictureIntent, 123);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 123:
                File file = getFileUriProvider().getOculusSnapshotsFile("eee", getOculus());
                File[] files = file.listFiles();
                Log.e("qqqq", String.valueOf(files.length));
                List<GetExaminationsUseCase.Snapshot> snapshotList = new ArrayList<>();
                for (File f: files) {
                    GetExaminationsUseCase.Snapshot snapshot = new GetExaminationsUseCase.Snapshot(FileProvider.getUriForFile(getContext(), "ru.krygin.ophtha.fileprovider", f), "qwqweqwe");
                    snapshotList.add(snapshot);
                }
                GetExaminationsUseCase.snapshots.clear();
                GetExaminationsUseCase.snapshots.addAll(snapshotList);
                break;
        }
    }

}
