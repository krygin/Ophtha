package ru.krygin.smart_sight.examination;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.smart_sight.FileUriProvider;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.snapshot.model.Snapshot;
import ru.krygin.smart_sight.oculus.Oculus;

public class OculusSnapshotsAdapter extends RecyclerView.Adapter<OculusSnapshotsAdapter.ViewHolder> {


    private final Oculus mOculus;
    private final OnShapshotClickListener mOnSnapshotClickListener;
    private List<Snapshot> mSnapshots = new ArrayList<>();

    @Inject
    FileUriProvider mFileUriProvider;

    public OculusSnapshotsAdapter(Oculus oculus, OnShapshotClickListener onShapshotClickListener) {
        mOculus = oculus;
        mOnSnapshotClickListener = onShapshotClickListener;
        Injector.getAppComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oculus_examination_snapshot_preview, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Snapshot snapshot = getFilteredByOculusSnapshots(mSnapshots).get(position);
        Uri uri = mFileUriProvider.getUriForSnapshotFilename(snapshot.getFilename());
        holder.imageView.setImageURI(uri);
        holder.indicatorView.setVisibility(!TextUtils.isEmpty(snapshot.getComment()) ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnSnapshotClickListener.onSnapshotClick(snapshot);
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSnapshotClickListener.onRemoveSnapshot(snapshot);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getFilteredByOculusSnapshots(mSnapshots).size();
    }

    public void setSnapshots(List<Snapshot> snapshots) {
        mSnapshots.clear();
        if (snapshots != null) {
            mSnapshots.addAll(snapshots);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.oculus_snapshot_image_view)
        SimpleDraweeView imageView;

        @BindView(R.id.oculus_comment_indicator_view)
        View indicatorView;

        @BindView(R.id.oculus_remove_snapshot_button)
        ImageButton removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnShapshotClickListener {
        void onSnapshotClick(Snapshot snapshot);
        void onRemoveSnapshot(Snapshot snapshot);
    }

    private List<Snapshot> getFilteredByOculusSnapshots(List<Snapshot> snapshots) {
        Iterable<Snapshot> filteredSnapshots = Iterables.filter(snapshots, input -> input.getOculus().equals(mOculus));
        return Lists.newArrayList(filteredSnapshots);
    }
}
