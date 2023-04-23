package me.weldnor.secure_chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // url to get all products list
    private static final String server_name = "http://10.0.2.2:80";

    /// В настройках WIFI сети AndroidWiFi установить локальный ip хоста в проксю

    // Обработчик запуска приложухи (создания формы)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем ссылки на визуальные компоненты
        TextView labOut = findViewById(R.id.labOut);
        EditText tbData = findViewById(R.id.tbData);
        Button butSend = findViewById(R.id.butSend);

        // Устанавливаем обработчик нажатия кнопки
        // Обработчик нажатия кнопки
        butSend.setOnClickListener(v -> {
            // Читаем введенные данные
            String data_string = tbData.getText().toString();

            // Создаем поток, в котором будет осуществляться обмен данными
            // Обработчик, выполняемый в потоке
            Thread thread = new Thread(() -> {
                // Открываем соединение и отправляем запрос серверу
                HttpURLConnection conn = null;

                try {
                    URL url = new URL(server_name + "?data=" + data_string);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                    conn.setDoInput(true);
                    conn.connect();
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        labOut.setText("ОШИБКА: " + e.getMessage());
                    });
                }

                // Получаем ответ сервера и закрываем соединение
                try {
                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String bfr_st;
                    while ((bfr_st = br.readLine()) != null) {
                        sb.append(bfr_st);
                    }
                    runOnUiThread(() -> {
                        labOut.setText(sb.toString());
                    });

                    is.close(); // закроем поток
                    br.close(); // закроем буфер

                } catch (Exception e) {
                    runOnUiThread(() -> {
                        labOut.setText("ОШИБКА: " + e.getMessage());
                    });
                } finally {
                    conn.disconnect();
                }
            });

            try {
                thread.start();
            } catch (Exception e) {
                runOnUiThread(() -> {
                    labOut.setText("Запуск потока ОШИБКА: " + e.getMessage());
                });
            }
        });
    }
}