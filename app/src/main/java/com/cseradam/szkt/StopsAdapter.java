package com.cseradam.szkt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.resources.TextAppearanceFontCallback;

import java.util.ArrayList;

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.ViewHolder> implements Filterable {
    private ArrayList<Stop> stopsAll;
    private ArrayList<Stop> stops;
    private Context mContext;
    private int lastPosition = -1;

    public StopsAdapter(Context mContext, ArrayList<Stop> stops) {
        this.stops = stops;
        this.stopsAll = stops;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_stop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StopsAdapter.ViewHolder holder, int position) {
        Stop currentItem = stops.get(position);
        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.stop_item_animation);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    @Override
    public Filter getFilter() {
        return stopFilter;
    }

    private Filter stopFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Stop> filteredList = new ArrayList<>();

            if (constraint.length() == 0 || constraint == null) {
                results.count = stopsAll.size();
                results.values = stopsAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Stop stop : stopsAll) {
                    if (stop.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(stop);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            stops = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView stop_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stop_name = itemView.findViewById(R.id.stop_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Stop clickedStop = stops.get(position);
                        Intent intent = new Intent(mContext, Lines_Activity.class);
                        intent.putExtra("stop_name", clickedStop.getName());
                        mContext.startActivity(intent);
                    }
                }
            }); // Ez a zárójel volt a hiányzó
        }

        public void bindTo (Stop currentItem) {
            stop_name.setText(currentItem.getName());
        }
    }

}
