package business.sacdefromage.parakeetvideos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Edit extends AppCompatActivity {
    Uri videoUri = null;
    Button btnSendVideo;
    Button btnFilterRewind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_filters);

        btnSendVideo = findViewById(R.id.btn_send_video_edit);
        btnSendVideo.setOnClickListener(btnSendVideoOnClick);

        btnFilterRewind = findViewById(R.id.btn_filter_rewind);
        btnFilterRewind.setOnClickListener(btnFilterRewindOnClick);


        Intent intent = getIntent();
        String strVideoUri = intent.getStringExtra("videoUri");
        if (strVideoUri != null)
        {
            videoUri = Uri.parse(strVideoUri);
        }
    }

    private View.OnClickListener btnFilterRewindOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(Edit.this, "Filtre Rembobiner - N'est pas encore développé", Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener btnSendVideoOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent sendIntent = new Intent(Edit.this, Send.class);
            if (videoUri != null)
            {
                sendIntent.putExtra("videoUri", videoUri.toString());
            }
            startActivity(sendIntent);
        }
    };
}
