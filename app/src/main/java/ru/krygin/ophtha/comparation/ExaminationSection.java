package ru.krygin.ophtha.comparation;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import ru.krygin.ophtha.R;
import ru.krygin.ophtha.examination.GetExaminationsUseCase;
import ru.krygin.ophtha.examination.model.Snapshot;

/**
 * Created by krygin on 06.08.17.
 */

public class ExaminationSection extends StatelessSection {

    private final String mTitle;
    private final Date mDate;
    private final List<Snapshot> mSnapshots;
    private OnShapshotClickListener mOnSnapshotClickListener;

    public ExaminationSection(String title, Date date, List<Snapshot> snapshots) {
        super(new SectionParameters.Builder(R.layout.item_oculus_examination_snapshot_preview)
                .headerResourceId(R.layout.item_oculus_examination_info_header).build());
        mTitle = title;
        mDate = date;
        mSnapshots = snapshots;
    }

    public void setOnSnapshotClickListener(OnShapshotClickListener onSnapshotClickListener) {
        mOnSnapshotClickListener = onSnapshotClickListener;
    }

    @Override
    public int getContentItemsTotal() {
        return mSnapshots.size();
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Snapshot snapshot = mSnapshots.get(position);
        itemViewHolder.imageView.setImageURI(snapshot.getSnapshotUri());
        itemViewHolder.indicatorView.setVisibility(!TextUtils.isEmpty(snapshot.getComment()) ? View.VISIBLE : View.GONE);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSnapshotClickListener.onSnapshotClick(snapshot);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.oculusExaminationTitleTextView.setText(mTitle);
        headerViewHolder.oculusExaminationDateTextView.setText(DateFormat.getDateInstance().format(mDate));

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.oculus_snapshot_image_view)
        SimpleDraweeView imageView;

        @BindView(R.id.oculus_comment_indicator_view)
        View indicatorView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.oculus_examination_title_text_view)
        TextView oculusExaminationTitleTextView;

        @BindView(R.id.oculus_examination_date_text_view)
        TextView oculusExaminationDateTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnShapshotClickListener {
        void onSnapshotClick(Snapshot snapshot);
    }
}
