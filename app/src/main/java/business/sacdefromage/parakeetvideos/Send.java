package business.sacdefromage.parakeetvideos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Send extends AppCompatActivity {
    ImageButton imgBtnSendMessenger;
    ImageButton imgBtnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_video);
        imgBtnSendMessenger = findViewById(R.id.imgBtn_sender_messenger);
        imgBtnSendMessenger.setOnClickListener(btnSendMessengerOnClick);

        imgBtnSendEmail = findViewById(R.id.imgBtn_sender_mail);
        imgBtnSendEmail.setOnClickListener(btnSendEmailOnClick);
    }

    private View.OnClickListener btnSendMessengerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(Send.this, "Envoie Messenger - N'est pas encore développé", Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener btnSendEmailOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(Send.this, "Envoie Email - N'est pas encore développé", Toast.LENGTH_LONG).show();
        }
    };
}
