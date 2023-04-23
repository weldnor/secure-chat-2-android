package me.weldnor.secure_chat.ui.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import me.weldnor.secure_chat.R;
import me.weldnor.secure_chat.ui.echo.EchoActivity;
import me.weldnor.secure_chat.ui.login.LoggedInUserView;
import me.weldnor.secure_chat.ui.login.LoginActivity;

public class IntroActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        TextView tvLogin = findViewById(R.id.tvName);
        TextView tvData = findViewById(R.id.tvData);
        TextView tvError = findViewById(R.id.tvError);

        Button echoButton = findViewById(R.id.butEcho);
        Button logoutButton = findViewById(R.id.butLogout);

        echoButton.setOnClickListener(v -> goToActivity(EchoActivity.class));
        logoutButton.setOnClickListener(v -> logout());


        // Проверка результата входа
        if (result.getResultCode() == Activity.RESULT_OK) {
            // В случае успеха, получение данных пользователя
            Intent intent = result.getData();
            assert intent != null;

            if (intent.hasExtra("UserData")) {
                // Получение экземпляра структуры с данными
                LoggedInUserView userData = (LoggedInUserView) intent.getSerializableExtra("UserData");
                runOnUiThread(() -> tvLogin.setText(userData.getDisplayName()));
                runOnUiThread(() -> tvData.setText(userData.getDisplayData().toString()));
            }


        } else {
            // В случае неудачи - сообщение об ошибке
            Intent intent = result.getData();
            if (intent.hasExtra("Error")) {
                String errorString = intent.getStringExtra("Error");
                runOnUiThread(() -> tvError.setText(errorString));
            } else runOnUiThread(() -> tvError.setText("Ошибка доступа"));
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Вызов формы авторизации
        Intent intent = new Intent(this, LoginActivity.class);
        mStartForResult.launch(intent);
    }

    private void logout() {
        //todo
    }

    @SuppressWarnings("SameParameterValue")
    private void goToActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}