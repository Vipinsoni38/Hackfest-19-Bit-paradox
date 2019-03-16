package com.goldenboat.waymart.Adapters;

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
    int[] draw;
    public CartRecyclerAdapter(Context mctx) {
        this.mctx = mctx;
        draw=new int[6];
        draw[0]=R.drawable.bournvita;
        draw[1]=R.drawable.charger;
        draw[2]=R.drawable.pen;
        draw[3]=R.drawable.silk;
        draw[4]=R.drawable.sunglasses;
        draw[5]=R.drawable.toothpaste;
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
        holder.count.setText(String.valueOf(data.get(position).getCount()));
        holder.price.setText("\u20B9" + String.valueOf(data.get(position).getPrice()));
        holder.title.setText(data.get(position).getProduct_title());

        if (data.get(position).getPic_url()==null || data.get(position).getPic_url().equals("")||
                data.get(position).getPic_url().equals("null") ) {
            Glide.with(mctx).load(draw[position*2%5]).into(holder.cardImage);
        } else {
            Glide.with(mctx).load(data.get(position).getPic_url()).into(holder.cardImage);
        }

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
