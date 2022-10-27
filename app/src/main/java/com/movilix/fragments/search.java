package com.movilix.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.movilix.R;
import com.movilix.adapters.SearchAdapter;
import com.movilix.appInstance.Singleton;
import com.movilix.praser.Praser;

import org.json.JSONException;
import org.json.JSONObject;

import static com.movilix.torrant.ui.FragmentCallback.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView search_RecyclerView;
    SearchAdapter search_adapter;
    SearchView searchView;
    TextView searchTxt,topSearches,no_result_txt;
    Praser search;
    ProgressBar bar;
    Context context;
    public search(Context mContext) {
        this.context = mContext;
    }
    public search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search.
     */
    // TODO: Rename and change types and number of parameters
    public static search newInstance(String param1, String param2) {
        search fragment = new search();
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
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        search_RecyclerView = root.findViewById(R.id.searchRecycle);
        searchView=root.findViewById(R.id.simpleSearchView);
        no_result_txt=root.findViewById(R.id.no_result_txt);
        bar= root.findViewById(R.id.searchBar);
        topSearches=root.findViewById(R.id.topSearches);
        bar.setVisibility(View.VISIBLE);
        no_result_txt.setVisibility(View.GONE);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        final TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        int id2 = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchClose = searchView.findViewById(id2);
        searchClose.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        searchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setText("");
                topSearches.setText("Top Rated Searches");
//                addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&sort_by=like_count");
//                addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&order_by=desc");
                addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&sort_by=download_count&order_by=desc");
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clear();
                bar.setVisibility(View.VISIBLE);

                topSearches.setVisibility(View.GONE);
                if(!query.isEmpty()) {
                    addreq("https://yts.lt/api/v2/list_movies.json?query_term=" + query);
                }else {
                    addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&order_by=desc");
                }
                searchView.clearFocus();
                if(query.isEmpty()){
                    searchTxt.setVisibility(View.GONE);
                    topSearches.setVisibility(View.VISIBLE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                clear();
                //bar.setVisibility(View.VISIBLE);

                topSearches.setText("Top Searches");
                if(!newText.isEmpty()) {
                    addreq("https://yts.lt/api/v2/list_movies.json?query_term=" + newText);
                }
                else{
                    addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&order_by=desc");
                }
                if(newText.isEmpty()){

                    topSearches.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });


        search_RecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        search = new Praser(getActivity());
        search_adapter = new SearchAdapter(getActivity(), search.list);
        search_RecyclerView.setAdapter(search_adapter);

//        addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&sort_by=like_count");
        addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&sort_by=download_count&order_by=desc");
        return root;
    }
    public void clear() {
        final int size = search.list.size();
        search.list.clear();
        search_adapter.notifyItemRangeRemoved(0, size);
    }

    void addreq(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getJSONObject("data").getInt("movie_count") != 0) {
                                search.JsonPrase(response);
                                bar.setVisibility(View.GONE);
                                no_result_txt.setVisibility(View.GONE);
                                search_adapter.notifyDataSetChanged();
                                Log.d(TAG, "JsonPrase Data: " + response);
                            }else {
                                Log.d(TAG, "JsonPrase Data: NO data" );
                                bar.setVisibility(View.GONE);
                                no_result_txt.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                no_result_txt.setVisibility(View.VISIBLE);
//                Toast.makeText(ge, "No Result Found..." , Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        Singleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}