package ru.krygin.ophtha.examination;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.photodraweeview.PhotoDraweeView;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.ui.BaseActivity;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 14.08.17.
 */

public class CreateOrUpdateExaminationActivity extends BaseActivity implements OculusExaminationFragment.OnAddSnapshotButtonClickListener {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private CreateOrUpdateExaminationPagerAdapter mPagerAdapter;
    private Uri mPhotoUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_update_examination);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mPagerAdapter = new CreateOrUpdateExaminationPagerAdapter(getResources(), getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onAddSnapshotButtonClick(Oculus oculus) {
        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        ComponentName componentName = takePictureIntent.resolveActivity(getPackageManager());
        if (componentName != null) {
            // Create the File where the photo should go
            File photoFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "oculus_snapshots");
            if (!photoFolder.exists()) {
                photoFolder.mkdirs();
            }
            File photoFile = new File(photoFolder, String.format(Locale.getDefault(), "%d_%s", System.currentTimeMillis(), Oculus.DEXTER.name()));
            // Continue only if the File was successfully created
            mPhotoUri = FileProvider.getUriForFile(this,
                    "ru.krygin.ophtha.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, 123);
        }
    }
}
