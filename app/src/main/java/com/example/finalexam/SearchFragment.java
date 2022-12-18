package com.example.finalexam;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalexam.databinding.FragmentSearchBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//nisZYQnONhZ7QhjVxwZJiMeObh5AtBn1qoGRCko9P70
public class SearchFragment extends Fragment {

    private final OkHttpClient client = new OkHttpClient();

    ArrayList<Image> imageArrayList = new ArrayList<>();

    SearchAdapter adapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentSearchBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search");
        setHasOptionsMenu(true);





        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = binding.editTextSearchKeyword.getText().toString();
                getSearchResults(keyword);

            }
        });

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchAdapter(imageArrayList);
        binding.recyclerView.setAdapter(adapter);



    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout_menu_item){
            FirebaseAuth.getInstance().signOut();
            mListener.login();

            return true;
        } else if(item.getItemId() == R.id.my_favorites_menu_item){
            mListener.gotoFavorite();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getSearchResults(String query){
        final String api = "nisZYQnONhZ7QhjVxwZJiMeObh5AtBn1qoGRCko9P70";


        HttpUrl url = HttpUrl.parse("https://api.unsplash.com/search/photos").newBuilder()
                .addQueryParameter("client_id",api)
                .addQueryParameter("query",query)
                .addQueryParameter("per_page","50")
                .addQueryParameter("orientation","landscape")
                .addQueryParameter("content_filter","high")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){

                    try {
                        String body = response.body().string();
                        JSONObject json = new JSONObject(body);

                        JSONArray jsonArray = json.getJSONArray("results");

                        Log.d("demo", "onResponse: results "+jsonArray.length());

                        for (int i =0; i<jsonArray.length(); i++){
                            //imageArrayList.clear();
                            Image image = new Image();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            image.setUrl(jsonObject.getJSONObject("urls").getString("thumb"));
                            image.setDate(jsonObject.getString("created_at"));
                            image.setDescription(jsonObject.getString("description"));
                            image.setUserName(jsonObject.getJSONObject("user").getString("username"));
                            image.setUserIcon((String) jsonObject.getJSONObject("user").getJSONObject("profile_image")
                                    .getString("small"));
                            image.setUserLink((String) jsonObject.getJSONObject("user").getJSONObject("links")
                                   .getString("html"));
                            Log.d("demo", "onResponse: "+ jsonObject.getJSONObject("user").getJSONObject("links")
                                    .getString("html"));

                            image.setLiked(jsonObject.getBoolean("liked_by_user"));
                            imageArrayList.add(image);
                        }
                        Log.d("demo", "onResponse: "+imageArrayList.get(0).liked);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("demo", "onResponse: size " + imageArrayList.size());

                }
            }
        });


                //base url is https://api.unsplash.com/search/photos/?client_id=your-acess-key&query=user-entered-keywords&per_page=50&orientation=landscape&content_filter=high

    }

   SearchListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SearchListener) context;
    }

    interface SearchListener {
        void login();
        void gotoFavorite();
        void gotoDetail(Image image);
    }


    class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.ImageViewHolder>{

        ArrayList<Image> images = new ArrayList<>();
        public SearchAdapter(ArrayList<Image> data){
            this.images = data;
        }


        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photo_search, parent, false);
            ImageViewHolder imageViewHolder = new ImageViewHolder(view);
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

           if(image.liked==true){
               holder.heart.setImageResource(R.drawable.ic_heart_favorite);
           }
           else{ holder.heart.setImageResource(R.drawable.ic_heart_not_favorite);
           }

           holder.image = image;

        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{
            TextView description, creatAt, userName;
            ImageView thumb, userIcon, heart;
            Image image;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                description = itemView.findViewById(R.id.textViewDescription);
                creatAt = itemView.findViewById(R.id.textViewCreatedAt);
                userName = itemView.findViewById(R.id.textViewUserFullName);

                heart = itemView.findViewById(R.id.imageViewFavorite);


                heart.setOnClickListener(new View.OnClickListener() {
                    Boolean clicked = true;
                    @Override
                    public void onClick(View v) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                      //  likeUserInstance.setUserid(user.getUid());
                        if (clicked) {
                            heart.setImageResource(R.drawable.ic_heart_favorite);
                            clicked = false;

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            DocumentReference docRef = db.collection("Images").document();
                            docRef.update("id",docRef.getId());
                            image.id = docRef.getId();
                            image.liked = true;
                            docRef.set(image).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Log.d("store", "onComplete: " + image);

                                    }

                                }
                                });



                        } else {
                            clicked = true;
                            heart.setImageResource(R.drawable.ic_heart_not_favorite);
                          //  mListener.unlike(forum);
//
                            Log.d("unLike", "onClick: "+user.getDisplayName());
                        }

                    }
                });

                thumb = itemView.findViewById(R.id.imageViewThumbnail);

                userIcon = itemView.findViewById(R.id.imageViewUserThumbnail);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.gotoDetail(image);
                    }
                });


            }

        }


    }


}