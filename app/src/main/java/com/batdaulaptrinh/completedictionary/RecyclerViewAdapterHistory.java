package com.batdaulaptrinh.completedictionary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.HistoryViewHolder> {
    private ArrayList<History> histories;
    private Context context;

    public RecyclerViewAdapterHistory(Context context, ArrayList<History> histories) {
        this.histories = histories;
        this.context = context;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView enWord;
        TextView enDef;
        ImageButton favourite;


        public HistoryViewHolder(View v) {
            super(v);
            enWord = v.findViewById(R.id.text_word);
            enDef = v.findViewById(R.id.text_definition);
            favourite = v.findViewById(R.id.changeFavourite);

            enWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String enWord = histories.get(position).get_en_word();

                    Intent intent = new Intent(context, WordInfoActivity.class);
                    intent.putExtra("en_word", enWord);
                    intent.putExtra("is_online","true");
                    context.startActivity(intent);
                }
            });

            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int is_favourite = histories.get(position).get_is_favourite();
                    if (is_favourite == 0) {
                        histories.get(position).setIs_favourite(1);
                        favourite.setImageResource(R.drawable.colored_star);
                        HistoryActivity.changeFavourite(histories.get(position).get_en_word(),1);
                    }else{
                        histories.get(position).setIs_favourite(0);
                        favourite.setImageResource(R.drawable.empty_star);
                        HistoryActivity.changeFavourite(histories.get(position).get_en_word(),0);
                    }
                }
            });
        }
    }

    @NonNull
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        holder.enWord.setText(histories.get(position).get_en_word());
        holder.enDef.setText(histories.get(position).get_def());
        if(histories.get(position).get_is_favourite() ==1){
            holder.favourite.setImageResource(R.drawable.colored_star);
        }else{
            holder.favourite.setImageResource(R.drawable.empty_star);

        }
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}

