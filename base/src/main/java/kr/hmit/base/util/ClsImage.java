package kr.hmit.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClsImage {
    public static void setUserPhoto(Context context, ImageView imgUserPhoto, String url, int nNoImage) {
        imgUserPhoto.setImageResource(nNoImage);

        try {
            int nIndex = url.lastIndexOf("/");
            String strFileName = "";

            if (nIndex == -1) {
                imgUserPhoto.setImageResource(nNoImage);
            } else {
                strFileName = url.substring(nIndex + 1, url.length());
                setImage(context, "user_profile", strFileName, url, imgUserPhoto, nNoImage);
            }
        } catch (Exception e) {
            imgUserPhoto.setImageResource(nNoImage);
            e.printStackTrace();
        }
    }

    public static void setImage(final Context context, String folderPath, String fileName, String url,
                                final ImageView imgView, int placeHolder) {
        try {
            final String strSavePath = context.getFilesDir() + "/" + folderPath;

            File dir = new File(strSavePath);
            if (!dir.exists())
                dir.mkdir();

            final String strFilePath = strSavePath + "/" + fileName;

            File file = new File(strFilePath);

            if (file.exists()) {
                // Debug
                // file.delete();
            }

            if (file.exists()) {
                // 사진 파일을 받은 경우
                try {
                    SimpleTarget target = new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imgView.setVisibility(View.VISIBLE);

                            imgView.setImageBitmap(bitmap);
                        }
                    };

                    Glide.with(context).load(file).asBitmap().override(2000, 2000).into(target);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 사진 파일을 받아야 하는 경우
                try {
                    SimpleTarget target = new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imgView.setImageBitmap(bitmap);

                            makeImageFile(context, bitmap, strFilePath);
                        }
                    };

                    Glide.with(context).load(url).asBitmap().placeholder(placeHolder).into(target);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean makeImageFile(Context context, Bitmap bitmap, String path) {
        boolean bResult = true;
        if (bitmap == null)
            return false;

        // 폴더 확인
        File file = new File(path);

        FileOutputStream fileStream = null;

        try {
            fileStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream);
        } catch (FileNotFoundException e) {
            bResult = false;
            e.printStackTrace();
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bResult;
    }

    /**
     * Base64로 이동
     *
     * @param photo
     * @return
     */
    public static String getBase64ImageString(Bitmap photo) {
        String imgString;
        if (photo != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] profileImage = outputStream.toByteArray();

            imgString = Base64.encodeToString(profileImage, Base64.NO_WRAP);
        } else {
            imgString = "";
        }

        return imgString;
    }
}
