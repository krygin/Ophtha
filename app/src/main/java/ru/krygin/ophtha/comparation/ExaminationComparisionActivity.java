package ru.krygin.ophtha.comparation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.ui.BaseActivity;

/**
 * Created by krygin on 06.08.17.
 */

public class ExaminationComparisionActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.first_oculus_in_comparision_container)
    FrameLayout mFirstOculusInComparationContainer;

    @BindView(R.id.second_oculus_in_comparision_container)
    FrameLayout mSecondOculusInComparationContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_comparision);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.first_oculus_in_comparision_container, new ExaminationComparisionWrapperFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.second_oculus_in_comparision_container, new ExaminationComparisionWrapperFragment()).commit();
    }
}
