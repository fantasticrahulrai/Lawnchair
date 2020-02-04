/*
 *     Copyright (C) 2019 Lawnchair Team.
 *
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */

package wallunix.uhd.wallpaper.wallpapers4k;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.launcher3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.parse.ParseObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import wallunix.uhd.wallpaper.wallpapers4k.Classes.Wallpaper;
import wallunix.uhd.wallpaper.wallpapers4k.Fragments.AdminWallpaperFragment;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnChoose, btnUpload, btnUploadLockScreen, btnDelete, btnUploadNotch;
    private ImageView imageView;
    private Spinner categories;
    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;


    private final int PICK_IMAGE_REQUEST = 71;

    String urlThumbnail="failed", urlWallpaper="failed";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallunix_activity_upload_admin);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_upload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUploadLockScreen = (Button) findViewById(R.id.btnUploadLockscreen);
        btnUploadNotch = (Button) findViewById(R.id.btnUploadNotch);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        imageView = (ImageView) findViewById(R.id.imgView);
        categories = (Spinner)findViewById(R.id.spinner);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadThumbnail();
            }
        });

        btnUploadLockScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { uploadLockscreenThumbnail();
            }
        });

        btnUploadNotch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { uploadNotchThumbnail();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment mFragment = new AdminWallpaperFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content,  mFragment)
                        .commit();


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                byte[] byteArray= compressImage(bitmap);
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);//byteArray to bitmap
                imageView.setImageBitmap(compressedBitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // onBackPressed();
                finish();
                startActivity(getIntent());
                return true;

            case R.id.action_settings:
                // startSettings();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private String uploadThumbnail() {

        if(filePath != null)
        {

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading thumbnail...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference refThumb = storageReference.child("thumbnails/"+ UUID.randomUUID().toString());
            refThumb.putBytes(compressImage(bitmap))
                    .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            refThumb.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlThumbnail=uri.toString();
                                }
                            });
                            progressDialog.dismiss();
                           // Uri u=taskSnapshot.getUploadSessionUri();
                           // urlThumbnail=u.toString();
                            Toast.makeText(AdminDashboardActivity.this, "Uploaded Thumbnail", Toast.LENGTH_SHORT).show();

                            uploadWallpaper();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminDashboardActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });


        }


        return urlThumbnail;

    }


    public byte[] compressImage(Bitmap mBitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG,25,stream);

        byte[] byteArray = stream.toByteArray();

        //Uncomment to convery byteArray to bitmap
        //Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        //Toast.makeText(getApplicationContext(), "Compressed", Toast.LENGTH_SHORT).show();

        return byteArray;
        //***********************************
    }

    public String uploadWallpaper(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading wallpaper...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final StorageReference refWallpaper = storageReference.child("wallpapers/"+ UUID.randomUUID().toString());
        refWallpaper.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        refWallpaper.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlWallpaper=uri.toString();
                                addWallpaperToFirebase();
                            }
                        });
                        progressDialog.dismiss();
                      //  Uri u=taskSnapshot.getUploadSessionUri();
                       // urlWallpaper=u.toString();
                        Toast.makeText(AdminDashboardActivity.this, "Uploaded Wallunix", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminDashboardActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });


        return urlWallpaper;

    }

    public void addWallpaperToFirebase(){

        long date = -1 * new Date().getTime();


        Wallpaper mWallpaper = new Wallpaper();
        mWallpaper.setName(date+"");
        mWallpaper.setThumbnail(urlThumbnail);
        mWallpaper.setWallpaper(urlWallpaper);
        mWallpaper.setCategory(categories.getSelectedItem().toString());
        mWallpaper.setDownloads(0);

        String id = UUID.randomUUID().toString();

        ParseObject w = new ParseObject("wallpapers");
        w.put("name", mWallpaper.getName());
        w.put("thumbnail", mWallpaper.getThumbnail());
        w.put("wallpaper", mWallpaper.getWallpaper());
        w.put("downloads", mWallpaper.getDownloads());
        w.put("category", mWallpaper.getCategory());
        w.put("firebaseid", id);

      //  w.put("artistsid", "E3Qg2K5gDg");
      //  w.put("artistname", "Team Wallunix");
      //  w.put("artistdp", "https://firebasestorage.googleapis.com/v0/b/raiwallpaper.appspot.com/o/artists_dp%2FTeam%20Wallunix.png?alt=media&token=bdee2adc-d95d-4b53-a3ce-2c9defc1be67");

        w.saveInBackground();

        Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show();

        // This is for refreshing the activity
        finish();
        startActivity(getIntent());
    }

    //*************************same methods for lockscreen**************************************

    private String uploadLockscreenThumbnail() {

        if(filePath != null)
        {

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading lockscreen thumbnail...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference refThumb = storageReference.child("lockscreen_thumbnails/"+ UUID.randomUUID().toString());
            refThumb.putBytes(compressImage(bitmap))
                    .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            refThumb.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlThumbnail=uri.toString();
                                }
                            });
                            progressDialog.dismiss();
                            // Uri u=taskSnapshot.getUploadSessionUri();
                            // urlThumbnail=u.toString();
                            Toast.makeText(AdminDashboardActivity.this, "Uploaded Lockscreen Thumbnail", Toast.LENGTH_SHORT).show();

                            uploadLockscreen();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminDashboardActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });


        }


        return urlThumbnail;

    }



    public String uploadLockscreen(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading lockscreen...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final StorageReference refWallpaper = storageReference.child("lockscreens/"+ UUID.randomUUID().toString());
        refWallpaper.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        refWallpaper.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlWallpaper=uri.toString();
                                addLockScreenToFirebase();
                            }
                        });
                        progressDialog.dismiss();
                        //  Uri u=taskSnapshot.getUploadSessionUri();
                        // urlWallpaper=u.toString();
                        Toast.makeText(AdminDashboardActivity.this, "Uploaded Lockscreen", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminDashboardActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });


        return urlWallpaper;

    }

    public void addLockScreenToFirebase(){

        long date = -1 * new Date().getTime();


        Wallpaper mWallpaper = new Wallpaper();
        mWallpaper.setName(date+"");
        mWallpaper.setThumbnail(urlThumbnail);
        mWallpaper.setWallpaper(urlWallpaper);
        mWallpaper.setCategory("Lockscreen");
        mWallpaper.setDownloads(0);


        String id = UUID.randomUUID().toString();

        ParseObject w = new ParseObject("lockscreens");
        w.put("name", mWallpaper.getName());
        w.put("thumbnail", mWallpaper.getThumbnail());
        w.put("wallpaper", mWallpaper.getWallpaper());
        w.put("downloads", mWallpaper.getDownloads());
        w.put("category", mWallpaper.getCategory());
        w.put("firebaseid", id);
        w.saveInBackground();

        Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show();

        // This is for refreshing the activity
        finish();
        startActivity(getIntent());
    }

    //***************************methods for notch*************************

    private String uploadNotchThumbnail() {

        if(filePath != null)
        {

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading notch thumbnail...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference refThumb = storageReference.child("notch_thumbnails/"+ UUID.randomUUID().toString());
            refThumb.putBytes(compressImage(bitmap))
                    .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            refThumb.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlThumbnail=uri.toString();
                                }
                            });
                            progressDialog.dismiss();
                            // Uri u=taskSnapshot.getUploadSessionUri();
                            // urlThumbnail=u.toString();
                            Toast.makeText(AdminDashboardActivity.this, "Uploaded Notch Thumbnail", Toast.LENGTH_SHORT).show();

                            uploadNotch();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminDashboardActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });


        }


        return urlThumbnail;

    }



    public String uploadNotch(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading notch...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final StorageReference refWallpaper = storageReference.child("notch/"+ UUID.randomUUID().toString());
        refWallpaper.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        refWallpaper.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlWallpaper=uri.toString();
                                addNotchToFirebase();
                            }
                        });
                        progressDialog.dismiss();
                        //  Uri u=taskSnapshot.getUploadSessionUri();
                        // urlWallpaper=u.toString();
                        Toast.makeText(AdminDashboardActivity.this, "Uploaded Notch", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminDashboardActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });


        return urlWallpaper;

    }

    public void addNotchToFirebase(){

        long date = -1 * new Date().getTime();


        Wallpaper mWallpaper = new Wallpaper();
        mWallpaper.setName(date+"");
        mWallpaper.setThumbnail(urlThumbnail);
        mWallpaper.setWallpaper(urlWallpaper);
        mWallpaper.setCategory("Notch");
        mWallpaper.setDownloads(0);


        String id = UUID.randomUUID().toString();

        ParseObject w = new ParseObject("notch");
        w.put("name", mWallpaper.getName());
        w.put("thumbnail", mWallpaper.getThumbnail());
        w.put("wallpaper", mWallpaper.getWallpaper());
        w.put("downloads", mWallpaper.getDownloads());
        w.put("category", mWallpaper.getCategory());
        w.put("firebaseid", id);
        w.saveInBackground();

        Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show();

        // This is for refreshing the activity
        finish();
        startActivity(getIntent());
    }
}


