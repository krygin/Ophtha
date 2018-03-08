package ru.krygin.smart_sight.examination;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.smart_sight.DateTimeUtils;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.camera.TakePhotoActivity;
import ru.krygin.smart_sight.core.ui.BaseActivity;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.oculus.Oculus;

public class ExaminationActivity extends BaseActivity implements
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
        getSupportActionBar().setTitle(Examination.Type.values()[examination.getType()].toString());
        if (examination.getDate() != null) {
            getSupportActionBar().setSubtitle(DateTimeUtils.getDateString(examination.getDate()));
        } else {
            getSupportActionBar().setSubtitle(null);
        }
        mCommentTextView.setText(examination.getComment());
    }

    @Override
    public void requestNewSnapshot(Oculus oculus) {
        Intent intent = TakePhotoActivity.newIntent(this, mExaminationUUID, oculus);
        startActivity(intent);
    }

    @Override
    public void notifyChanges() {
        mPagerAdapter.notifyDataSetChanged();
    }
}
