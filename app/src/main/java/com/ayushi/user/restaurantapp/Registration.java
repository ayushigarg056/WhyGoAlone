package com.ayushi.user.restaurantapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayushi.user.restaurantapp.Util.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Registration extends AppCompatActivity {
    TextView reg,signin;
    EditText euser,epass,ephone,email,esex,ecity,eage;
    ProgressDialog pg;
    FirebaseAuth fauth;
    DatabaseReference db;
    StorageReference storageReference;
    SharedPreferences sp;
    Uri userPicUri = null;
    ImageView img;
    List<User> info;
    String userPicUrl = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    //OnAuthentication onAuth;
    String name, city, mail, password, contactNo;
    Integer age, sex;
    private String userChoosenTask;
    String imageString;
    FileOutputStream fo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setTitle("SignUp");
        info=new ArrayList<>();
        reg=(TextView)findViewById(R.id.register);
        email=(EditText)findViewById(R.id.email);
        epass=(EditText)findViewById(R.id.password);
        ephone=(EditText)findViewById(R.id.phone);
        esex=(EditText)findViewById(R.id.sex);
        eage=(EditText)findViewById(R.id.age);
        ecity=(EditText)findViewById(R.id.city);
        img=(ImageView)findViewById(R.id.imag1);
        euser= (EditText)findViewById(R.id.username1);
        signin=(TextView)findViewById(R.id.signin);
        fauth=FirebaseAuth.getInstance();
        pg=new ProgressDialog(this);
        db= FirebaseDatabase.getInstance().getReference("detail");
        
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });
        signin.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startloginpage();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            registeruser();
            }
        });
    }


    private void registeruser()
    {
        name = euser.getText().toString().trim();
        final String stringAge = eage.getText().toString().trim();
//                age = Integer.parseInt(stringAge);
        String stringSex = esex.getText().toString().trim();
        if (stringSex.equals("MALE") || stringSex.equals("Male") || stringSex.equals("M") || stringSex.equals("m") || stringSex.equals("male")){
            sex = 1;
        }
        else{
            sex = 2;
        }
        city = ecity.getText().toString().trim();
        contactNo = ephone.getText().toString();
        mail = email.getText().toString().trim();
        password = epass.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(stringAge)) {
            Toast.makeText(getApplicationContext(), "Enter Age!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(stringSex)) {
            Toast.makeText(getApplicationContext(), "Enter Sex!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(city)){
            Toast.makeText(getApplicationContext(), "Enter City!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(contactNo)){
            Toast.makeText(getApplicationContext(), "Enter Contact Number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(getApplicationContext(), "Enter Email Address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            epass.setError("Password too short, enter minimum 6 characters!");
            return;
        }

        age = Integer.parseInt(stringAge);

        if (age < 0 || age > 100) {
            eage.setError("Enter Correct Age!");
            return;
        }

        if (age < 18 || age > 50) {
            eage.setError("You are not Eligible!");
            return;
        }

        if (userPicUri == null){
            Toast.makeText(Registration.this, "Please Upload Profile Picture", Toast.LENGTH_SHORT).show();
            return;
        }
        pg.setMessage("Registering Please Wait...");
        pg.show();

        fauth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pg.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(Registration.this, "Authentication failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Registration.this, "Sign Up done", Toast.LENGTH_SHORT).show();
                   // uploadImage(userPicUri);

                }
            }
        });
    }

    private void userdatabase() {

    }

    private void startloginpage() {
        Intent i=new Intent(Registration.this,Login.class);
        startActivity(i);
        finish();
    }

    private void getImage()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Registration.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.ayushi.user.restaurantapp",
                        //"com.example.android.fileprovider",
                        photoFile);
                userPicUri = photoURI;
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        }
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        picture=thumbnail;
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//           thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            String path = MediaStore.Images.Media.insertImage(getContentResolver(), thumbnail, "Title", null);
//            filePath= Uri.parse(path);

        img.setImageBitmap(thumbnail);
        uploadImage(userPicUri);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
//
//        Bitmap bm=null;
//        if (data != null) {
//            try {
//                Uri image = data.getData();
//                //Uri resultUri = image.getUri();
//                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(Registration.this, "Something went wrong", Toast.LENGTH_LONG).show();
//
//            }
//        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
//
//        String path = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(),bm, "Title", null);
//        Uri u= Uri.parse(path);
//        userPicUri=u;
//        Log.d("uri", "onClick: " + u);
//        Log.d("uri1", "onClick: " + userPicUri);
//
//        // photosList.add(bm);
//        img.setImageBitmap(bm);
        userPicUri = data.getData();

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), userPicUri);
            //picture=bitmap;
            img.setImageBitmap(bitmap);
            uploadImage(userPicUri);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void uploadImage(Uri uri){
        Log.d("uri2", "onClick: " + uri);

        if (uri != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image...");

            StorageReference profileRef = storageReference.child("profileImages/" + mail);

            final Bitmap noUse = null;

            profileRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            userPicUrl = taskSnapshot.getDownloadUrl().toString();
                           // onAuth.onSuccess(noUse);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage(((int) progress) + "% Uploaded...");
                        }
                    })
            ;
        }
    }
}
