package com.example.cameracroplibrary;

import android.graphics.Bitmap;

import java.util.SplittableRandom;

public class Photo {

  private   String imageFilePath;
  private   String thumbnailFilePath;
  private Bitmap rotatedBitmap;

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getThumbnailFilePath() {
        return thumbnailFilePath;
    }

    public Photo(String imageFilePath, String thumbnailFilePath, Bitmap rotatedBitmap) {
        this.imageFilePath = imageFilePath;
        this.thumbnailFilePath = thumbnailFilePath;
        this.rotatedBitmap = rotatedBitmap;
    }

    public void setThumbnailFilePath(String thumbnailFilePath) {
        this.thumbnailFilePath = thumbnailFilePath;
    }

    public Bitmap getRotatedBitmap() {
        return rotatedBitmap;
    }

    public void setRotatedBitmap(Bitmap rotatedBitmap) {
        this.rotatedBitmap = rotatedBitmap;
    }
}
