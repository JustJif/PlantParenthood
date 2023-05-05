package com.example.plantparenthood;

import androidx.recyclerview.widget.RecyclerView;

public abstract class AbstractCreatorAdapter extends RecyclerView.Adapter
{
    public void notifyChange(int plantLocation)
    {
        notifyItemChanged(plantLocation);
    }
}
