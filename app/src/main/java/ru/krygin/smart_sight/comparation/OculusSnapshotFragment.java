package ru.krygin.smart_sight.comparation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;
import ru.krygin.smart_sight.FileUriProvider;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.core.async.UseCase;
import ru.krygin.smart_sight.core.ui.BaseFragment;
import ru.krygin.smart_sight.oculus.GetOculusSnapshotUseCase;

/**
 * Created by krygin on 06.08.17.
 */

public class OculusSnapshotFragment extends BaseFragment {

    private static final String ARG_OCULUS_SNAPSHOT_UUID = "ARG_OCULUS_SNAPSHOT_UUID";

    @Inject FileUriProvider mFileUriProvider;

    @BindView(R.id.oculus_shapshot_image_view)
    PhotoDraweeView mOculusSnapshotImageView;

    @BindView(R.id.close_snapshot_button)
    ImageView mCloseSnapshotButton;

    private OnCloseOculusPreviewButtonClickListener mOnCloseOculusPreviewButtonClickListener;
    private long mOculusSnapshotUUID;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Injector.getAppComponent().inject(this);
        mOnCloseOculusPreviewButtonClickListener = (OnCloseOculusPreviewButtonClickListener) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOculusSnapshotUUID = getArguments().getLong(ARG_OCULUS_SNAPSHOT_UUID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oculus_snapshot, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUseCaseHandler().execute(new GetOculusSnapshotUseCase(), new GetOculusSnapshotUseCase.RequestValues(mOculusSnapshotUUID), new UseCase.UseCaseCallback<GetOculusSnapshotUseCase.ResponseValue>() {
            @Override
            public void onSuccess(GetOculusSnapshotUseCase.ResponseValue response) {
                mOculusSnapshotImageView.setImageURI(mFileUriProvider.getUriForSnapshotFilename(response.getSnapshot().getFilename()));
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.close_snapshot_button)
    void onClick(View view) {
        if (mOnCloseOculusPreviewButtonClickListener != null) {
            mOnCloseOculusPreviewButtonClickListener.onCloseOculusPreviewButtonClickListener();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnCloseOculusPreviewButtonClickListener = null;
    }

    public static Fragment newInstance(long oculusUUID) {
        Fragment fragment = new OculusSnapshotFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_OCULUS_SNAPSHOT_UUID, oculusUUID);
        fragment.setArguments(arguments);
        return fragment;
    }
}
