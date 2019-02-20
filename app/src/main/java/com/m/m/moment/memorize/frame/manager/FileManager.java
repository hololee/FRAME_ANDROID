package com.m.m.moment.memorize.frame.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.Toast;

import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.activity.SettingActivity;
import com.m.m.moment.memorize.frame.dialog.LoadingDialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class FileManager {

    Context mContext;

    //loading
    LoadingDialog loadingDialog;

    public FileManager(Context context) {
        mContext = context;
    }

    public Bitmap getBitmapFromUri(Uri uri) {

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    //공지 이미지 가져오기.
    public Uri getNoticeUri() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/MM/images/etc/notice.jpg");

        if (file.exists()) {
            return Uri.fromFile(file);
        } else {
            return null;
        }
    }

    //공지 이미지 저장.
    public void saveNoticeBitmap(Bitmap bitmap) {
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/MM/images/etc";
        File file = new File(filePath);

        if (!file.exists())
            file.mkdirs();

        File noticeFile = new File(filePath + "/notice.jpg");
        OutputStream out = null;

        try {
            noticeFile.createNewFile();
            out = new FileOutputStream(noticeFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Uri copyImageToMM(Uri uri, String filename) throws Exception {

        Bitmap bitmap = getBitmapFromUri(uri);
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/MM/images";
        File file = new File(filePath);

        //폴더 없을때.
        if (!file.exists()) {
            file.mkdirs();
            //안보이게 설정.
            File createNomedia = new File(filePath + "/.nomedia");
            createNomedia.createNewFile();
        }

        //이미지.
        File outputImg = new File(filePath + "/" + filename);
        outputImg.createNewFile();

        OutputStream out = new FileOutputStream(outputImg);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.close();

        return Uri.fromFile(outputImg);
    }

    public Uri copyCoverToMM(Uri uri, String filename) throws Exception {

        Bitmap bitmap = getBitmapFromUri(uri);
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/MM/images/etc";
        File file = new File(filePath);

        //폴더 없을때.
        if (!file.exists()) {
            file.mkdirs();
            //안보이게 설정.
            File createNomedia = new File(filePath + "/.nomedia");
            createNomedia.createNewFile();
        }

        //이미지.
        File outputImg = new File(filePath + "/" + filename);
        outputImg.createNewFile();

        OutputStream out = new FileOutputStream(outputImg);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.close();

        return Uri.fromFile(outputImg);

    }


    public void deleteCover() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/MM/images/etc/main_cover.jpg");
        if (file.exists())
            file.delete();
    }

    public void deleteImage(String uri) {

        if (!uri.equals("null")) {//이미지가 있다면.
            File file = new File(Uri.parse(uri).getPath());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void backupAllData(Activity activity) {
        //데이터베이스와 이미지 파일들을 별도로 백업해야함.


        //MM/images/backup save database.
        File sd = new File(Environment.getExternalStorageDirectory().getPath() + "/MM/backup");
        File data = Environment.getDataDirectory();
        //폴더 없을때.
        if (!sd.exists()) {
            sd.mkdirs();
        }

        try {
            //데이터베이스 이동.
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + mContext.getPackageName() + "//databases//" + "Database";
                String backupDBPath = "Database";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //zip MM folder.
        try {

            //find MM backup data.
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/MM_backup.zip");
            if (file.exists()) {

                //show already exists.
                AlertDialog.Builder existsBuilder = new AlertDialog.Builder(mContext);
                existsBuilder.setMessage(R.string.already_backup);
                existsBuilder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //압축.
                        backUp(activity);

                    }
                });
                existsBuilder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                existsBuilder.show();

            } else {
                //압축.
                backUp(activity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //restore data.
    public void restoreAllData(Activity activity) {

        AlertDialog.Builder restoreBuilder = new AlertDialog.Builder(mContext);
        restoreBuilder.setMessage(R.string.confirm_restore);
        restoreBuilder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //show loading dialog.
                loadingDialog = new LoadingDialog(mContext);
                loadingDialog.show();

                //backup data.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();


                        //find MM backup data.
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/MM_backup.zip");
                        if (file.exists()) {
                            //remove previous database.

                            //remove previous folder.
                            remove(Environment.getExternalStorageDirectory().getPath() + "/MM");

                            //then unzip file.
                            try {
                                ZipFile zipfile = new ZipFile(Environment.getExternalStorageDirectory().getPath() + "/MM_backup.zip");
                                zipfile.extractAll(Environment.getExternalStorageDirectory().getPath());


                                //restore database.
                                try {
                                    File sd = new File(Environment.getExternalStorageDirectory().getPath() + "/MM/backup");
                                    File data = Environment.getDataDirectory();
                                    if (sd.canWrite()) {
                                        String currentDBPath = "//data//" + mContext.getPackageName() + "//databases//" + "Database";
                                        String backupDBPath = "Database"; // From SD directory.
                                        File currentDB = new File(data, currentDBPath);
                                        File backupDB = new File(sd, backupDBPath);

                                        FileChannel src = new FileInputStream(backupDB).getChannel();
                                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                                        dst.transferFrom(src, 0, src.size());
                                        src.close();
                                        dst.close();

                                        //close loading
                                        loadingDialog.dismiss();


                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, mContext.getString(R.string.alert_restore_success), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch (ZipException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.no_backup_data), Toast.LENGTH_SHORT).show();
                        }


                    }
                }).start();


            }
        });
        restoreBuilder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        restoreBuilder.show();

    }

    //backup Data
    private void backUp(Activity activity) {
        AlertDialog.Builder backupbuilder = new AlertDialog.Builder(mContext);
        backupbuilder.setMessage(R.string.confirm_backup);
        backupbuilder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                //show loading dialog.
                loadingDialog = new LoadingDialog(mContext);
                loadingDialog.show();

                //backup data.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        //zip backup.
                        ZipFile zipfile = null;
                        try {
                            zipfile = new ZipFile(Environment.getExternalStorageDirectory().getPath() + "/MM_backup.zip");
                        } catch (ZipException e) {
                            e.printStackTrace();
                        }
                        ZipParameters parameters = new ZipParameters();
                        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                        try {
                            zipfile.addFolder(Environment.getExternalStorageDirectory().getPath() + "/MM", parameters);
                        } catch (ZipException e) {
                            e.printStackTrace();
                        }

                        //close loading
                        loadingDialog.dismiss();


                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getString(R.string.alert_backup_success), Toast.LENGTH_SHORT).show();
                            }
                        });

                        //select backup intent. to any app.
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/MM_backup.zip"));
                        intent.setType("application/*");
                        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.select_backup_storage)));

                    }
                }).start();

            }
        });
        backupbuilder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        backupbuilder.show();

    }


    //zip imageData.
    public void zipData(long orderId) {


        //zip backup.
        ZipFile zipfile = null;
        try {
            //zipfile = new ZipFile(Environment.getExternalStorageDirectory().getPath() + "/cards_data.dat");
            zipfile = new ZipFile(mContext.getFilesDir() + "/"+orderId+".zip");
        } catch (ZipException e) {
            e.printStackTrace();
        }
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        try {
            //압축
            // zipfile.addFolder(Environment.getExternalStorageDirectory().getPath() + "/MM/upload", parameters);
            zipfile.addFolder(mContext.getFilesDir() + "/MM/upload", parameters);

        } catch (ZipException e) {
            e.printStackTrace();
        }

    }

    // 압축 이미지 반환.
    public boolean isZipData(long orderId) {
        //  File file = new File(Environment.getExternalStorageDirectory().getPath() + "/cards_data.dat");
        File file = new File(mContext.getFilesDir() + "/"+orderId+".zip");
        return file.exists();
    }

    // 압축 이미지 삭제.
    public void removeZipData(long orderId) {
        //  File file = new File(Environment.getExternalStorageDirectory().getPath() + "/cards_data.dat");
        File file = new File(mContext.getFilesDir() + "/"+orderId+".zip");
        file.delete();
    }


    //remove files.
    public void remove(String path) {
        File dir = new File(path);
        File[] childFileList = dir.listFiles();
        if (dir.exists() && childFileList != null) {
            for (File childFile : childFileList) {
                if (childFile.isDirectory()) {
                    //Log.d("path", childFile.getAbsolutePath());
                    remove(childFile.getAbsolutePath());
                } else {
                    childFile.delete();
                }
            }
            dir.delete();
        }

    }

    //카드정보 txt 로 저장.
    //텍스트내용을 경로의 텍스트 파일에 쓰기
    public String CreateDataPath(String data) {

        String foldername = mContext.getFilesDir() + "/MM/upload";
        String filename = "data.txt";

        try {
            File dir = new File(foldername);
            //디렉토리 폴더가 없으면 생성함
            if (!dir.exists()) {
                dir.mkdir();
            }
            //data file 이 있으면 삭제.
            File existsfile = new File(filename + "/" + filename);
            if (existsfile.exists()) {
                existsfile.delete();
            }

            //파일 output stream 생성
            FileOutputStream fos = new FileOutputStream(foldername + "/" + filename, true);
            //파일쓰기
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(data);
            writer.flush();

            writer.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return foldername + "/" + filename;
    }
}

