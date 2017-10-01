package ru.krygin.ophtha.comparation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.ui.BaseFragment;
import ru.krygin.ophtha.examination.model.Snapshot;

/**
 * Created by krygin on 06.08.17.
 */

public class ExaminationComparisionWrapperFragment extends BaseFragment implements OnOculusSnapshotPreviewClickListener, OnCloseOculusPreviewButtonClickListener {

    @BindView(R.id.container)
    FrameLayout mContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_examination_comparision_wrapper, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction().replace(R.id.container, new ExaminationsComparisionFragment()).commit();
        }
    }


    @Override
    public void onOculusSnapshotPreviewClick(Snapshot snapshot) {
        getChildFragmentManager().beginTransaction().replace(R.id.container, OculusSnapshotFragment.newInstance(snapshot.getUUID())).addToBackStack(null).commit();
    }

    @Override
    public void onCloseOculusPreviewButtonClickListener() {
        getChildFragmentManager().popBackStack();
    }
}
