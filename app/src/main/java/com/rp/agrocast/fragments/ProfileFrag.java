package com.rp.agrocast.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rp.agrocast.Login;
import com.rp.agrocast.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button btn_save;
    RoundedImageView roundedImageView;
    FloatingActionButton fab_logout;
    EditText et_name, et_regNo, et_contact, et_nic, tv_email;
    FirebaseAuth auth;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    String Uid;
    Uri mImageUri = null;
    Uri downloadUri = null;
    String encodedImage;
    ProgressBar progressBar;
    private boolean isPhotoSelected = false;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFrag newInstance(String param1, String param2) {
        ProfileFrag fragment = new ProfileFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btn_save = view.findViewById(R.id.btn_save);
        roundedImageView = view.findViewById(R.id.imageProfile);
        et_name = view.findViewById(R.id.name);
        et_regNo = view.findViewById(R.id.registerNo);
        tv_email = view.findViewById(R.id.email);
        et_contact = view.findViewById(R.id.contact);
        et_nic = view.findViewById(R.id.nic);
        fab_logout = view.findViewById(R.id.fablogout);
        progressBar = view.findViewById(R.id.Progressbar);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Uid = auth.getCurrentUser().getUid();

        getProfile();

        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidity();
            }
        });

        return view;
    }


    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            roundedImageView.setImageBitmap(bitmap);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHight = bitmap.getHeight() * previewWidth / bitmap.getHeight();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if(encodedImage != null){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else {
            return null;
        }

    }

    private void getProfile() {
        firestore.collection("users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String name = task.getResult().getString("name");
                        String regNo = task.getResult().getString("regNo");
                        String email = task.getResult().getString("email");
                        String contact = task.getResult().getString("contact");
                        String nic = task.getResult().getString("nicNo");
                        String image = task.getResult().getString("image");

                        roundedImageView.setImageBitmap(getBitmapFromEncodedString(image));
                        et_name.setText(name);
                        et_regNo.setText(regNo);
                        tv_email.setText(email);
                        et_contact.setText(contact);
                        et_nic.setText(nic);
                    }
                }
            }
        });
    }

    private void updateProfile() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", et_name.getText().toString());
        user.put("regNo", et_regNo.getText().toString());
        user.put("contact", et_contact.getText().toString());
        user.put("nicNo", et_nic.getText().toString());
        user.put("image", encodedImage);
        database.collection("users")
                .document(Uid)
                .update(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    getProfile();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    getProfile();
                });
        getProfile();
    }

    public void checkValidity() {
        String name = et_name.getText().toString();
        String registerNo = et_regNo.getText().toString();
        String contactNo = et_contact.getText().toString();
        String nic = et_nic.getText().toString();

        if (TextUtils.isEmpty(name)) {
            et_name.setError("Name is required.");
            return;
        }else if(TextUtils.isEmpty(registerNo)){
            et_regNo.setError("Email is required.");
            return;
        }else if(TextUtils.isEmpty(contactNo)){
            et_contact.setError("Email is required.");
            return;
        }else if(TextUtils.isEmpty(nic)){
            et_nic.setError("Email is required.");
            return;
        }else if(encodedImage == null){
            Toast.makeText(getContext(), "Please add an image.", Toast.LENGTH_SHORT).show();
        }else{
            updateProfile();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), Login.class));
    }
}