package kr.hmit.dmjs.ui.qr_scan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import kr.hmit.dmjs.ui.receive.ReceiveMainActivity;


public class ScanQR extends CaptureActivity {
    public static final int REQUEST_CODE = 8552;
    private String Qrcode = "";
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrScan = new IntentIntegrator(this);
        // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt("정보를 스캔해주세요");
        qrScan.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Qrcode = result.getContents();
                // ODM:입고대기 , RUN:출고대기
                if (Qrcode.contains("ODM")){
                    Intent intent = new Intent(ScanQR.this, ReceiveMainActivity.class);
                    //"D211207-021:ODM" => :ODM 지우고 보내기
                    intent.putExtra("Qrcode",Qrcode.substring(0,Qrcode.length()-4));
                    //입출고 대기화면에서 뒤로가기시 onActivityResult 태우기 위해 REQUEST_CODE
                    startActivityForResult(intent, ScanQR.REQUEST_CODE);
                }else if (Qrcode.contains("RUN")){

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        //입고대기,출고대기 화면에서 뒤로가기시 다시 스캔
        if (data == null && requestCode == REQUEST_CODE) qrScan.initiateScan();
    }
}

