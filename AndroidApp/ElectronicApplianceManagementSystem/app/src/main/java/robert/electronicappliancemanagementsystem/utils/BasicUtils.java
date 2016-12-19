package robert.electronicappliancemanagementsystem.utils;


import android.content.Context;
import android.widget.Toast;

public class BasicUtils {
    public static void showToast(String message, Context ctx) {
        Toast.makeText(
                ctx,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }
}
