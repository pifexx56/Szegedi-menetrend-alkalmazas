package com.cseradam.szkt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineViewHolder> {
    private ArrayList<Line> lines;
    private Context mContext;

    public LineAdapter(Context mContext, ArrayList<Line> lines) {
        this.lines = lines;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LineViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_route, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineViewHolder holder, int position) {
        Line currentLine = lines.get(position);
        holder.bindTo(currentLine);
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

    class LineViewHolder extends RecyclerView.ViewHolder {
        TextView line_number;
        TextView line_terminus;
        TextView line_times;

        public LineViewHolder(@NonNull View itemView) {
            super(itemView);
            line_number = itemView.findViewById(R.id.route_number);
            line_terminus = itemView.findViewById(R.id.route_destination);
            line_times = itemView.findViewById(R.id.route_times);
        }

        public void bindTo(Line currentLine) {
            line_number.setText(currentLine.getNumber());
            line_terminus.setText(currentLine.getTerminusz());
            line_times.setText(currentLine.getTimes().toString());
            line_times.setSelected(true);
        }
    }
}
