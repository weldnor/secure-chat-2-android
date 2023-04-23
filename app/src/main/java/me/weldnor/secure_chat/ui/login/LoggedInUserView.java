package me.weldnor.secure_chat.ui.login;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView implements Serializable {
    private static final long serialVersionUID = 1L;

    private String displayName;
    //... other data fields that may be accessible to the UI
    private Integer displayData;

    LoggedInUserView(String displayName, Integer displayData) {
        this.displayName = displayName;
        this.displayData = displayData;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getDisplayData() {
        return displayData;
    }

    private void writeObject(@NonNull ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(displayName);
        oos.writeObject(displayData);
    }

    private void readObject(@NonNull ObjectInputStream ois)
            throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        displayName = (String) ois.readObject();
        displayData = (Integer) ois.readObject();
    }
}