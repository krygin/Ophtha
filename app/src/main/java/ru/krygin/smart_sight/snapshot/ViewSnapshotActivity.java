package ru.krygin.smart_sight.snapshot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;
import ru.krygin.smart_sight.FileUriProvider;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.core.ui.BaseActivity;
import ru.krygin.smart_sight.snapshot.use_cases.GetOculusSnapshotUseCase;
import ru.krygin.smart_sight.snapshot.model.Snapshot;
import ru.krygin.smart_sight.snapshot.use_cases.GetExtendedOculusSnapshotUseCase;
import ru.krygin.smart_sight.snapshot.use_cases.RemoveSnapshotUseCase;
import ru.krygin.smart_sight.snapshot.use_cases.SaveSnapshotUseCase;
import ru.krygin.smart_sight.snapshot.use_cases.UpdateSnapshotUseCase;

public class ViewSnapshotActivity extends BaseActivity {

    private static final String EXTRA_SNAPSHOT_UUID = "EXTRA_SNAPSHOT_UUID";

    @Inject
    FileUriProvider mFileUriProvider;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.oculus_shapshot_image_view)
    PhotoDraweeView mOculusSnapshotImageView;

    @BindView(R.id.oculus_comment_text_view)
    TextView mOculusCommentTextView;

    @BindView(R.id.edit_oculus_snapshot_comment_button)
    ImageButton mEditOculusSnapshotCommentButton;

    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private long mSnapshotUUID;
    private Snapshot mSnapshot;

    @OnClick(R.id.edit_oculus_snapshot_comment_button)
    void onClick(View view) {
        if (mOculusCommentTextView.isEnabled()) {
            mEditOculusSnapshotCommentButton.setImageResource(R.drawable.ic_edit_black_24dp);
            mOculusCommentTextView.setEnabled(false);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mSnapshot.setComment(mOculusCommentTextView.getText().toString());
            getUseCaseHandler().execute(new UpdateSnapshotUseCase(), new UpdateSnapshotUseCase.RequestValues(mSnapshot), new UseCase.UseCaseCallback<UpdateSnapshotUseCase.ResponseValue>() {
                @Override
                public void onSuccess(UpdateSnapshotUseCase.ResponseValue response) {

                }

                @Override
                public void onError() {

                }
            });
        } else {
            mEditOculusSnapshotCommentButton.setImageResource(R.drawable.ic_check_white_24dp);
            mOculusCommentTextView.setEnabled(true);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @BindView(R.id.bottom_sheet)
    View mBottomSheet;

    public static Intent newIntent(Context context, long snapshotUUID) {
        Intent intent = new Intent(context, ViewSnapshotActivity.class);
        intent.putExtra(EXTRA_SNAPSHOT_UUID, snapshotUUID);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Injector.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        mSnapshotUUID = getIntent().getLongExtra(EXTRA_SNAPSHOT_UUID, 0);
        setContentView(R.layout.activity_view_snapshot);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUseCaseHandler().execute(new GetExtendedOculusSnapshotUseCase(), new GetExtendedOculusSnapshotUseCase.RequestValues(getIntent().getLongExtra(EXTRA_SNAPSHOT_UUID, 0)), new UseCase.UseCaseCallback<GetExtendedOculusSnapshotUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetExtendedOculusSnapshotUseCase.ResponseValue response) {
                mSnapshot = response.getSnapshot();
                mOculusSnapshotImageView.setPhotoUri(mFileUriProvider.getUriForSnapshotFilename(response.getSnapshot().getFilename()));
                mOculusCommentTextView.setText(response.getSnapshot().getComment());
            }

            @Override
            public void onError() {

            }
        });
        mOculusSnapshotImageView.setMaximumScale(10f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_snapshot, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export_to_pdf_menu_item:
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {

                    if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                        Toast.makeText(this, "FUCK", Toast.LENGTH_LONG).show();
                    } else {
                        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
                        printManager.print(getString(R.string.app_name), new PrintReportAdapter(this, mSnapshot), null);
                    }
                }
                return true;
            case R.id.remove_snapshot_menu_item:
                getUseCaseHandler().execute(new RemoveSnapshotUseCase(), new RemoveSnapshotUseCase.RequestValues(mSnapshot), new UseCase.UseCaseCallback<RemoveSnapshotUseCase.ResponseValue>() {
                    @Override
                    public void onSuccess(RemoveSnapshotUseCase.ResponseValue response) {
                        finish();
                    }

                    @Override
                    public void onError() {

                    }
                });
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
