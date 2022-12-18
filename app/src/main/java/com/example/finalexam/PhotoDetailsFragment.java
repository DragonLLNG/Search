package com.example.finalexam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalexam.databinding.FragmentPhotoDetailsBinding;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_IMAGE = "param1";


    // TODO: Rename and change types of parameters
    private Image mParam;
    public PhotoDetailsFragment() {
        // Required empty public constructor
    }



    public static PhotoDetailsFragment newInstance(Image image) {
        PhotoDetailsFragment photoDetailsFragment = new PhotoDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_IMAGE, image);
        photoDetailsFragment.setArguments(args);
        return photoDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = (Image) getArguments().getSerializable(ARG_PARAM_IMAGE);

        }
    }
    FragmentPhotoDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textViewDescription.setText(mParam.description);
        binding.textViewCreatedAt.setText(mParam.date);
        binding.textViewUserFullName.setText(mParam.userName);

        Picasso.get().load(mParam.url).into(binding.imageViewThumbnail);
        Picasso.get().load(mParam.userIcon).into(binding.imageViewUserThumbnail);


        binding.imageViewUserThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mParam.userLink));
                startActivity(intent);

            }
        });

    }
}