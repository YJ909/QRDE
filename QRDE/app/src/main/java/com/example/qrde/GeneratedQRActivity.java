package com.example.qrde;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratedQRActivity extends AppCompatActivity {
    public final static int QRcodeWidth = 500;
    private static final String IMAGE_DIRECTORY = "/QRcode";
    Bitmap bitmap;
    File pictureFile;
    ImageView qrImage;
    Button buttonShare;
    Button buttonHome;
    String path;
    String userID;
    String baseURL;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_qr);
        addListenerOnButton();
        Bundle bundle = getIntent().getExtras();

        message = bundle.getString("message");
        userID = bundle.getString("userID");
        baseURL = bundle.getString("baseURL");

        qrImage = (ImageView) findViewById(R.id.qr);
        if(setQRCode(message) == false){
                Toast.makeText(GeneratedQRActivity.this, "Error", Toast.LENGTH_SHORT).show();}
    }

    public void addListenerOnButton() {
        final Context context = this;
        buttonShare = (Button) findViewById(R.id.btnShare);
        buttonHome = (Button) findViewById(R.id.btnHome);

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                shareImage(path);
            }
        });
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                startActivity(intent);
            }
        });
    }

    private void shareImage(String imagePath) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        File imageFileToShare = new File(imagePath);
        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image!"));
    }

    private boolean setQRCode(String message){
        try {
            bitmap = TextToImageEncode(message);
            qrImage.setImageBitmap(bitmap);
            path = saveImage(bitmap);  //give read write permission
            if(path != "Null"){
                Toast.makeText(GeneratedQRActivity.this, "QRCode saved to -> " + path, Toast.LENGTH_SHORT).show();}
        } catch (WriterException e) {
            e.printStackTrace();
            return false;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return  true;
    }

    public String saveImage(Bitmap myBitmap)
    {
        pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("TAG", "Error creating media file, check storage permissions: ");// e.getMessage());
            Toast.makeText(GeneratedQRActivity.this, "Error creating media file, check storage permissions: " , Toast.LENGTH_SHORT).show();
            return "Null";
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Toast.makeText(GeneratedQRActivity.this, "File saved" , Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
            Toast.makeText(GeneratedQRActivity.this, "File not found: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
            Toast.makeText(GeneratedQRActivity.this, "Error accessing file: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            throw e;
        }
        return pictureFile.getAbsolutePath().toString();
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        Toast.makeText(GeneratedQRActivity.this, "Path : " + mediaStorageDir , Toast.LENGTH_SHORT).show();
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            Toast.makeText(GeneratedQRActivity.this, "Path does not exist" , Toast.LENGTH_SHORT).show();
            if (! mediaStorageDir.mkdirs()){
                Toast.makeText(GeneratedQRActivity.this, "Error!" , Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MyQR_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        Toast.makeText(GeneratedQRActivity.this, "Name : " + mediaFile , Toast.LENGTH_SHORT).show();
        return mediaFile;
    }
}
