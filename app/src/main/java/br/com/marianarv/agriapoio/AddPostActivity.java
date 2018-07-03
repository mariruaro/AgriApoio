package br.com.marianarv.agriapoio;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPostActivity extends AppCompatActivity {
    EditText mTitle, mDate, mHour, mLocation, mDescription;
    ImageView mImagePost;
    Button mButtonAdd;

    String storagePath = "All_Images_Uploads/";
    String databasePath = "Data";
    int cameraOrFile=0;

    Uri mFilePathUri;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    int IMAGE_REQUEST_CODE=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Adicionar Novo Registro");


        mTitle = (EditText) findViewById(R.id.inputTitle);
        mDate = (EditText) findViewById(R.id.inputDate);
        mHour = (EditText) findViewById(R.id.inputHour);
        mLocation = (EditText) findViewById(R.id.inputLocation);
        mDescription = (EditText) findViewById(R.id.inputDescription);
        mButtonAdd = (Button) findViewById(R.id.btnAdd);
        mImagePost = (ImageView) findViewById(R.id.inputImage);

        mImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraOrFile=1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione Imagem"), IMAGE_REQUEST_CODE);
            }
        });

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToFirebase();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        progressDialog = new ProgressDialog(AddPostActivity.this);
    }
    private void uploadDataToFirebase(){
        if(mFilePathUri!=null){
            progressDialog.setTitle("Adicionando...");
            progressDialog.show();
            StorageReference storageReference2nd =
                    storageReference.child(storagePath+System.currentTimeMillis()+"."+getFileExtension(mFilePathUri));

            storageReference2nd.putFile(mFilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String mPostTitle = mTitle.getText().toString().trim();
                            String mPostDate = mDate.getText().toString().trim();
                            String mPostHour = mHour.getText().toString().trim();
                            String mPostLocation = mLocation.getText().toString().trim();
                            String mPostDescription = mDescription.getText().toString().trim();

                            progressDialog.dismiss();

                            Toast.makeText(AddPostActivity.this,"Adicionado com sucesso",Toast.LENGTH_SHORT).show();
                            Model modelUploadInfo = new Model(mPostTitle,mPostDate,mPostHour,
                                    mPostDescription,mPostLocation,taskSnapshot.getDownloadUrl().toString());

                            String imageUploadId = databaseReference.push().getKey();

                            databaseReference.child(imageUploadId).setValue(modelUploadInfo);
                            Intent intent = new Intent(AddPostActivity.this, PostListActivity.class);
                            startActivity(intent);
                            AddPostActivity.this.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            Toast.makeText(AddPostActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setTitle("Adicionando...");
                        }
                    });
        }else{
            Toast.makeText(AddPostActivity.this,"Por Favor, selecione imagem ou adicione nome da imagem",Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null
                && data.getData()!=null){
            mFilePathUri = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mFilePathUri);
                mImagePost.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
