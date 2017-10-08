package ru.krygin.smart_sight.snapshot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;
import ru.krygin.smart_sight.R;
import ru.krygin.smart_sight.core.ui.BaseActivity;

/**
 * Created by krygin on 06.08.17.
 */

public class ViewSnapshotActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.oculus_shapshot_image_view)
    PhotoDraweeView mOculusSnapshotImageView;

    @BindView(R.id.oculus_comment_text_view)
    TextView mOculusCommentTextView;

    @BindView(R.id.edit_oculus_snapshot_comment_button)
    ImageButton mEditOculusSnapshotCommentButton;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    @OnClick(R.id.edit_oculus_snapshot_comment_button)
    void onClick(View view) {
        if (mOculusCommentTextView.isEnabled()) {
            mEditOculusSnapshotCommentButton.setImageResource(R.drawable.ic_edit_black_24dp);
            mOculusCommentTextView.setEnabled(false);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            mEditOculusSnapshotCommentButton.setImageResource(R.drawable.ic_check_white_24dp);
            mOculusCommentTextView.setEnabled(true);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @BindView(R.id.bottom_sheet)
    View mBottomSheet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_snapshot);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOculusSnapshotImageView.setMaximumScale(10f);
        mOculusSnapshotImageView.setPhotoUri(Uri.parse("http://www.yvetrov.ru/UserFiles/Image/yv_os.jpg"));
        mOculusCommentTextView.setText("Исследование в проходящем свете используют для диагностики патологии " +
                "в хрусталике и в стекловидном теле. Это прозрачные оптические среды глаза. " +
                "Исследование проводится в темной комнате. Матовую лампу мощностью около 100 Вт устанавливают " +
                "слева и несколько позади больного. Врач садится напротив на расстоянии 30–40 см и смотрит " +
                "через отверстие глазного зеркала – офтальмоскопа правым глазом, направляя отраженный зеркалом " +
                "офтальмоскопа пучок света в зрачок больного. Свет проходит внутрь глаза и отражается от сосудистой " +
                "оболочки и пигментного эпителия, при этом зрачок «загорается» красным цветом. Красный цвет объясняется " +
                "отчасти просвечиванием крови сосудистой оболочки, отчасти красно-бурым оттенком ретикального пигмента. " +
                "Ход лучей от зеркала в глаз и ход отраженного пучка по закону сопряженных фокусов совпадают. " +
                "В глаз врача через отверстие в офтальмоскопе попадают отраженные от глазного дна лучи, и зрачок светится.");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_snapshot, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export_to_pdf_menu_item:
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {

                    if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                        Toast.makeText(this, "FUCK", Toast.LENGTH_LONG).show();
                    } else {


                        PdfDocument pdfDocument = new PdfDocument();

                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(getWindow().getDecorView().getWidth(), getWindow().getDecorView().getHeight(), 0).create();

                        PdfDocument.Page page = pdfDocument.startPage(pageInfo);


                        View content = findViewById(android.R.id.content);
                        content.draw(page.getCanvas());

                        pdfDocument.finishPage(page);

                        File file = new File(getExternalFilesDir("OculusReports"), "qqq.pdf");
                        try {
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            pdfDocument.writeTo(new FileOutputStream(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pdfDocument.close();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
