package kr.hmit.dmjs.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.ui.board.BoardFragment;
import kr.hmit.dmjs.ui.calendar.CalendarFragment;
import kr.hmit.dmjs.ui.qr_scan.ShipmentManagementActivity;
import kr.hmit.dmjs.ui.settings.SettingFragment;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_view_pager.BaseViewPager;
import kr.hmit.base.base_view_pager.ViewPagerAdapter;
import kr.hmit.base.util.PermissionUtils;

public class MainActivity extends BaseActivity {
    //================================
    // final
    //================================
    public static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyyMMdd_HHmmss";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int REQUEST_CODE_TAKE_PHOTO = 1111;

    private int PAGE_BOARD;
    private int PAGE_CALENDAR;

    //================================
    // layout
    //================================
    private BaseViewPager viewPager;
    private BoardFragment fragmentBoard;
    private CalendarFragment fragmentCalendar;
    private SettingFragment fragmentSetting;

    private ImageView imgMenu1;
    private ImageView imgMenu2;
    private ImageView imgMenu3;
    private ImageView imgMenu4;
    private ImageView imgMenu5;
    private ImageView imgMenu6;

    //================================
    // variable
    //================================
    private ViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> mListFragment;

    //================================
    // initialize
    //================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        imgMenu1 = findViewById(R.id.imgMenu1);
        imgMenu2 = findViewById(R.id.imgMenu2);
        imgMenu3 = findViewById(R.id.imgMenu3);
        imgMenu4 = findViewById(R.id.imgMenu4);
        imgMenu4.setOnClickListener(v -> goScan());
        imgMenu5 = findViewById(R.id.imgMenu5);
        imgMenu5.setOnClickListener(v -> goCamera());
        imgMenu6 = findViewById(R.id.imgMenu6);

        initViewPager();
    }

    /**
     * Viewpager 초기화
     */
    private void initViewPager() {
        viewPager = findViewById(R.id.viewPagerMain);

        fragmentCalendar = CalendarFragment.newInstance();
        fragmentBoard = BoardFragment.newInstance();
        fragmentSetting = SettingFragment.newInstance();

        mListFragment = new ArrayList<>();

        int nIndex = 0;
        mListFragment.add(fragmentBoard);
        mListFragment.add(fragmentCalendar);
        mListFragment.add(fragmentSetting);
        PAGE_BOARD = nIndex++;

        mViewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager(), mListFragment);
        viewPager.setAdapter(mViewPagerAdapter);
        //viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(mListFragment.size() - 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(PAGE_BOARD);
        System.out.println("자~~~~스캔 드가자~~~~~~ 아차 여기가 아니라구~!!! 1111111111111111111");
    }

    private void setPage(int position) {
        imgMenu2.setSelected(false);
        imgMenu3.setSelected(false);

        if (position == PAGE_BOARD) {
            imgMenu2.setSelected(true);
        } else {
            imgMenu3.setSelected(true);
        }

        System.out.println("자~~~~스캔 드가자~~~~~~ 아차 여기가 아니라구~!!! 22222222222222222");
    }

    @Override
    protected void initialize() {

    }


    private File fileTakePhoto;
    private Uri fileUri;

    private void goCamera() {
        if (!PermissionUtils.checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !PermissionUtils.checkPermission(mContext, Manifest.permission.CAMERA)) {
            PermissionUtils.requestPermission(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
            return;
        }


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileTakePhoto = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        fileUri = FileProvider.getUriForFile(mContext, mSettings.Value.FileProviderPath, fileTakePhoto);

        List<ResolveInfo> resolvedIntentActivities = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            mContext.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);

        System.out.println("자~~~~스캔 드가자~~~~~~ 아차 여기가 아니라구~!!! 44444444444444444444444");
    }

    @SuppressLint("SimpleDateFormat")
    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "smfactory");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS, Locale.US).format(new Date());
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + mSettings.Value.LoginID + "_IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * QR 스캔
     */
    private void goScan() {
        System.out.println("자~~~~스캔 드가자~~~~~~ 아차 여기가 아니라구~!!! 333333333");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ShipmentManagementActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }
}
