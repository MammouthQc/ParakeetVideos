package business.sacdefromage.parakeetvideos;

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
    Button btnEditVideo;
    Button btnSendVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnTakeVideo = findViewById(R.id.btn_take_video_home);
        btnTakeVideo.setOnClickListener(btnTakeVideoOnClick);

        btnEditVideo = findViewById(R.id.btn_edit_video_home);
        btnEditVideo.setOnClickListener(btnEditVideoOnClick);

        btnSendVideo = findViewById(R.id.btn_send_video_home);
        btnSendVideo.setOnClickListener(btnSendVideoOnClick);
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

    private View.OnClickListener btnEditVideoOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
            else
            {
                Intent editIntent = new Intent(Home.this, Edit.class);
                startActivity(editIntent);
            }
        }
    };

    private View.OnClickListener btnSendVideoOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent editIntent = new Intent(Home.this, Send.class);
            startActivity(editIntent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        toast(Integer.toString(resultCode));
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // User approved the video, take him to the filter

                // Uri videoUri = intent.getData();
                // playVideoInView(videoUri);
            } else {
                // User canceled, do something?
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        String permissionGranted = "Permission obtenu! Veuillez réessayer.";

        switch (requestCode) {
            case PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast(permissionGranted);
                } else {
                    toast("Vous devez permettre l'accès à vos fichiers. Veuillez réessayer.");
                }
                return;
            }
        }
    }

    private void playVideoInView(Uri videoUri)
    {
        setContentView(R.layout.activity_video_editor);
        viewVideo = findViewById(R.id.viewVideo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(viewVideo);
        viewVideo.setMediaController(mediaController);
        viewVideo.setVideoURI(videoUri);
        viewVideo.start();
    }

    private void sendOnMessenger()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Message envoyé");
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");
        try {
            startActivity(sendIntent);
        }
        catch (android.content.ActivityNotFoundException ex) {
            toast("Veuillez installer Facebook Messenger.", Toast.LENGTH_LONG);
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
