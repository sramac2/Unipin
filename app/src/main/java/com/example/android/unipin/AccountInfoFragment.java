package com.example.android.unipin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountInfoFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String username;
    private String email;
    private StorageReference storageReference;
    private TextView usernameTextView;
    private TextView emailTextView;
    private String uId;
    private ImageButton nameButton;
    private ImageButton emailButton;
    private ImageView imageView;
    private FirebaseUser firebaseUser;
    private Button profileButton;
    private ImageButton passwordButton;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_info_fragment, container, false);

        imageView = view.findViewById(R.id.profile_image);
        emailTextView = view.findViewById(R.id.email_profile_update);
        usernameTextView = view.findViewById(R.id.name_profile_update);
        nameButton = view.findViewById(R.id.name_edit_profile);
        emailButton = view.findViewById(R.id.email_edit_profile);
        passwordButton = view.findViewById(R.id.password_edit_profile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();

        username = user.getDisplayName();
        email = user.getEmail();

        emailTextView.setText(email);
        usernameTextView.setText(username);
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getActivity());
                editText.setText(username);
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setView(editText)
                        .setTitle("Type your name: ")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button button = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(name)) {
                                    Toast.makeText(getActivity(),
                                            "Please type your name", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (!name.contains(" ")) {
                                    Toast.makeText(getActivity(),
                                            "Please type your first and Last ane " +
                                                    "with a space inbetween",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    UserProfileChangeRequest profileChangeRequest =
                                            new UserProfileChangeRequest
                                                    .Builder()
                                                    .setDisplayName(name)
                                                    .build();
                                    firebaseUser.updateProfile(profileChangeRequest)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        onStart();
                                                        Toast.makeText(getActivity(),
                                                                "Your name has been updated " +
                                                                        "successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    } else {
                                                        Toast.makeText(getActivity(),
                                                                task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getActivity());
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setView(editText)
                        .setTitle("Type your new password")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button button = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String password = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(password)) {
                                    Toast.makeText(getActivity(), "Password is empty",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    firebaseUser.updatePassword(password)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(),
                                                                "Password updated successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    } else {
                                                        Toast.makeText(getActivity()
                                                                , task.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });

                    }
                });
                dialog.show();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getActivity());
                editText.setText(email);
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setView(editText)
                        .setTitle("Type your new email: ")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button button = ((AlertDialog) dialog)
                                .getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(email)) {
                                    Toast.makeText(getActivity()
                                            , "Please type your email",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    firebaseUser.updateEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(),
                                                                "Your Email updated " +
                                                                        "Successfully",
                                                                Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    } else {
                                                        Toast.makeText(getActivity(),
                                                                task.getException().getMessage()
                                                                , Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        uId = firebaseUser.getUid();

        storageReference = FirebaseStorage.getInstance().getReference(uId + ".jpg");

        imageView = view.findViewById(R.id.profile_image);
        image();
        return view;

    }

    public void image(){
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageView);
    }

    @Override
    public void onStart() {
        super.onStart();
        username = user.getDisplayName();
        email = user.getEmail();
        emailTextView.setText(email);
        usernameTextView.setText(username);
    }


}
