package ru.krygin.ophtha.examination;

import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.krygin.ophtha.core.ui.TitledFragment;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 14.08.17.
 */

public class ExaminationPagerAdapter extends FragmentPagerAdapter {

    private final Resources mResources;
    private List<OculusExaminationFragment> mFragments = new ArrayList<>();

    public ExaminationPagerAdapter(Resources resources, FragmentManager fm) {
        super(fm);
        mResources = resources;
        mFragments.add(new OculusDexterExaminationFragment());
        mFragments.add(new OculusSinisterExaminationFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        @StringRes int titleResId = mFragments.get(position).getTitleResId();
        return titleResId != 0 ? mResources.getString(titleResId) : "";
    }

    public Oculus getOculus(int position) {
        return mFragments.get(position).getOculus();
    }
}
