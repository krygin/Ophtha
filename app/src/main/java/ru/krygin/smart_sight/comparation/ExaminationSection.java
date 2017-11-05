package ru.krygin.smart_sight.comparation;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.text.DateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import ru.krygin.smart_sight.FileUriProvider;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.snapshot.model.Snapshot;
import ru.krygin.smart_sight.oculus.Oculus;

/**
 * Created by krygin on 06.08.17.
 */

public class ExaminationSection extends StatelessSection {

    @Inject
    FileUriProvider mFileUriProvider;

    private final Examination mExamination;
    private final Oculus mOculus;
    private OnShapshotClickListener mOnSnapshotClickListener;
    private OnExaminationClickListener mOnSectionClickListener;

    public ExaminationSection(Examination examination, Oculus oculus) {
        super(new SectionParameters.Builder(R.layout.item_oculus_examination_snapshot_preview)
                .headerResourceId(R.layout.item_oculus_examination_info_header).build());
        Injector.getAppComponent().inject(this);
        mExamination = examination;
        mOculus = oculus;
    }

    void setOnShapshotClickListener(OnShapshotClickListener onShapshotClickListener) {
        mOnSnapshotClickListener = onShapshotClickListener;
    }

    @Override
    public int getContentItemsTotal() {
        return getFilteredByOculusSnapshots(mExamination.getSnapshots()).size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Snapshot snapshot = getFilteredByOculusSnapshots(mExamination.getSnapshots()).get(position);
        itemViewHolder.imageView.setImageURI(mFileUriProvider.getUriForSnapshotFilename(snapshot.getFilename()));
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
        headerViewHolder.oculusExaminationTitleTextView.setText(Examination.Type.values()[mExamination.getType()].toString());
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

    private List<Snapshot> getFilteredByOculusSnapshots(List<Snapshot> snapshots) {
        Iterable<Snapshot> filteredSnapshots = Iterables.filter(snapshots, input -> input.getOculus().equals(mOculus));
        return Lists.newArrayList(filteredSnapshots);
    }
}
