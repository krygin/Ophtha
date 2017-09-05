package ru.krygin.ophtha.examination;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.krygin.ophtha.core.async.UseCase;
import ru.krygin.ophtha.oculus.Oculus;

/**
 * Created by krygin on 06.08.17.
 */

public class GetExaminationsUseCase extends UseCase<GetExaminationsUseCase.RequestValues, GetExaminationsUseCase.ResponseValue> {

    public static class RequestValues implements UseCase.RequestValues {

        private final long mPatientId;
        private final Oculus mOculus;

        public RequestValues(long patientId, Oculus oculus) {
            mPatientId = patientId;
            mOculus = oculus;
        }
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        ResponseValue responseValue = new ResponseValue(examinations);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static class ResponseValue implements UseCase.ResponseValue {

        private final List<Examination> mExaminations;

        public ResponseValue(List<Examination> examinations) {
            mExaminations = examinations;
        }

        public List<Examination> getExaminations() {
            return mExaminations;
        }
    }

    public static class Examination {
        private final List<Snapshot> mSnapshots;
        private final String mTitle;
        private final Date mDate;

        public Examination(String title, Date date, List<Snapshot> snapshots) {
            mTitle = title;
            mDate = date;
            mSnapshots = snapshots;
        }

        public String getTitle() {
            return mTitle;
        }

        public Date getDate() {
            return mDate;
        }

        public List<Snapshot> getSnapshots() {
            return mSnapshots;
        }
    }

    public static class Snapshot {
        private final Uri mSnapshotUri;
        private final String mComment;

        public Snapshot(Uri snapshotUri, String comment) {
            mSnapshotUri = snapshotUri;
            mComment = comment;
        }

        public Uri getSnapshotUri() {
            return mSnapshotUri;
        }

        public String getComment() {
            return mComment;
        }
    }

    public static List<Snapshot> snapshots = new ArrayList<Snapshot>() {{
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));
        add(new Snapshot(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"), "Comment"));

    }};


    private static List<Examination> examinations = new ArrayList<Examination>() {{
        add(new Examination("Examination Title 1", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 2", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 3", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 4", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 5", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 6", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 7", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 8", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 9", Calendar.getInstance().getTime(), snapshots));
        add(new Examination("Examination Title 10", Calendar.getInstance().getTime(), snapshots));


    }};


}
