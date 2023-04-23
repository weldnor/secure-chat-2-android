package me.weldnor.secure_chat.ui.login;

import android.content.Context;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.FileNotFoundException;

import me.weldnor.secure_chat.R;
import me.weldnor.secure_chat.data.LoginRepository;
import me.weldnor.secure_chat.data.Result;
import me.weldnor.secure_chat.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, Context context) throws FileNotFoundException {
        Result result = loginRepository.login(username, password, context);
        // В случае успешного входа
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            // Получаем дополнительные данные пользователя (например, число)
            Integer customData = 1;
            // Формируем данные в структуру, для передачи в главное окно
            LoggedInUserView userData = new LoggedInUserView(data.getDisplayName(), customData);
            loginResult.setValue(new LoginResult(userData));
        } else if (result instanceof Result.Error) {
            // В случае неудачного входа передаем сообщение об ошибке
            loginResult.setValue(new LoginResult(((Result.Error) result).getError()));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}