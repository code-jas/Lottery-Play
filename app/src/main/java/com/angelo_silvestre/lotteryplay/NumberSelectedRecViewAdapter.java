package com.angelo_silvestre.lotteryplay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class NumberSelectedRecViewAdapter  extends RecyclerView.Adapter<NumberSelectedRecViewAdapter.ViewHolder>{

    private ArrayList<BettingCard> cards = new ArrayList<>();
    private Context context;

    public NumberSelectedRecViewAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<BettingCard> getContacts() {
        return cards;
    }
    public void setContacts(ArrayList<BettingCard> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bet_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


       String selectedNumbers = Arrays.toString(cards.get(position).getCard().toArray()).substring(1).replaceFirst("]", "").replace(", ", ", ");
        Log.d("Tag",
                selectedNumbers);


        holder.txtSelectedNumbers.setText(selectedNumbers);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,cards.get(position).getCard() +  "", Toast.LENGTH_SHORT).show();
            }
        });

//        Glide.with(context)
//                .asBitmap()
//                .load(contacts.get(position).getImageUrl())
//                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtSelectedNumbers;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            parent.setElevation(0);


            txtSelectedNumbers = itemView.findViewById(R.id.txtSelectedNumbers);
        }
    }



}
