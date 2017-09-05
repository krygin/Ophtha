package ru.krygin.ophtha.patients;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.comparation.ExaminationComparisionActivity;
import ru.krygin.ophtha.core.ui.BaseActivity;
import ru.krygin.ophtha.examination.CreateOrUpdateExaminationActivity;
import ru.krygin.ophtha.examination.ExaminationsPerOculusPagerAdapter;

/**
 * Created by krygin on 05.08.17.
 */

public class PatientActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    private ExaminationsPerOculusPagerAdapter mExaminationPerOculusPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mExaminationPerOculusPagerAdapter = new ExaminationsPerOculusPagerAdapter(getResources(), getSupportFragmentManager());
        mViewPager.setAdapter(mExaminationPerOculusPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_patient, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compare_menu_item:
                Intent intent = new Intent(this, ExaminationComparisionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab)
    void onClick(View view) {
        Intent intent = new Intent(this, CreateOrUpdateExaminationActivity.class);
        startActivity(intent);
    }
}
