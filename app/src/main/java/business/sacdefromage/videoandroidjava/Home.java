package business.sacdefromage.videoandroidjava;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Home extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1233211;
    VideoView viewVideo;
    Button btnTakeVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnTakeVideo = findViewById(R.id.btnTakeVideo);
        btnTakeVideo.setOnClickListener(btnTakeVideoOnClick);
    }

    private View.OnClickListener btnTakeVideoOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
            else
            {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            playVideoInView(videoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast("Permission obtenu! Veuillez réessayer.");
                } else {
                    toast("Vous devez permettre l'accès à vos fichiers. Veuillez réessayer.");
                }
                return;
            }
        }
    }

    private void playVideoInView(Uri videoUri)
    {
        setContentView(R.layout.activity_video);
        viewVideo = findViewById(R.id.viewVideo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(viewVideo);
        viewVideo.setMediaController(mediaController);
        viewVideo.setVideoURI(videoUri);
        viewVideo.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        toast("menuitem clicked " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.btnMenuTakeVideo:
                return true;

            case R.id.btnMenuUpdateVideo:
                return true;

            case R.id.btnMenuSend:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void toast(String message)
    {
        toast(message, Toast.LENGTH_SHORT);
    }

    private void toast(String message, int length)
    {
        Toast.makeText(this, message, length).show();
    }
}
