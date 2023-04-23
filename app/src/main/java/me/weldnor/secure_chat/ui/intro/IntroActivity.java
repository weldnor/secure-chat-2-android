package me.weldnor.secure_chat.ui.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import me.weldnor.secure_chat.R;
import me.weldnor.secure_chat.ui.echo.EchoActivity;
import me.weldnor.secure_chat.ui.login.LoginActivity;

public class IntroActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        Button echoButton = findViewById(R.id.butEcho);
        echoButton.setOnClickListener(v -> goToActivity(EchoActivity.class));

        Button loginButton = findViewById(R.id.butLogin);
        loginButton.setOnClickListener(v -> goToActivity(LoginActivity.class));
    }


    public void goToActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}