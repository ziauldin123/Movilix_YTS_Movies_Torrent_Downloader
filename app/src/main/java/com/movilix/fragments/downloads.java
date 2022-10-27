package com.movilix.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.movilix.R;
import com.movilix.torrant.ui.BaseAlertDialog;
import com.movilix.torrant.ui.main.MainViewModel;
import com.movilix.torrant.ui.main.MsgMainViewModel;
import com.movilix.torrant.ui.main.drawer.DrawerExpandableAdapter;
//import com.movilix.ui.main.MainViewModel;
//import com.movilix.ui.main.drawer.DrawerExpandableAdapter;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link downloads#newInstance} factory method to
 * create an instance of this fragment.
 */
public class downloads extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private static final String TAG_PERM_DIALOG_IS_SHOW = "perm_dialog_is_show";
    private static final String TAG_ABOUT_DIALOG = "about_dialog";

    public static final String ACTION_ADD_TORRENT_SHORTCUT = "com.movilix.ADD_TORRENT_SHORTCUT";

    /* Android data binding doesn't work with layout aliases */
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private RecyclerView drawerItemsList;
    private LinearLayoutManager layoutManager;
    private DrawerExpandableAdapter drawerAdapter;
    private RecyclerView.Adapter wrappedDrawerAdapter;
    private RecyclerViewExpandableItemManager drawerItemManager;
    private SearchView searchView;
    private TextView sessionDhtNodesStat, sessionDownloadStat,
            sessionUploadStat, sessionListenPortStat;

    private MainViewModel viewModel;
    private MsgMainViewModel msgViewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private BaseAlertDialog.SharedViewModel dialogViewModel;
    private BaseAlertDialog aboutDialog;
    private boolean permDialogIsShow = false;





    Context context;
    public downloads(Context mContext) {
        this.context = mContext;
    }
    public downloads() {
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
    public static downloads newInstance(String param1, String param2) {
        downloads fragment = new downloads();
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
        View root = inflater.inflate(R.layout.activity_main_torrent, container, false);

        layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically()
            {
                /* Disable scroll, because RecyclerView is wrapped in ScrollView */
                return false;
            }
        };






        return root;
    }


}