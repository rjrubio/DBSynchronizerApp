package com.example.dbprototypeapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dbprototypeapp.AddingCollegeActivity;
import com.example.dbprototypeapp.College;
import com.example.dbprototypeapp.CollegeActivity;
import com.example.dbprototypeapp.CollegeAdapter;
import com.example.dbprototypeapp.DatabaseHelper;
import com.example.dbprototypeapp.Main2Activity;
import com.example.dbprototypeapp.R;

import java.sql.SQLException;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    List<College> collegeList = null;
    CollegeAdapter dw = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.activity_college, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        swipeLayout = root.findViewById(R.id.refreshView);
        recycleView = root.findViewById(R.id.college_listView);
        layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        DatabaseHelper db = new DatabaseHelper(getActivity());
        try {
            collegeList = db.getAll(College.class);
        }catch (SQLException ex){
            Log.e("College Activity :",ex.toString());
        }
        dw = new CollegeAdapter(collegeList,getActivity());
        recycleView.setAdapter(dw);

        ((Main2Activity)getActivity()).setFragmentRefreshListener(new Main2Activity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load data to your RecyclerView
                reloadData();
            }
        });

        return root;
    }

    public void reloadData(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        DatabaseHelper db = new DatabaseHelper(getActivity());
        try {
            collegeList = db.getAll(College.class);
            dw = new CollegeAdapter(collegeList,getActivity());
            recycleView.setAdapter(dw);
            dw.notifyDataSetChanged();
            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
            }
            Log.e("College ron :","rubio");
        }catch (SQLException ex){
            Log.e("College Activity :",ex.toString());
        }

    }
}