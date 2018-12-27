package business.sacdefromage.parakeetvideos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class Send extends AppCompatActivity {
    ImageButton imgBtnSendMessenger;
    ImageButton imgBtnSendEmail;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_video);

        Intent intent = getIntent();
        String strVideoUri = intent.getStringExtra("videoUri");
        if (strVideoUri != null)
        {
            videoUri = Uri.parse(strVideoUri);
        }

        imgBtnSendMessenger = findViewById(R.id.imgBtn_sender_messenger);
        imgBtnSendMessenger.setOnClickListener(btnSendMessengerOnClick);

        imgBtnSendEmail = findViewById(R.id.imgBtn_sender_mail);
        imgBtnSendEmail.setOnClickListener(btnSendEmailOnClick);
    }

    private View.OnClickListener btnSendMessengerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoUri != null)
                Toast.makeText(Send.this, "Envoie Messenger - " + videoUri.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(Send.this, "Envoie Messenger - N'est pas encore développé", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener btnSendEmailOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoUri != null)
            {
                Toast.makeText(Send.this, "Envoie Email - " + videoUri.toString(), Toast.LENGTH_SHORT).show();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("video/mp4");
                emailIntent.setData(Uri.parse("mailto:" + ""));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sujet");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Corps du texte");

                emailIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
                try {
                    startActivity(Intent.createChooser(emailIntent, "Envoie l'email en utilisant..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Send.this, "Il n'y a pas de client d'envoie email d'installé sur cet appareil.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(Send.this, "Envoie de l'Email échoué - Aucune vidéo n'est sélectionnée", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
