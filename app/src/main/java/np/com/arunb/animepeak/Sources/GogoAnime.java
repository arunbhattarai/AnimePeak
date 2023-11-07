package np.com.arunb.animepeak.Sources;

import static np.com.arunb.animepeak.Activity.VideoPlayer.Current;

import static np.com.arunb.animepeak.Activity.VideoPlayer.exo_quality_txt;
import static np.com.arunb.animepeak.Activity.VideoPlayer.next_eps;
import static np.com.arunb.animepeak.Activity.VideoPlayer.player;
import static np.com.arunb.animepeak.Activity.VideoPlayer.previous_eps;
import static np.com.arunb.animepeak.Activity.VideoPlayer.sources;
import static np.com.arunb.animepeak.Activity.VideoPlayer.videoView;

import static np.com.arunb.animepeak.Activity.VideoPlayer.video_loading;
import static np.com.arunb.animepeak.Activity.VideoPlayer.video_quality;
import static np.com.arunb.animepeak.Activity.VideoPlayer.video_quality_num;
import static np.com.arunb.animepeak.Fragments.HomeFragment.Home_IDList;
import static np.com.arunb.animepeak.Fragments.HomeFragment.Home_TitleUrlList;
import static np.com.arunb.animepeak.Fragments.HomeFragment.Home_imageUrlList;
import static np.com.arunb.animepeak.Fragments.HomeFragment.LayoutManager;
import static np.com.arunb.animepeak.Fragments.HomeFragment.home_loading;
import static np.com.arunb.animepeak.Fragments.HomeFragment.isLoading;
import static np.com.arunb.animepeak.Fragments.HomeFragment.mainAdapter;
import static np.com.arunb.animepeak.Fragments.HomeFragment.more_loading;
import static np.com.arunb.animepeak.Fragments.HomeFragment.network_error;
import static np.com.arunb.animepeak.Fragments.HomeFragment.pos;
import static np.com.arunb.animepeak.Fragments.SearchFragment.Search_IDList;
import static np.com.arunb.animepeak.Fragments.SearchFragment.Search_TitleUrlList;
import static np.com.arunb.animepeak.Fragments.SearchFragment.Search_imageUrlList;
import static np.com.arunb.animepeak.Fragments.SearchFragment.Search_loading;
import static np.com.arunb.animepeak.Fragments.SearchFragment.not_found;
import static np.com.arunb.animepeak.Fragments.SearchFragment.searchBar;
import static np.com.arunb.animepeak.Fragments.SearchFragment.searchView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.SharedPreferences;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import np.com.arunb.animepeak.Activity.Anime_Details;

import np.com.arunb.animepeak.Adapters.Ani_Details_Adapter;
import np.com.arunb.animepeak.Adapters.Ani_Details_Genre_Adapter;

import np.com.arunb.animepeak.Adapters.MainAdapter;
import np.com.arunb.animepeak.Adapters.SearchAdapter;
import np.com.arunb.animepeak.R;

import com.google.android.exoplayer2.MediaItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GogoAnime {
    public static class Gogoanime_popular {

        private static final String TAG = "Hello";

        private final Activity activity;
        private final boolean is_added;
        ExecutorService executor;
        boolean is_new_page = false;
        boolean is_more = false;
        int page;

        public Gogoanime_popular(Activity activity, boolean is_added, boolean is_more, int page) {
            this.activity = activity;
            this.is_added = is_added;

            this.is_more = is_more;
            this.page = page;
            executor = Executors.newSingleThreadExecutor();
        }

        public void execute() {
            onPreExecute();

            executor.execute(() -> {
                String results = doInBackground();
                onPostExecute(results);
            });
            executor.shutdown();
        }

        private void onPreExecute() {
            if (is_more) {
                activity.runOnUiThread(() -> {
                    more_loading.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .asGif()
                            .load(R.raw.loading_animation)
                            .into(more_loading);
                });
            }

            // Display loading animation
            activity.runOnUiThread(() -> {
                Glide.with(activity)
                        .asGif()
                        .load(R.raw.loading_animation)
                        .into(home_loading);
            });

        }

        private String doInBackground() {
            StringBuilder response = new StringBuilder();
            try {
                if (Home_TitleUrlList.isEmpty() || isLoading) {

                    if (isLoading) {
                        is_new_page = true;
                    }

                    int last_page = page + 4;
                    for (int first_page = page; first_page < last_page; first_page++) {
                        String API_ENDPOINT = "https://api.consumet.org/anime/gogoanime/top-airing?page=" + page;
                        URL url = new URL(API_ENDPOINT);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json");

                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        conn.disconnect();
                    }


                }

//                activity.runOnUiThread(() -> network_error.setVisibility(View.GONE));
                isLoading = false;

                return response.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error retrieving top anime: " + e.getMessage());
//                activity.runOnUiThread(() -> network_error.setVisibility(View.VISIBLE));
                isLoading = false;
                return null;
            }
        }

        private void onPostExecute(String response) {
            boolean is_available;
            int Old_Home_TitleUrlList_size = 0;

            if (is_added && response != null) {
                is_available = true;
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(response);
                    JSONArray animeList = jsonObject.getJSONArray("results");

                    for (int i = 0; i < animeList.length(); i++) {
                        JSONObject anime = animeList.getJSONObject(i);
                        String title = anime.getString("title");
                        String image = anime.getString("image");
                        String ani_id = anime.getString("id");
                        Old_Home_TitleUrlList_size = Home_TitleUrlList.size();

                        if (!title.isEmpty() && !image.isEmpty() && !ani_id.isEmpty()) {
                            Home_TitleUrlList.add(title);
                            Home_imageUrlList.add(image);
                            Home_IDList.add(ani_id);
                        }
                    }

                } catch (JSONException e) {
//                        throw new RuntimeException(e);
                    Toast.makeText(activity, "End!!", Toast.LENGTH_SHORT).show();
                }

            } else {
                is_available = false;
            }

            int finalOld_Home_TitleUrlList_size = Old_Home_TitleUrlList_size;
            activity.runOnUiThread(() -> {
                // Your code to update UI elements goes here
                // Check if loading animation is still visible before hiding it
                mainAdapter = new MainAdapter(activity, Home_TitleUrlList, Home_imageUrlList, Home_IDList);
//                recyclerView.setAdapter(mainAdapter);
                mainAdapter.notifyItemRangeInserted(finalOld_Home_TitleUrlList_size - Home_TitleUrlList.size(), Home_TitleUrlList.size());
//

                if (home_loading.getVisibility() == View.VISIBLE) {
                    home_loading.setVisibility(View.GONE);
                }

            });
            activity.runOnUiThread(() -> {
                if (is_new_page) {

                    LayoutManager.scrollToPosition(pos - 1);
                    is_new_page = false;
                }
            });

            // Show or hide the network error based on loading status and data availability
            activity.runOnUiThread(() -> {
                // Your code to handle network error visibility goes here
                if (!is_available) {
                    network_error.setVisibility(View.VISIBLE);
                } else {
                    network_error.setVisibility(View.GONE);
                }
            });
            if (is_more) {
                activity.runOnUiThread(() -> {
                    more_loading.setVisibility(View.GONE);
                });
            }
            isLoading = false;
        }
    }

    public static class Gogoanime_details {
        Activity activity;
        int originalOrientation;
        int Error;
        public ExecutorService executor;

        public Gogoanime_details(Activity activity) {
            this.activity = activity;
        }

        public void execute() {
            onPreExecute();
            executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                String result = doInBackground();
                onPostExecute(result);
            });
            executor.shutdown();
        }

        protected void onPreExecute() {
            // Store the original orientation.
            originalOrientation = activity.getRequestedOrientation();

            // Lock the orientation to the current orientation.
            int currentOrientation = activity.getResources().getConfiguration().orientation;
            activity.setRequestedOrientation(currentOrientation);
            activity.runOnUiThread(() -> {
                // Your code for displaying loading animation goes here
                Anime_Details.anime_details.setVisibility(View.GONE);
                Anime_Details.episode_text.setVisibility(View.GONE);
                Glide.with(activity)
                        .asGif()
                        .load(R.raw.loading_animation)
                        .into(Anime_Details.details_loading);
            });

        }


        protected String doInBackground() {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("https://api.consumet.org/anime/gogoanime/info/" + Anime_Details.Ani_ID);
                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

                bufferedReader.close();
                Anime_Details.episodeID_list = Anime_Details.extractEpisodeIds(result.toString());

            } catch (IOException e) {

                Error = 1;

                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result.toString();
        }


        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                Anime_Details.img = jsonObject.getString("image");
                Anime_Details.releasedDate = jsonObject.getString("releaseDate");
                Anime_Details.status = jsonObject.getString("status");
                Anime_Details.desc = jsonObject.getString("description");
                if (Anime_Details.episodes.length() == 0) {

                    Anime_Details.episodes = jsonObject.getJSONArray("episodes");


                }

                Anime_Details.genres = jsonObject.getJSONArray("genres");
                if (Anime_Details.genres.length() > 4) {
                    JSONArray firstThreeItems = new JSONArray();
                    for (int i = 0; i < 4 && i < Anime_Details.genres.length(); i++) {
                        firstThreeItems.put(Anime_Details.genres.getString(i));

                    }
                    Anime_Details.genres = firstThreeItems;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!activity.isFinishing()) {
                activity.runOnUiThread(() -> {


                    Anime_Details.details_loading.setVisibility(View.GONE);
                    if (Error == 0) {

                        Anime_Details.net_error_ani_details.setVisibility(View.GONE);
                        Anime_Details.anime_details.setVisibility(View.VISIBLE);
                        Anime_Details.episode_text.setVisibility(View.VISIBLE);


                        Anime_Details.Release.setText("Release Date: " + Anime_Details.releasedDate);
                        Anime_Details.Status.setText("Status: " + Anime_Details.status);

                        Glide.with(activity)
                                .load(Anime_Details.img)
                                .into(Anime_Details.Anime_Image);

                        Ani_Details_Adapter ani_details_adapter = new Ani_Details_Adapter(Anime_Details.episodes, activity);
                        Anime_Details.details_recyclerView.setAdapter(ani_details_adapter);

                        Ani_Details_Genre_Adapter ani_details_genre_adapter = new Ani_Details_Genre_Adapter(activity, Anime_Details.genres);
                        Anime_Details.genre_recyclerView.setAdapter(ani_details_genre_adapter);

                        Anime_Details.expandableTextView.setText(Anime_Details.desc);
                        Anime_Details.expandableTextView.setReadMoreText("More");
                        Anime_Details.expandableTextView.setReadLessText("Less");
//
                        Anime_Details.expandableTextView.setAnimationDuration(500);

                        // Reset the orientation to the original orientation.
                        activity.setRequestedOrientation(originalOrientation);


                    } else {
                        Anime_Details.net_error_ani_details.setVisibility(View.VISIBLE);
                    }

                });
            }

        }

    }

    public static class GogoAnime_search {
        private static final String TAG = "Search";

        private static final String API_ENDPOINT = "https://api.consumet.org/anime/gogoanime/";
        Activity activity;
        boolean is_added;
        String text;

        public GogoAnime_search(Activity activity, boolean is_added, String text) {
            this.activity = activity;
            this.is_added = is_added;
            this.text = text;
        }

        public void execute() {
            onPreExecute();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                doInBackground();
                onPostExecute();
            });
            executor.shutdown();
        }

        private void onPreExecute() {
            activity.runOnUiThread(() -> {
                Search_loading.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .asGif()
                        .load(R.raw.loading_animation)
                        .into(Search_loading);
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
            });

        }


        private void doInBackground() {

            try {


//                String query = search_Box.getText().toString();
                text = text.replace(" ", "-");
                URL url = new URL(API_ENDPOINT + text);

                // Open an HTTP connection to the URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method and headers
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                // Read the response body from the input stream
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();

                // Parse the JSON response into a list of anime objects
                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                JSONArray animeList = jsonObject.getJSONArray("results");

                for (int i = 0; i < animeList.length(); i++) {
                    JSONObject anime = animeList.getJSONObject(i);
                    String title = anime.getString("title");

                    String image = anime.getString("image");


                    String ani_id = anime.getString("id");
                    if (!title.equals("") && !image.equals("") && !ani_id.equals("")) {
                        Search_TitleUrlList.add(title);
                        Search_imageUrlList.add(image);
                        Search_IDList.add(ani_id);

                    }

                }


            } catch (IOException | JSONException e) {

                Log.e(TAG, "Error retrieving top anime: " + e.getMessage());

            }
        }


        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        private void onPostExecute() {
            activity.runOnUiThread(() -> {
                Search_loading.setVisibility(View.GONE);

                if (is_added) {
                    // clear the data from the adapter


                    SearchAdapter searchAdapter = new SearchAdapter(activity, Search_TitleUrlList, Search_imageUrlList, Search_IDList);
                    // notify the adapter that the data has changed
                    searchAdapter.notifyDataSetChanged();
                    searchView.setAdapter(searchAdapter);
                }
                if (Search_TitleUrlList.size() == 0) {
                    not_found.setText("Cannot Find the Anime ");
                    not_found.setVisibility(View.VISIBLE);
                } else {
                    not_found.setVisibility(View.GONE);
                }
            });

        }
    }

    public static class Gogoanime_stream {
        Activity activity;
        JSONObject source;
        ExecutorService executor;

        public Gogoanime_stream(Activity activity) {
            this.activity = activity;
            executor = Executors.newSingleThreadExecutor();
        }

        public void execute() {
            onPreExecute();

            executor.execute(() -> {
                String results = doInBackground();
                onPostExecute(results);
            });
            executor.shutdown();
        }

        protected void onPreExecute() {
            activity.runOnUiThread(() -> {
                videoView.setVisibility(View.INVISIBLE);
                previous_eps.setVisibility(View.GONE);
                next_eps.setVisibility(View.GONE);
            });
            activity.runOnUiThread(() -> {
                // Your code for displaying loading animation goes here
                if (video_loading.getVisibility() == View.GONE) {
                    video_loading.setVisibility(View.VISIBLE);
                }
                Glide.with(activity)
                        .asGif()
                        .load(R.raw.loading_animation)
                        .into(video_loading);
            });


        }

        public void Cancel() {
            // Implement your cancellation logic here
            executor.shutdownNow();

        }


        private String doInBackground() {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("https://api.consumet.org/anime/gogoanime/watch/" + Anime_Details.episodeID_list.get(Current));
                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result.toString();
        }


        @SuppressLint("SetTextI18n")
        private void onPostExecute(String result) {
            activity.runOnUiThread(() -> {
                try {
                    if (player != null) {
                        if (player.isPlaying()) {
                            player.stop();
                        }

                        JSONObject jsonObject = new JSONObject(result);
                        sources = jsonObject.getJSONArray("sources");
                        for (int i = 0; i < sources.length(); i++) {
                            JSONObject source = sources.getJSONObject(i);
                            String quality = source.getString("quality");
                            if (!quality.equals("backup") && !quality.equals("default")) {
                                video_quality.add(quality);
                            }
                        }


                        SharedPreferences sharedpreferences = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
                        String Current_Quality = sharedpreferences.getString("Video_Quality", "480p");
                        video_quality_num = video_quality.indexOf(Current_Quality);
                        if (video_quality_num == -1) {
                            exo_quality_txt.setText("Quality(" + video_quality.get(0) + ")");

                            source = sources.getJSONObject(0);
                        } else {

                            exo_quality_txt.setText("Quality(" + video_quality.get(video_quality_num) + ")");
                            source = sources.getJSONObject(video_quality_num);
                        }
                        String Link = source.getString("url");


// Create a MediaSource from the URL.
                        Uri videoUri = Uri.parse(Link);

// Set the media item to be played.
                        player.setMediaItem(MediaItem.fromUri(videoUri));

                        player.prepare();

                        videoView.setPlayer(player);
                        player.setPlayWhenReady(true);

                        videoView.setVisibility(View.VISIBLE);
                        previous_eps.setVisibility(View.VISIBLE);
                        next_eps.setVisibility(View.VISIBLE);
                        if (video_loading.getVisibility() == View.VISIBLE) {
                            video_loading.setVisibility(View.GONE);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });


        }
    }


}
