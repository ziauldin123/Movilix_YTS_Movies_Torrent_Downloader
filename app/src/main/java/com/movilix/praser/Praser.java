package com.movilix.praser;

import android.content.Context;
import android.util.Log;

import com.movilix.Model.Cast;
import com.movilix.Model.Model;
import com.movilix.Model.Torrent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Praser {
    public ArrayList<Model> list = new ArrayList<>();
    String TAG = "Json";
    Context context;


    public Praser(Context context) {
        this.context = context;
    }

    public boolean JsonPrase(JSONObject jsonObject) {

        try {
            jsonObject.getString("status");
            String q = jsonObject.getString("status_message");
            if (q.matches("Query was successful")) {
                Log.d(TAG, "JsonPrase: " + jsonObject.getString("status_message"));
                Log.d(TAG, "JsonPrase Data: " + jsonObject.getJSONObject("data"));
                JSONObject data = jsonObject.getJSONObject("data");
                data.getString("movie_count");
                int count=jsonObject.getJSONObject("data").getInt("movie_count");
                Log.d(TAG, "JsonPrase Data: " + count);
                //   Log.d(TAG, "JsonPrase: " +   data.getString("limit"));

                //  data.getString("page_number");
                if(count!=0) {
                    JSONArray movie = data.getJSONArray("movies");
                    for (int i = 0; i < movie.length(); i++) {
                        Model model = new Model();
                        JSONObject movieDetails = movie.getJSONObject(i);
                        model.setId(movieDetails.getInt("id"));
                        model.setUrl(movieDetails.getString("url"));
                        model.setImdb_code(movieDetails.getString("imdb_code"));
                        model.setTitle(movieDetails.getString("title"));
                        model.setTitle_english(movieDetails.getString("title_english"));
                        model.setTitle_long(movieDetails.getString("title_long"));
                        model.setSlug(movieDetails.getString("slug"));
                        model.setYear(movieDetails.getInt("year"));
                        model.setRating(movieDetails.getDouble("rating"));
                        model.setRuntime(movieDetails.getInt("runtime"));
                        model.setSummary(movieDetails.getString("summary"));
                        model.setDescription_ful(movieDetails.getString("description_full"));
                        model.setSynopsis(movieDetails.getString("synopsis"));
                        model.setYts_trailer_code(movieDetails.getString("yt_trailer_code"));
                        model.setLanguage(movieDetails.getString("language"));
                        model.setBackroung_image(movieDetails.getString("background_image"));
                        model.setSmall_cover(movieDetails.getString("small_cover_image"));
                        model.setMedium_cover_image(movieDetails.getString("medium_cover_image"));
                        if (movieDetails.has("date_uploaded")){
                            model.setDate_uploaded(movieDetails.getString("date_uploaded"));
                        }
                        try {

                            model.setLarge_cover_image(movieDetails.getString("large_cover_image"));
                        } catch (Exception e) {

                        }
//
                        //gener adding
                        JSONArray gener = movieDetails.getJSONArray("genres");
                        ArrayList<String> g = new ArrayList<>();
                        for (int j = 0; j < gener.length(); j++) {
                            g.add(gener.get(j).toString());
                        }
                        model.setGener(g);

                        //torrents adding
                        if (movieDetails.has("torrents")){
                            JSONArray torrents = movieDetails.getJSONArray("torrents");
                            ArrayList<Torrent> torentlist = new ArrayList<>();
                            for (int k = 0; k < torrents.length(); k++) {
                                JSONObject t = torrents.getJSONObject(k);
                                // Object
                                Torrent torrentobj = new Torrent();
                                torrentobj.setUrl(t.getString("url"));
                                torrentobj.setHash(t.getString("hash"));
                                torrentobj.setQuality(t.getString("quality"));
                                torrentobj.setSize(t.getString("size"));
                                torrentobj.setSeed(t.getInt("seeds"));
                                torrentobj.setPeer(t.getInt("peers"));
                                // Adding in List
                                torentlist.add(torrentobj);
                            }
                            model.setTorrents(torentlist);
                        }
                        Log.d(TAG, "JsonPrase:Done1 ");
                        list.add(model);
                    }
                }
            } else {

                return false;

            }

        } catch (JSONException e) {
                  e.printStackTrace();
            Log.d(TAG, "JsonPrase: "+e);
        }


        return true;
    }


    public Model JsonCast(JSONObject jsonObject) {

        try {
            jsonObject.getString("status");
            String q = jsonObject.getString("status_message");
            if (q.matches("Query was successful")) {
                Log.d(TAG, "JsonPrase: " + jsonObject.getString("status_message"));
                JSONObject data = jsonObject.getJSONObject("data");
                //   Log.d(TAG, "JsonPrase: " +   data.getString("limit"));
                Model model = new Model();
                //  data.getString("page_number");
                JSONObject movieDetails = data.getJSONObject("movie");

                model.setId(movieDetails.getInt("id"));
                model.setUrl(movieDetails.getString("url"));
                model.setImdb_code(movieDetails.getString("imdb_code"));
                model.setTitle(movieDetails.getString("title"));
                model.setTitle_english(movieDetails.getString("title_english"));
                model.setTitle_long(movieDetails.getString("title_long"));
                model.setSlug(movieDetails.getString("slug"));
                model.setYear(movieDetails.getInt("year"));
                model.setRating(movieDetails.getDouble("rating"));
                model.setRuntime(movieDetails.getInt("runtime"));
                model.setDescription_ful(movieDetails.getString("description_intro"));
                model.setYts_trailer_code(movieDetails.getString("yt_trailer_code"));
                model.setLanguage(movieDetails.getString("language"));
                model.setBackroung_image(movieDetails.getString("background_image"));
                model.setSmall_cover(movieDetails.getString("small_cover_image"));
                model.setMedium_cover_image(movieDetails.getString("medium_cover_image"));
                model.setShot1(movieDetails.getString("medium_screenshot_image1"));
                model.setShot2(movieDetails.getString("medium_screenshot_image2"));
                model.setShot3(movieDetails.getString("medium_screenshot_image3"));
                model.setDownload(movieDetails.getInt("download_count"));
                model.setLike(movieDetails.getInt("like_count"));

                model.setDate_uploaded(movieDetails.getString("date_uploaded"));
                //gener adding
                JSONArray gener = movieDetails.getJSONArray("genres");
                ArrayList<String> g = new ArrayList<>();
                for (int j = 0; j < gener.length(); j++) {
                    g.add(gener.get(j).toString());
                }
                model.setGener(g);
                try {


                    //Cast Adding
                    ArrayList<Cast> castArrayList = new ArrayList<>();
                    JSONArray casts = movieDetails.getJSONArray("cast");
                    for (int m = 0; m < casts.length(); m++) {
                        JSONObject c = casts.getJSONObject(m);
                        Cast cast = new Cast();
                        cast.setName(c.getString("name"));
                        cast.setChracerName(c.getString("character_name"));
//                        cast.setUrl(""+ c.getString("url_small_image").toString());
                        cast.setImdbcode(c.getString("imdb_code"));
                        castArrayList.add(cast);
                    }
                    model.setCasts(castArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //torrents adding

                JSONArray torrents = movieDetails.getJSONArray("torrents");
                ArrayList<Torrent> torentlist = new ArrayList<>();
                for (int k = 0; k < torrents.length(); k++) {
                    JSONObject t = torrents.getJSONObject(k);
                    // Object
                    Torrent torrentobj = new Torrent();
                    torrentobj.setUrl(t.getString("url"));
                    torrentobj.setHash(t.getString("hash"));
                    torrentobj.setQuality(t.getString("quality"));
                    torrentobj.setSize(t.getString("size"));
                    torrentobj.setSeed(t.getInt("seeds"));
                    torrentobj.setPeer(t.getInt("peers"));
                    // Adding in List
                    torentlist.add(torrentobj);
                }
                model.setTorrents(torentlist);
                Log.d(TAG, "JsonPrase:Done ");
                return model;
            } else {


                return null;

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }


        return null;
    }


}
