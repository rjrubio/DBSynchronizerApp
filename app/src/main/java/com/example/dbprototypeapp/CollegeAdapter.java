package com.example.dbprototypeapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.List;

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.CollegeViewHolder>{

    private List<College> collegeList;
    private Context mContext;


    public class CollegeViewHolder extends RecyclerView.ViewHolder {

        public TextView name, age, index;
        public LinearLayout editLayout, deleteLayout;

        public CollegeViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            age = (TextView) view.findViewById(R.id.age);
            index = (TextView) view.findViewById(R.id.index);
            editLayout = (LinearLayout) view.findViewById(R.id.edit_layout);
            deleteLayout = (LinearLayout) view.findViewById(R.id.delete_layout);
        }
    }


    public CollegeAdapter(List<College> userItemDBList,Context context) {

        this.collegeList = userItemDBList;
        this.mContext = context;
    }


    @Override
    public CollegeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_text_holder, parent, false);
        return new CollegeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CollegeViewHolder holder, int position) {
        final College college = collegeList.get(position);

        holder.name.setText(college.getCollegeName());

        holder.age.setText(college.getCreated_at());
        holder.index.setText(Long.toString(college.getId()));

        holder.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof ListActivity) {
                   // ((ListActivity)mContext).presentAlert(college.getCollegeName(), college.getCreated_at(), Long.toString(college.getId()));
                    notifyDataSetChanged();
                }
            }
        });

        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentAlertDelete(holder.getAdapterPosition(),Long.toString(college.getId()));
            }
        });




    }

    public void updateUserList(List<College> newlist) {
        collegeList.clear();
        collegeList.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return collegeList.size();
    }

    public void presentAlertDelete(final int position, final String index){

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("Delete User")
                .setMessage("Are you sure to delete this user?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper db = new DatabaseHelper(mContext);
                        try {
                            db.deleteById(College.class, index);
                        }catch (SQLException ex){
                            Log.e("Adapter error: ",ex.toString());
                        }
                        deleteUser(position);
                    }
                })
                .setNegativeButton("No", null)
                .create();
        dialog.show();

    }

    private void deleteUser(int position) {
        collegeList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,collegeList.size());

    }


}
