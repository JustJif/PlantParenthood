package com.example.plantparenthood;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupAdapter extends AbstractCreatorAdapter
{
    private ArrayList<Group> groupList;
    private Context openActivity;

    private DatabaseHandler databaseHandler;

    public GroupAdapter(ArrayList<Group> newGroupList, Context newContext)
    {
        groupList = newGroupList;
        openActivity = newContext;
        databaseHandler = DatabaseHandler.getDatabase(newContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_square, parent, false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(currentView) {};
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Group thisGroup = groupList.get(position);

        holder.itemView.setOnClickListener(view -> setupPopup(view, thisGroup));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    private void setupPopup(View view, Group thisGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext()
                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View newPopup = layoutInflater.inflate(R.layout.activity_group_popup, null);

        PopupWindow newPopupWindow = new PopupWindow(newPopup, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        newPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView GroupName = newPopup.findViewById(R.id.GroupName);
        GroupName.setText(thisGroup.getGroupName());

        Button closeButton = newPopup.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPopupWindow.dismiss();
            }
        });
    }
}
