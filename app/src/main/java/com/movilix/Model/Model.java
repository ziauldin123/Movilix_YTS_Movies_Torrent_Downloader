package com.movilix.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable {
    int id;
    String url;
    String imdb_code;
    String title;
    String title_english;
    String title_long;
    String slug;
    int year;
    int like;
    int download;
    String shot1;
    String shot2;
    String shot3;
    ArrayList<Cast> casts = new ArrayList<>();
    double rating;
    int runtime;
    String summary;
    String description_ful;
    String synopsis;
    String yts_trailer_code;
    String language;
    String large_cover_image;
    String backroung_image;
    String small_cover;
    String medium_cover_image;
    String date_uploaded;
    ArrayList<String> gener = new ArrayList<>();
    ArrayList<Torrent> torrents = new ArrayList<>();


    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public String getShot1() {
        return shot1;
    }

    public void setShot1(String shot1) {
        this.shot1 = shot1;
    }

    public String getShot2() {
        return shot2;
    }

    public void setShot2(String shot2) {
        this.shot2 = shot2;
    }

    public String getShot3() {
        return shot3;
    }

    public void setShot3(String shot3) {
        this.shot3 = shot3;
    }

    public ArrayList<Cast> getCasts() {
        return casts;
    }

    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
    }

    public ArrayList<Torrent> getTorrents() {
        return torrents;
    }

    public void setTorrents(ArrayList<Torrent> torrents) {
        this.torrents = torrents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImdb_code() {
        return imdb_code;
    }

    public void setImdb_code(String imdb_code) {
        this.imdb_code = imdb_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_english() {
        return title_english;
    }

    public void setTitle_english(String title_english) {
        this.title_english = title_english;
    }

    public String getTitle_long() {
        return title_long;
    }

    public void setTitle_long(String title_long) {
        this.title_long = title_long;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription_ful() {
        return description_ful;
    }

    public void setDescription_ful(String description_ful) {
        this.description_ful = description_ful;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getYts_trailer_code() {
        return yts_trailer_code;
    }

    public void setYts_trailer_code(String yts_trailer_code) {
        this.yts_trailer_code = yts_trailer_code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBackroung_image() {
        return backroung_image;
    }

    public void setBackroung_image(String backroung_image) {
        this.backroung_image = backroung_image;
    }

    public String getSmall_cover() {
        return small_cover;
    }

    public void setSmall_cover(String small_cover) {
        this.small_cover = small_cover;
    }

    public String getMedium_cover_image() {
        return medium_cover_image;
    }

    public void setMedium_cover_image(String medium_cover_image) {
        this.medium_cover_image = medium_cover_image;
    }

    public String getDate_uploaded() {
        return date_uploaded;
    }

    public void setDate_uploaded(String date_uploaded) {
        this.date_uploaded = date_uploaded;
    }

    public ArrayList<String> getGener() {
        return gener;
    }

    public void setGener(ArrayList<String> gener) {
        this.gener = gener;
    }

    public String getLarge_cover_image() {
        return large_cover_image;
    }

    public void setLarge_cover_image(String large_cover_image) {
        this.large_cover_image = large_cover_image;
    }
}
