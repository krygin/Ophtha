package ru.krygin.smart_sight.snapshot;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;

import ru.krygin.smart_sight.FileUriProvider;
import ru.krygin.smart_sight.SmartSightFileManager;
import ru.krygin.smart_sight.core.Injector;
import ru.krygin.smart_sight.examination.model.Examination;
import ru.krygin.smart_sight.patients.model.Patient;
import ru.krygin.smart_sight.snapshot.model.Snapshot;

/**
 * Created by Ivan on 06.11.2017.
 */

public class PrintReportAdapter extends PrintDocumentAdapter {

    @Inject
    SmartSightFileManager mFileManager;

    private final Context mContext;
    private final Snapshot mSnapshot;
    private PrintedPdfDocument mReport;
    private int mPageHeight;
    private int mPageWidth;

    PrintReportAdapter(Context context, Snapshot snapshot) {
        mContext = context;
        mSnapshot = snapshot;
        Injector.getAppComponent().inject(this);
    }

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
        mReport = new PrintedPdfDocument(mContext, printAttributes1);

        mPageHeight = printAttributes1.getMediaSize().getHeightMils() / 1000 * 72;
        mPageWidth = printAttributes1.getMediaSize().getWidthMils() / 1000 * 72;

        if (cancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
            return;
        }
        PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                .Builder("oculus_report.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(1);

        PrintDocumentInfo info = builder.build();
        layoutResultCallback.onLayoutFinished(info, true);

    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        for (int i = 0; i < 1; i++) {
            if (pageInRange(pageRanges, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(mPageWidth, mPageHeight, i).create();

                PdfDocument.Page page = mReport.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    writeResultCallback.onWriteCancelled();
                    mReport.close();
                    mReport = null;
                    return;
                }
                drawReport(page);
                mReport.finishPage(page);
            }
        }

        try {
            mReport.writeTo(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
        } catch (IOException e) {
            writeResultCallback.onWriteFailed(e.toString());
            return;
        } finally {
            mReport.close();
            mReport = null;
        }

        writeResultCallback.onWriteFinished(pageRanges);
    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }


    private void drawReport(PdfDocument.Page page) {
        String doctorName = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString("doctor_name_preference", "");

        Examination examination = mSnapshot.getExamination();
        Patient patient = examination.getPatient();


        Canvas canvas = page.getCanvas();
        float currentContentYPosition = 0f;

        int titleBaseLine = 72;
        int leftMargin = 54;
        int bottomMargin = 54;
        int textHeight = 16;
        int logoHeight = 128;

        int spaceBetweenInfoBlocks = 8;

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textHeight);

        Paint logoPaint = new Paint();
        logoPaint.setColor(Color.GRAY);

        PdfDocument.PageInfo pageInfo = page.getInfo();


        canvas.drawRect(0, currentContentYPosition, pageInfo.getPageWidth(), logoHeight, logoPaint);
        currentContentYPosition += logoHeight;

        currentContentYPosition += spaceBetweenInfoBlocks;

        canvas.drawText(String.format("Ф. И. О.: %s %s %s", patient.getFirstName(), patient.getLastName(), patient.getPatronymic()), leftMargin, currentContentYPosition + textHeight, textPaint);
        currentContentYPosition += textHeight;

        currentContentYPosition += spaceBetweenInfoBlocks;

        canvas.drawText(String.format("Дата рождения: %s", DateFormat.getDateInstance().format(patient.getBirthday())), leftMargin, currentContentYPosition + textHeight, textPaint);
        currentContentYPosition += textHeight;

        currentContentYPosition += spaceBetweenInfoBlocks;

        canvas.drawText(String.format("Диагноз: %s", patient.getDiagnosis()), leftMargin, currentContentYPosition + textHeight, textPaint);
        currentContentYPosition += textHeight;

        currentContentYPosition += spaceBetweenInfoBlocks;

        canvas.drawText(String.format("Дата обследования: %s", DateFormat.getDateInstance().format(examination.getDate())), leftMargin, currentContentYPosition + textHeight, textPaint);
        currentContentYPosition += textHeight;

        currentContentYPosition += spaceBetweenInfoBlocks;

        Bitmap bitmap = null;
        try {
            File file = mFileManager.getSnapshotFile(mSnapshot.getFilename());
            Bitmap tempBitmap = BitmapFactory.decodeFile(file.getPath());
            ExifInterface exifInterface = new ExifInterface(file.getPath());
            //int rotation = getOrientation(mContext, uri);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int orientationDegrees = exifToDegrees(orientation);
            Matrix matrix = new Matrix();
            matrix.setRotate(orientationDegrees);
            bitmap = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), matrix, true);
            tempBitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, leftMargin, currentContentYPosition, logoPaint);
            currentContentYPosition += bitmap.getHeight();
        }

        currentContentYPosition += spaceBetweenInfoBlocks;
        String comment = mSnapshot.getComment() != null ? mSnapshot.getComment() : "";
        canvas.drawText(String.format("Комментарий: %s", comment), leftMargin, currentContentYPosition + textHeight, textPaint);
        currentContentYPosition += textHeight;

        currentContentYPosition += spaceBetweenInfoBlocks;


        canvas.drawText(String.format("Лечащий врач: %s", doctorName), leftMargin, pageInfo.getPageHeight() - bottomMargin - textHeight, textPaint);


    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
