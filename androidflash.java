package programming.edu.gobe.fireflash;


// important use android.hardware.camera2 library not camera1. is deprecated.
// use api 26 or higher for new apps.
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v6.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView imageFlashlight;
    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        imageFlashlight = (ImageView) findViewById(R.id.imageButton);
        imageFlashlight.setImageResource(R.drawable.poff);// set flashligt button to off
        final boolean hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        imageFlashlight.setOnClickListener(imgButtonHandler);
 }

    View.OnClickListener imgButtonHandler = new View.OnClickListener() {

        public void onClick(View view) {
            final boolean hasCameraFlash = getPackageManager().
                    hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            if (hasCameraFlash) {
                if (flashLightStatus)
                { flashLightOff();
                    imageFlashlight.setImageResource(R.drawable.poff);}//set flashlight button to off
                else
                { flashLightOn();
                    imageFlashlight.setImageResource(R.drawable.pon);}//set flasflight button to on
            } else {
                Toast.makeText(MainActivity.this, "No flash available on your device",
                        Toast.LENGTH_SHORT).show();// let user know there is no flash on device
            }
        }
    };
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);

            flashLightStatus = true;
        } catch (CameraAccessException e) {
        }
    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
        } catch (CameraAccessException e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CAMERA_REQUEST :
                if (grantResults.length > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //buttonEnable.setEnabled(false);// use this for testing if your permissions are set.
                    //buttonEnable.setText("Camera Enabled");

                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied for the Camera", Toast.LENGTH_SHORT).show();
                    // test here is permission is denied for camera on app.
                }
                break;
        }
    }
}