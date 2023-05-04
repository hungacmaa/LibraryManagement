package com.librarymanagement.fragments.admin;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.librarymanagement.R;
import com.librarymanagement.activities.AdminActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminAddBookFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ImageView bookImage;
    private EditText inputName, inputAmount, inputLocation;
    private Button btnSubmit;
    private Spinner spinnerCategory;
    private DatabaseReference database;
    private StorageReference storage;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_add_book, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {

        bookImage = view.findViewById(R.id.bookImage);
        inputName = view.findViewById(R.id.inputName);
        inputAmount = view.findViewById(R.id.inputAmount);
        inputLocation = view.findViewById(R.id.inputLocation);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        spinnerCategory.setOnItemSelectedListener(this);

        database = FirebaseDatabase.getInstance().getReference().child("AllBooks");
        storage = FirebaseStorage.getInstance().getReference();

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        categories.add("Romantic");
        categories.add("Sport");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCategory.setAdapter(adapter);

        //Setting onClick Listener for the imageView To select image
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                //setting the intent action to get content
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                //setting the upload content type as image
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get data from text view
                String bookName = inputName.getText().toString();
                String booksCount = inputAmount.getText().toString();
                String bookLocation = inputLocation.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();

                // validate
                if (bookName.isEmpty() || booksCount.isEmpty() || bookLocation.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null) {
                    Toast.makeText(getContext(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                } else {
                    //calling the method to add data to fireabase
                    Toast.makeText(getContext(), "Đang gửi dữ liệu...", Toast.LENGTH_SHORT).show();
                    uploadData(imageUri, bookName, booksCount, bookLocation, category);

                }
            }
        });

    }

    private void uploadData(Uri imageUri, String bookName, String booksCount, String bookLocation, String category) {

        //setting the file name as current time with milli Seconds to make the image name unique
        StorageReference fileRef = storage.child("images/"+System.currentTimeMillis() + "." + getFileExtension(imageUri));

        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //generating the unique key to add data under this node
                            String push = database.push().getKey().toString();

                            //Hash map to store values
                            HashMap bookDetails = new HashMap();

                            //adding the data to hashmap
                            bookDetails.put("bookName", bookName);
                            bookDetails.put("booksCount", booksCount);
                            bookDetails.put("bookLocation", bookLocation);
                            bookDetails.put("imageUrl", uri.toString());
                            bookDetails.put("category", category);
                            bookDetails.put("pushKey", push);

                            // uploading the data to the firebase
                            database.child(category)
                                    .child(push).setValue(bookDetails)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            FirebaseDatabase.getInstance().getReference().child("Categories")
                                                    .child(category).child("category").setValue(category)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            // Calling the same intent to reset all the current data
                                                            Intent intent = new Intent(getContext(), AdminActivity.class);
                                                            getActivity().startActivity(intent);
                                                            getActivity().finish();

                                                            // Showing the toast to user for confirmation
                                                            Toast.makeText(getContext(), "Thêm sách thành công", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                            //Showing the toast message to the user to reUpload the data
                            Toast.makeText(getContext(), "Thêm sách không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

    }

    private String getFileExtension(Uri imageUri) {

        // getting the image extension
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
        return extension;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            //Getting the image from the device and setting the image to imageView
            imageUri = data.getData();
            bookImage.setImageURI(imageUri);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
//        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
