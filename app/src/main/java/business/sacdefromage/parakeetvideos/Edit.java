package business.sacdefromage.parakeetvideos;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

public class Edit extends AppCompatActivity {
    Uri videoUri = null;
    int editCounts = 0;
    Button btnSendVideo;
    Button btnFilterRewind;
    FFmpeg ffmpeg = null;

    //region onCreate
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

        ffmpeg = FFmpeg.getInstance(this);
    }
    //endregion

    //region Filters
    private View.OnClickListener btnFilterRewindOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoUri == null)
            {
                Toast.makeText(Edit.this, "Vidéo originale perdue, veuillez ré-essayer.", Toast.LENGTH_LONG).show();
                return;
            }

            editCounts++;
            String currentUri = getRealPathFromURI(videoUri);
            Log.d("Edit Video - Current", currentUri);
            final String outputVideoUri = GetEditedPath(currentUri, editCounts);
            Log.d("Edit Video - Edited", outputVideoUri);

            // Valid command: ffmpeg -i C:\FFmpeg\input.mp4 -filter:v "reverse" C:\FFmpeg\output.mp4
            // Semi-Valid command #2, mais audio mauvais dans 2e partie: -i input.mp4 -filter_complex "[0:v]reverse,fifo[r];[0:v][0:a][r] [0:a]concat=n=2:v=1:a=1 [v] [a]" -map "[v]" -map "[a]" output.mp4

           String[] commands = new String[]
            {
                "-i",
                currentUri,
                "-filter_complex",
                "[v]reverse[r];[v][r]concat",
                outputVideoUri
            };

            StringBuilder builder = new StringBuilder();
            for (String s : commands) {
                builder.append(s + " ");
            }
            String str = builder.toString();
            Log.d("Edit Video - Commands", str);
            if (ffmpeg.isSupported()) {
                ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
                    @Override
                    public void onStart() {
                        Log.d("Edit Video START", "");
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(Edit.this, "La conversion vidéo n'a pas fonctionnée... Ré-essayez.", Toast.LENGTH_LONG).show();
                        Log.d("Edit Video FAILURE", message);
                    }

                    @Override
                    public void onProgress(String message) {
                        Log.d("Edit Video PROGRESS", message);
                    }


                    @Override
                    public void onSuccess(String message) {
                        videoUri = Uri.parse(outputVideoUri);
                        //playVideoInView(videoUri);
                        Toast.makeText(Edit.this, "Edit vidéo done", Toast.LENGTH_SHORT);
                        Log.d("Edit Video SUCCESS", message);
                    }
                    @Override
                    public void onFinish() {
                        Log.d("Edit Video FINISH", "");
                    }
                });
            }
            else {
                Log.d("Edit Video", "L'éditeur de vidéos FFmpeg n'est pas disponible sur cet appareil.");
                Toast.makeText(Edit.this, "L'éditeur de vidéos FFmpeg n'est pas disponible sur cet appareil.", Toast.LENGTH_LONG).show();
                return;
            }
        }
    };
    //endregion

    //region Send Video
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
    //endregion

    //region Open viewer
    private void playVideoInView(Uri videoUri)
    {
        setContentView(R.layout.activity_video_editor);
        VideoView viewVideo = findViewById(R.id.viewVideo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(viewVideo);
        viewVideo.setMediaController(mediaController);
        viewVideo.setVideoURI(videoUri);
        viewVideo.start();
    }
    //endregion

    //region Private helpers
    private String GetEditedPath(String currentUrl, int editCount)
    {
        String[] currentUrlParts = currentUrl.split("\\.");
        String uriWithoutExtension = currentUrlParts[0];
        String extension = currentUrlParts[1];
        if (editCount > 1)
        {
            uriWithoutExtension = uriWithoutExtension.split("_EDIT_")[0];
        }

        uriWithoutExtension = uriWithoutExtension.replace("/storage/emulated/0/DCIM/Camera/",
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/ParakeetVideos/");

        return uriWithoutExtension + "_EDIT_" + editCount + "." + extension;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(Edit.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    //endregion
}
