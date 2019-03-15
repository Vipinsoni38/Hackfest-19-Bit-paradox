package com.goldenboat.waymart.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.goldenboat.waymart.DataTypes.ProductDetails;
import com.goldenboat.waymart.R;
import java.util.ArrayList;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder> {

    ArrayList<ProductDetails> data;
    Context mctx;

    public CartRecyclerAdapter(Context mctx) {
        this.mctx = mctx;
    }

    @NonNull @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart, parent, false);

        return new CartViewHolder(v);
    }

    @Override public int getItemCount() {
        return data.size();
    }

    public void givedata(ArrayList<ProductDetails> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        holder.price.setText("\u20B9" + String.valueOf(data.get(position).getPrice()));

    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView cardImage;
        TextView title,price,count;

        public CartViewHolder(View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.object_title);
            price = itemView.findViewById(R.id.price);
            count = itemView.findViewById(R.id.quantity);

        }

    }

}
