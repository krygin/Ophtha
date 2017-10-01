package ru.krygin.ophtha.comparation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.ui.BaseFragment;
import ru.krygin.ophtha.examination.model.Snapshot;

/**
 * Created by krygin on 06.08.17.
 */

public class ExaminationsComparisionFragment extends BaseFragment implements OnOculusSnapshotPreviewClickListener {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private ExaminationsPagerAdapter mExaminationsPagerAdapter;
    private OnOculusSnapshotPreviewClickListener mOnOculusSnapshotPreviewClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnOculusSnapshotPreviewClickListener = (OnOculusSnapshotPreviewClickListener) getParentFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnOculusSnapshotPreviewClickListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExaminationsPagerAdapter = new ExaminationsPagerAdapter(getResources(), getChildFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oculus_examinations, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mViewPager.setAdapter(mExaminationsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onOculusSnapshotPreviewClick(Snapshot snapshot) {
        mOnOculusSnapshotPreviewClickListener.onOculusSnapshotPreviewClick(snapshot);
    }
}
