package cn.adminzero.passwordshield_demo0.util;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Descriptions
 *
 * @version 2014-6-5
 * @author luguo3000
 * @since JDK1.6
 *
 */
public class IconFinder {
    public static Bitmap lastFetchBitmap = null;
    public static Bitmap getBitmap(String path) throws IOException {
        try {
            lastFetchBitmap = null;
            URL url = new URL("https://"+path + "/favicon.ico");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                lastFetchBitmap = bitmap;
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

