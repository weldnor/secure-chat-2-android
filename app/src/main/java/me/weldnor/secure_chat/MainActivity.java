package me.weldnor.secure_chat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import me.weldnor.secure_chat.utils.EasyAES;

public class MainActivity extends AppCompatActivity {
    private static final String SERVER_HOST = "http://10.0.2.2:80";
    public static final String SECRET_KEY = "SUPER_SECRET_KEY";

    private TextView oututTextView;
    private EditText inputTextEdit;
    @SuppressWarnings("FieldCanBeLocal")
    private Button sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oututTextView = findViewById(R.id.labOut);
        inputTextEdit = findViewById(R.id.tbData);
        sendButton = findViewById(R.id.butSend);

        sendButton.setOnClickListener(v -> {
            String message = inputTextEdit.getText().toString();
            EasyAES easyAES = new EasyAES(SECRET_KEY);
            String encryptedMessage = easyAES.encrypt(message);


            Thread thread = new Thread(() -> {
                HttpURLConnection conn = null;
                try {
                    conn = openConnection(encryptedMessage);
                    processConnection(conn);

                } catch (IOException e) {
                    runOnUiThread(() -> oututTextView.setText(e.getMessage()));
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            });

            tryStartThread(thread);
        });
    }

    private void tryStartThread(Thread thread) {
        try {
            thread.start();
        } catch (Exception e) {
            runOnUiThread(() -> oututTextView.setText(e.getMessage()));
        }
    }

    private HttpURLConnection openConnection(String message) throws IOException {
        URL url = new URL(SERVER_HOST + "?data=" + message);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setDoInput(true);
        conn.connect();
        return conn;
    }

    private void processConnection(HttpURLConnection conn) throws IOException {
        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();

        String bfr_st;
        while ((bfr_st = br.readLine()) != null) {
            sb.append(bfr_st);
        }

        runOnUiThread(() -> oututTextView.setText(sb.toString()));

        is.close(); // закроем поток
        br.close(); // закроем буфер
    }
}