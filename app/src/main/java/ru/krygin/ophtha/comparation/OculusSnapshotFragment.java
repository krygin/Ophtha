package ru.krygin.ophtha.comparation;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.photodraweeview.PhotoDraweeView;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.core.ui.BaseFragment;

/**
 * Created by krygin on 06.08.17.
 */

public class OculusSnapshotFragment extends BaseFragment {

    @BindView(R.id.oculus_shapshot_image_view)
    PhotoDraweeView mOculusSnapshotImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oculus_snapshot, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mOculusSnapshotImageView.setPhotoUri(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"));
    }
}
