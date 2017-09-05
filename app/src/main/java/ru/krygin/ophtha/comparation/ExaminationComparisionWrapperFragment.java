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
import ru.krygin.ophtha.examination.GetExaminationsUseCase;

/**
 * Created by krygin on 06.08.17.
 */

public class ExaminationComparisionWrapperFragment extends BaseFragment implements OnOculusSnapshotPreviewClickListener {

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
        getChildFragmentManager().beginTransaction().replace(R.id.container, new ExaminationsComparisionFragment()).commit();
    }


    @Override
    public void onOculusSnapshotPreviewClick(GetExaminationsUseCase.Snapshot snapshot) {
        getChildFragmentManager().beginTransaction().replace(R.id.container, new OculusSnapshotFragment()).commit();
    }
}
