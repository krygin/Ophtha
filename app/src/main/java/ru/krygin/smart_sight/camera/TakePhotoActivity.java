package ru.krygin.smart_sight.camera;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.cameraview.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.core.ui.BaseActivity;
import ru.krygin.smart_sight.oculus.Oculus;
import ru.krygin.smart_sight.snapshot.model.Snapshot;
import ru.krygin.smart_sight.snapshot.use_cases.SaveSnapshotUseCase;

/**
 * Created by Ivan on 10.10.2017.
 */

public class TakePhotoActivity extends BaseActivity {

    private static final String EXTRA_EXAMINATION_UUID = "EXTRA_EXAMINATION_UUID";
    private static final String EXTRA_OCULUS = "EXTRA_OCULUS";
    @BindView(R.id.camera_view)
    CameraView mCameraView;
    private Handler mBackgroundHandler;
    private Oculus mOculus;
    private long mExaminationUUID;

    @OnClick(R.id.take_photo_button)
    void onClick(View view) {
        mCameraView.takePicture();
    }

    public static Intent newIntent(Context context, long examinationUUID, Oculus oculus) {
        Intent intent = new Intent(context, TakePhotoActivity.class);
        intent.putExtra(EXTRA_EXAMINATION_UUID, examinationUUID);
        intent.putExtra(EXTRA_OCULUS, oculus);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        ButterKnife.bind(this);
        mExaminationUUID = getIntent().getLongExtra(EXTRA_EXAMINATION_UUID, 0);
        mOculus = (Oculus) getIntent().getSerializableExtra(EXTRA_OCULUS);
        mCameraView.addCallback(new CameraView.Callback() {

            @Override
            public void onPictureTaken(CameraView cameraView, byte[] data) {
                getBackgroundHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        File photoFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "smart_sight_snapshots");
                        if (!photoFolder.exists()) {
                            photoFolder.mkdirs();
                        }
                        long photoTimestamp = System.currentTimeMillis();
                        File photoFile = new File(photoFolder, String.format(Locale.getDefault(), "%d_%s", photoTimestamp, mOculus.name()));

                        FileOutputStream fileOutputStream = null;
                        try {
                            fileOutputStream = new FileOutputStream(photoFile);
                            fileOutputStream.write(data);
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e) {
                            }
                        }

                        Uri photoUri = FileProvider.getUriForFile(TakePhotoActivity.this,
                    "ru.krygin.smart_sight.fileprovider",
                    photoFile);

                        Snapshot snapshot = new Snapshot();
                        snapshot.setUUID(photoTimestamp);
                        snapshot.setFilename(photoUri.toString());
                        snapshot.setOculus(mOculus);
                        getUseCaseHandler().execute(new SaveSnapshotUseCase(), new SaveSnapshotUseCase.RequestValues(mExaminationUUID, snapshot), new UseCase.UseCaseCallback<SaveSnapshotUseCase.ResponseValue>() {
                            @Override
                            public void onSuccess(SaveSnapshotUseCase.ResponseValue response) {
                                finish();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }
}
