package ru.krygin.smart_sight.snapshot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.core.ui.BaseActivity;
import ru.krygin.smart_sight.oculus.GetOculusSnapshotUseCase;

/**
 * Created by krygin on 06.08.17.
 */

public class ViewSnapshotActivity extends BaseActivity {

    private static final String EXTRA_SNAPSHOT_UUID = "EXTRA_SNAPSHOT_UUID";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.oculus_shapshot_image_view)
    PhotoDraweeView mOculusSnapshotImageView;

    @BindView(R.id.oculus_comment_text_view)
    TextView mOculusCommentTextView;

    @BindView(R.id.edit_oculus_snapshot_comment_button)
    ImageButton mEditOculusSnapshotCommentButton;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    @OnClick(R.id.edit_oculus_snapshot_comment_button)
    void onClick(View view) {
        if (mOculusCommentTextView.isEnabled()) {
            mEditOculusSnapshotCommentButton.setImageResource(R.drawable.ic_edit_black_24dp);
            mOculusCommentTextView.setEnabled(false);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        super.onCreate(savedInstanceState);
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
        getUseCaseHandler().execute(new GetOculusSnapshotUseCase(), new GetOculusSnapshotUseCase.RequestValues(getIntent().getLongExtra(EXTRA_SNAPSHOT_UUID, 0)), new UseCase.UseCaseCallback<GetOculusSnapshotUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetOculusSnapshotUseCase.ResponseValue response) {
                mOculusSnapshotImageView.setPhotoUri(Uri.parse(response.getSnapshot().getFilename()));
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


                        PdfDocument pdfDocument = new PdfDocument();

                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight(), 0).create();

                        PdfDocument.Page page = pdfDocument.startPage(pageInfo);


                        View content = findViewById(android.R.id.content);
                        content.draw(page.getCanvas());

                        pdfDocument.finishPage(page);

                        File file = new File(getExternalFilesDir("OculusReports"), "qqq.pdf");
                        try {
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            pdfDocument.writeTo(new FileOutputStream(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pdfDocument.close();
                    }
                }
                return true;
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
