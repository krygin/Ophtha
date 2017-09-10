package ru.krygin.ophtha.examination;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.examination.model.Snapshot;

/**
 * Created by krygin on 14.08.17.
 */

public class OculusSnapshotsAdapter extends RecyclerView.Adapter<OculusSnapshotsAdapter.ViewHolder> {


    private List<Snapshot> mSnapshots = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oculus_examination_snapshot_preview, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Snapshot snapshot = mSnapshots.get(position);
        holder.imageView.setImageURI(snapshot.getFilename());
        holder.indicatorView.setVisibility(!TextUtils.isEmpty(snapshot.getComment()) ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return mSnapshots.size();
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
