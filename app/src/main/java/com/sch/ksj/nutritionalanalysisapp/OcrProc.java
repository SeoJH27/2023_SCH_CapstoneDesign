package com.sch.ksj.nutritionalanalysisapp;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Base64;


public class OcrProc {

    static String ocrApiUrl = "https://wuruk0p342.apigw.ntruss.com/custom/v1/25116/1b481823b785af66fad5bb3187dcf8904363afa68fdcde0339e1089e1db637ee/general";
    static String ocrSecretKey = "bU9yZm5uUGx6V2dKcVRDTkttUlRlWFZiUWtXaEN0VXQ=";

    private static String getURLForResource(int resId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resId).toString();
    }

    private static Context context;

    public static String main() {
        String ocrMessage = "";

        try {
            URL url = new URL(ocrApiUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;UTF-8");
            con.setRequestProperty("X-OCR-SECRET", ocrSecretKey);
            con.setRequestProperty("Accept","application/json");
            con.setDoOutput(true);

            String base64Img = imageToBase64("image");

            String jsonData = "{\n"+
                    "\"version\": \"V2\",\n"+
                    "\"requestId\": \"user\",\n" +
                    "\"timestamp\": 0,\n"+
                    "\"images\": [{ \"format\": \"jpg\", \"data\": \"" + base64Img +
                    "\", \"name\": \"aaa\"}]\n"+
                    "}";

            System.out.println(jsonData);

            BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            bufWriter.write(jsonData);
            bufWriter.flush();
            bufWriter.close();

            int responseCode = con.getResponseCode();
            Log.e("CODE", responseCode+"");
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            Log.e("response", response.toString());
            ocrMessage = response.toString();

        } catch (Exception e) {
            Log.e("error-OcrProc", e.toString());
        }
        return ocrMessage;
    }

    public static String imageToBase64(String imgName){
        String base64Img = "";

        File f = new File("/data/user/0/com.sch.ksj.nutritionalanalysisapp/files", imgName+".jpg");
        Log.d("파일", f.toString());

        if (f.exists() && f.isFile() && f.length() > 0) {
            byte[] bt = new byte[(int) f.length()];
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                fis.read(bt);
                base64Img = new String(Base64.getEncoder().encode(bt));
                Log.v("base64Img : bt", bt.toString());
            } catch (Exception e) {
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    Log.e("Error!","IOException : " + e.getMessage());
                } catch (Exception e) {
                    Log.e("Error!","Exception : " + e.getMessage());
                }
            }
        }

        Log.v("base64Img",base64Img);

        return base64Img;
    }
}
