package ru.krygin.smart_sight.examination;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.smart_sight.DateTimeUtils;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.camera.TakePhotoActivity;
import ru.krygin.smart_sight.core.ui.BaseActivity;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.oculus.Oculus;

/**
 * Created by krygin on 14.08.17.
 */

public class ExaminationActivity extends BaseActivity implements
        OculusExaminationFragment.OnAddSnapshotButtonClickListener,
        ExaminationUUIDProvider,
        ExaminationView {

    private static final String EXTRA_EXAMINATION_UUID = "EXTRA_EXAMINATION_UUID";
    private static final String EXTRA_PATIENT_UUID = "EXTRA_PATIENT_UUID";

    @InjectPresenter
    ExaminationActivityPresenter mPresenter;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.diagnosis_text_view)
    TextView mDiagnosisTextView;

    @BindView(R.id.comment_text_view)
    TextView mCommentTextView;

    private ExaminationPagerAdapter mPagerAdapter;
    private Uri mPhotoUri;
    private long mExaminationUUID;
    private long mPhotoTimestamp;
    private long mPatientUUID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        ButterKnife.bind(this);
        mExaminationUUID = getIntent().getLongExtra(EXTRA_EXAMINATION_UUID, 0);
        mPatientUUID = getIntent().getLongExtra(EXTRA_PATIENT_UUID, 0);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPagerAdapter = new ExaminationPagerAdapter(getResources(), getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bindOnClickListener(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bindOnClickListener(0);
    }

    private void bindOnClickListener(int position) {
        mFab.setOnClickListener(v -> mPresenter.addOculusSnapshot(mPagerAdapter.getOculus(position)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_examination, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.edit_menu_item:
                Intent intent = CreateOrUpdateExaminationActivity.newIntent(this, mPatientUUID, mExaminationUUID);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadPatient(mPatientUUID, mExaminationUUID);
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
            File photoFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "smart_sight_snapshots");
            if (!photoFolder.exists()) {
                photoFolder.mkdirs();
            }
            File photoFile = new File(photoFolder, String.format(Locale.getDefault(), "%d_%s", mPhotoTimestamp, Oculus.DEXTER.name()));
            // Continue only if the File was successfully created
            mPhotoUri = FileProvider.getUriForFile(this,
                    "ru.krygin.smart_sight.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, 123);
        }
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ExaminationActivity.class);
        return intent;
    }

    public static Intent newIntent(Context context, long patientUUID, long examinationUUID) {
        Intent intent = newIntent(context);
        intent.putExtra(EXTRA_EXAMINATION_UUID, examinationUUID);
        intent.putExtra(EXTRA_PATIENT_UUID, patientUUID);
        return intent;
    }

    @Override
    public long getExaminationUUID() {
        return mExaminationUUID;
    }

    @Override
    public void showExamination(Examination examination) {
        getSupportActionBar().setTitle(examination.getTitle());
        getSupportActionBar().setSubtitle(DateTimeUtils.getDateString(examination.getDate()));

        mDiagnosisTextView.setText(examination.getDiagnosis());
        mCommentTextView.setText(examination.getComment());
    }

    @Override
    public void requestNewSnapshot(Oculus oculus) {
        Intent intent = TakePhotoActivity.newIntent(this, mExaminationUUID, oculus);
        startActivity(intent);
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        ComponentName componentName = takePictureIntent.resolveActivity(getPackageManager());
//        if (componentName != null) {
//            // Create the File where the photo should go
//            File photoFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "smart_sight_snapshots");
//            if (!photoFolder.exists()) {
//                photoFolder.mkdirs();
//            }
//            mPhotoTimestamp = System.currentTimeMillis();
//            File photoFile = new File(photoFolder, String.format(Locale.getDefault(), "%d_%s", mPhotoTimestamp, Oculus.DEXTER.name()));
//            // Continue only if the File was successfully created
//            mPhotoUri = FileProvider.getUriForFile(this,
//                    "ru.krygin.smart_sight.fileprovider",
//                    photoFile);
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
//            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            startActivityForResult(takePictureIntent, 123);
//        }
    }

    @Override
    public void notifyChanges() {
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 123:
                if (resultCode == Activity.RESULT_OK) {
                    mPresenter.addNewSnapshot(mPhotoUri.toString(), mPhotoTimestamp, mExaminationUUID);
                }
        }
    }
}
