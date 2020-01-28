package com.example.dbprototypeapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.List;

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.CollegeViewHolder>{

    private List<College> collegeList;
    private Context mContext;


    public class CollegeViewHolder extends RecyclerView.ViewHolder {

        public TextView college_id, college_name, college_status, college_created_at;
        public ImageView editBtn, deleteBtn;

        public CollegeViewHolder(View view) {
            super(view);
            college_id = view.findViewById(R.id.college_id);
            college_name =  view.findViewById(R.id.college_name_value);
            college_status =  view.findViewById(R.id.college_status_value);
            college_created_at = view.findViewById(R.id.college_created_at_value);
            editBtn =  view.findViewById(R.id.edit_img);
            editBtn.setClickable(true);
            deleteBtn =  view.findViewById(R.id.delete_img);
            deleteBtn.setClickable(true);
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

        holder.college_name.setText(college.getCollegeName());
        holder.college_status.setText(college.getStatus()!= "0"? "Active": "Inactive");
        holder.college_created_at.setText(college.getCreated_at());
        holder.college_id.setText(Long.toString(college.getId()));

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentAlert(college.getStatus(), college.getCollegeName(), college.getCreated_at(), Long.toString(college.getId()));
                notifyDataSetChanged();

            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentAlertDelete(holder.getAdapterPosition(),college);
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

    public void presentAlertDelete(final int position, final College obj){

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("Delete User")
                .setMessage("Are you sure to delete this user?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper db = new DatabaseHelper(mContext);
                        try {
                            db.deleteById(College.class, obj);
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

    public void presentAlert(String isChecked,String name, String age, final String index){
        final EditText collegeName = new EditText(mContext);
        final CheckBox collegeStatus = new CheckBox(mContext);

        collegeName.setText(name);
        collegeStatus.setText(age);
        collegeName.setWidth(100);
        collegeStatus.setWidth(100);
        collegeStatus.setText("Status");
        collegeStatus.setChecked(isChecked != "0"?true:false);

        final LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(collegeName);
        linearLayout.addView(collegeStatus);


        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("Edit User")
                .setView(linearLayout)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper db = new DatabaseHelper(mContext);
                        College college = new College();
                        college.setCollegeName(collegeName.getText().toString());
                        college.setId(Integer.parseInt(index));
                        college.setStatus(collegeStatus.isChecked() ? "1":"0");
                        college.setCreatedAt(db.getCurrentTimeStamp());


                        try {
                            db.createOrUpdate(college);
                            List<College> collegeHolderList = db.getAll(College.class);
                            updateUserList(collegeHolderList);
                        }catch (SQLException ex){
                            Log.e("DB error Updating", ex.toString());
                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

}
