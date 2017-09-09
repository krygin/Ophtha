package ru.krygin.ophtha.examination;

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
import ru.krygin.ophtha.examination.model.Examination;
import ru.krygin.ophtha.examination.model.Snapshot;

/**
 * Created by krygin on 06.08.17.
 */

public class ExaminationSection extends StatelessSection {

    private final Examination mExamination;
    private OnShapshotClickListener mOnSnapshotClickListener;
    private OnExaminationClickListener mOnSectionClickListener;

    public ExaminationSection(Examination examination) {
        super(new SectionParameters.Builder(R.layout.item_oculus_examination_snapshot_preview)
                .headerResourceId(R.layout.item_oculus_examination_info_header).build());
        mExamination = examination;
    }

    void setOnShapshotClickListener(OnShapshotClickListener onShapshotClickListener) {
        mOnSnapshotClickListener = onShapshotClickListener;
    }

    @Override
    public int getContentItemsTotal() {
        return mExamination.getSnapshots().size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Snapshot snapshot = mExamination.getSnapshots().get(position);
        itemViewHolder.imageView.setImageURI(snapshot.getSnapshotUri());
        itemViewHolder.indicatorView.setVisibility(!TextUtils.isEmpty(snapshot.getComment()) ? View.VISIBLE : View.GONE);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSnapshotClickListener != null) {
                    mOnSnapshotClickListener.onSnapshotClick(snapshot);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.oculusExaminationTitleTextView.setText(mExamination.getTitle());
        headerViewHolder.oculusExaminationDateTextView.setText(DateFormat.getDateInstance().format(mExamination.getDate()));
        headerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSectionClickListener != null) {
                    mOnSectionClickListener.onExaminationClick(mExamination);
                }
            }
        });
    }

    public void setOnSectionClickListener(OnExaminationClickListener onSectionClickListener) {
        mOnSectionClickListener = onSectionClickListener;
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

    public interface OnExaminationClickListener {
        void onExaminationClick(Examination examination);
    }
}
