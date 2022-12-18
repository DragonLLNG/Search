package com.example.finalexam;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalexam.databinding.FragmentMyFavoritesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyFavoritesFragment extends Fragment {
    ArrayList<Image> imageArrayList = new ArrayList<>();

    public MyFavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentMyFavoritesBinding binding;

    FavoriteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Favorites");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Images")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        imageArrayList.clear();
                        for(QueryDocumentSnapshot commentDoc : value) {
                            Image image = commentDoc.toObject(Image.class);
                            imageArrayList.add(image);


                        }
                       adapter.notifyDataSetChanged();
                    }
                });

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoriteAdapter(imageArrayList);
        binding.recyclerView.setAdapter(adapter);


    }


    class FavoriteAdapter extends  RecyclerView.Adapter<FavoriteAdapter.ImageViewHolder>{

        ArrayList<Image> images = new ArrayList<>();
        public FavoriteAdapter(ArrayList<Image> data){
            this.images = data;
        }


        @NonNull
        @Override
        public FavoriteAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photo_favorite, parent, false);
            FavoriteAdapter.ImageViewHolder imageViewHolder = new ImageViewHolder(view);
            return imageViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            Image image = images.get(position);

            holder.description.setText(image.description);
            holder.creatAt.setText(image.date);
            holder.userName.setText(image.userName);
            Picasso.get().load(image.url).into(holder.thumb);
            Picasso.get().load(image.userIcon).into(holder.userIcon);
            holder.image = image;
        }


        @Override
        public int getItemCount() {
            return images.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{
            TextView description, creatAt, userName;
            ImageView thumb, userIcon, trash;
            Image image;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                description = itemView.findViewById(R.id.textViewDescription);
                creatAt = itemView.findViewById(R.id.textViewCreatedAt);
                userName = itemView.findViewById(R.id.textViewUserFullName);

                trash = itemView.findViewById(R.id.imageViewTrash);


                thumb = itemView.findViewById(R.id.imageViewThumbnail);

                userIcon = itemView.findViewById(R.id.imageViewUserThumbnail);

                trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Images")
                                .document(image.id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("Comment", "onSuccess: Image successfully deleted");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Comment", "onFailure: Error deleting image" + e);
                                    }
                                });

                    }
                });



            }

        }


    }
}