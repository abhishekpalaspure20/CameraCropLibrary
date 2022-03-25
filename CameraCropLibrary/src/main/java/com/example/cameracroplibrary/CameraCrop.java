package com.example.cameracroplibrary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CameraCrop {
        int rotatedWidth,  rotatedHeight ,  notRoatedWidth,  notRotatedHeight;
        File file;
        File root,rootThumbnail,root1;
        String dir,imageFilePath,dir_thumbnail,thumbnailFilePath;
        private Uri picUri;
        static final int CAMERA_CAPTURE = 1;
        final int PIC_CROP = 3;
        final int PICK_IMAGE_REQUEST = 2;
        Bitmap photoBm,rotatedBitmap;

        String id_user;
        Activity activity;

    public    CameraCrop(Activity activity,String id)
        {
                this.activity = activity;
                this.id_user = id;
        }


        public void createDirectory(String foldername,  String thumbnailFolderName)
        {
              //  root = new File(Environment.getExternalStorageDirectory() + "/StudentIdCard");
                root = new File(Environment.getExternalStorageDirectory() + "/" + foldername);
                boolean isPresent = true;
                if (!root.exists()) {
                        isPresent = root.mkdir();
                } else {
                        if (!root.exists()) {
                                isPresent = root.mkdir();
                        }
                }
                dir = root.getPath();

             //   rootThumbnail =  new File(Environment.getExternalStorageDirectory() + "/StudentIdCard/thumbnail");
                rootThumbnail =  new File(Environment.getExternalStorageDirectory() + "/"+foldername+"/"+thumbnailFolderName);
                if (!rootThumbnail.exists()) {
                        rootThumbnail.mkdir();
                }
                dir_thumbnail =rootThumbnail.getPath();



        }

    public void captureImage(int rotatedWidth, int rotatedHeight , int notRoatedWidth, int notRotatedHeight){

//            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//            Camera camera = Camera.open();
//            camera.cancelAutoFocus();

            this.rotatedWidth = rotatedWidth;
            this.rotatedHeight = rotatedHeight;
            this.notRoatedWidth = notRoatedWidth;
            this.notRotatedHeight = notRotatedHeight;


            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // imageFilePath = dir + "/images/" + userid + ".jpg";
            // imageFilePath = dir + "/images.jpg";
            imageFilePath = dir + "/" + id_user + ".jpg";

            File imageFile = new File(imageFilePath);
            picUri = Uri.fromFile(imageFile); // convert path to Uri
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            this.activity.startActivityForResult(takePictureIntent, CAMERA_CAPTURE);

    }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                try {

                        if (resultCode == activity.RESULT_OK) {
                                //user is returning from capturing an image using the camera
                                //     Toast.makeText(this, "Activity result="+resultCode+"request="+requestCode+"camera"+CAMERA_CAPTURE, Toast.LENGTH_SHORT).show();
                                if (requestCode == CAMERA_CAPTURE) {
                                        Log.i("RRRR","TTT 0.1");

                                        Uri uri = picUri;
                                        Log.i("RRRR","TTT 0.2");
                                        performCrop( rotatedWidth,  rotatedHeight ,  notRoatedWidth,  notRotatedHeight);
                                        // Log.d("picUri", "PIC URI" + uri.toString());

                                } else if (requestCode == PICK_IMAGE_REQUEST) {
                                        picUri = data.getData();

                                        performCrop(rotatedWidth,  rotatedHeight ,  notRoatedWidth,  notRotatedHeight);
                                }

                                //user is returning from cropping the image
                                else if (requestCode == PIC_CROP) {

                                        //get the returned data
                                        Bundle extras = data.getExtras();
                                        //get the cropped bitmap
                                        // Toast.makeText(this, "trying to set image" + picUri, Toast.LENGTH_LONG).show();
                                        // Bitmap bitmap = (Bitmap) extras.get("data");
                                        //commented below single line
                                    //    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                                        //    bitmap = crupAndScale(bitmap, 300);

                                        //display the returned cropped image
                                        //commented below 2 lines
//                                        profile_pic=(CircleImageView)findViewById(R.id.profilePic);
//                                        profile_pic.setImageBitmap(bitmap);


                                }else{
                                        //     Toast.makeText(this, "elseeee ", Toast.LENGTH_SHORT).show();
                                }

                        }
                } catch (Exception e) {
                        //    Toast.makeText(this, "error="+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private Photo performCrop(int rotatedWidth, int rotatedHeight , int notRoatedWidth, int notRotatedHeight) {

                try {

                        try {

                                //  Toast.makeText(this, "PErforming crop", Toast.LENGTH_SHORT).show();
                                Log.i("RRRR","TTT 1");
                                // Cursor img_count = db.rawQuery(" SELECT * FROM crop_pest_survey_images where cisd_srno=" + img_cisd, null);
                                String userid="dd";
                                // imageFilePath = dir + "/images/" + userid + ".jpg";
                                //   imageFilePath = dir + "/images.jpg";
                                imageFilePath = dir + "/"+id_user+".jpg";
                                thumbnailFilePath = dir_thumbnail + "/"+id_user+".jpg";
                                Log.i("IMG PATH", "IMG PATHJ=" + imageFilePath);
                                File imageFile = new File(imageFilePath);
                                picUri = Uri.fromFile(imageFile);
                                Log.i("RRRR","TTT 2");
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), picUri);

                                photoBm = (Bitmap) bitmap;


                                Log.i("RRRR","TTT 4");
                                /**********THE REST OF THIS IS FROM Prabu's answer*******/
                                //create a byte array output stream to hold the photo's bytes
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                ByteArrayOutputStream bytest = new ByteArrayOutputStream();

                                if (photoBm.getWidth() > photoBm.getHeight()) {
                                        //       Toast.makeText(this, "Photo rotated 90 degree", Toast.LENGTH_SHORT).show();
                                        Matrix matrix = new Matrix();
                                        matrix.postRotate(90);
                                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(photoBm, rotatedWidth, rotatedHeight, true);
                                     //   Bitmap scaledBitmap = Bitmap.createScaledBitmap(photoBm, 450, 600, true);
                                        rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                                        Matrix matrix2 = new Matrix();
                                        matrix2.postRotate(90);
                                        Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(photoBm, photoBm.getWidth(), photoBm.getHeight(), true);
                                        photoBm = Bitmap.createBitmap(scaledBitmap2, 0, 0, scaledBitmap2.getWidth(), scaledBitmap2.getHeight(), matrix2, true);
                                }else{
                                        //    Toast.makeText(this, "Photo not rotated 90 degree", Toast.LENGTH_SHORT).show();
                                        //    rotatedBitmap = photoBm;

                                        Matrix matrix = new Matrix();
                                        matrix.postRotate(0);
                                       // Bitmap scaledBitmap = Bitmap.createScaledBitmap(photoBm, 600, 450, true);
                                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(photoBm, notRoatedWidth, notRotatedHeight, true);
                                        rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                                        Matrix matrix2 = new Matrix();
                                        matrix2.postRotate(0);
                                        Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(photoBm, photoBm.getWidth(), photoBm.getHeight(), true);
                                        photoBm = Bitmap.createBitmap(scaledBitmap2, 0, 0, scaledBitmap2.getWidth(), scaledBitmap2.getHeight(), matrix2, true);
                                }


                                //compress the photo's bytes into the byte array output streamwd
                                photoBm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytest);
                                // photoBm.compress(Bitmap.CompressFormat.JPEG, 50, bytest);
                                File f = new File(imageFilePath);
                                f.createNewFile();
                                FileOutputStream fo = new FileOutputStream(f);
                                fo.write(bytes.toByteArray());
                                fo.close();


                                //for thumbnail
                                File f1 = new File(thumbnailFilePath);
                                f1.createNewFile();
                                FileOutputStream fo1 = new FileOutputStream(f1);
                                fo1.write(bytest.toByteArray());
                                fo1.close();

                                //display the returned cropped image
                                Log.i("RRRR","TTT 5");
                                //commented below 2 lines
//                                profile_pic=(CircleImageView)findViewById(R.id.profilePic);
//
//                                profile_pic.setImageBitmap(rotatedBitmap);

                                //  Toast.makeText(this, "data updated", Toast.LENGTH_SHORT).show();
                                Log.i("RRRR","TTT 6");
                                //finish by closing the FileOutputStream
//                fo.close();



                        } catch (Exception e) {
                                Toast.makeText(this.activity, "error i image="+e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                }

                catch (Exception e) {
                        //  Toast.makeText(this, "error="+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                return new Photo(imageFilePath, thumbnailFilePath , rotatedBitmap);
        }

}
