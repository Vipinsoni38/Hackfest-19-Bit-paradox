package com.goldenboat.waymart.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goldenboat.waymart.DataTypes.ProductDetails;
import com.goldenboat.waymart.R;
import java.util.ArrayList;

public class Cart extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ProductDetails> data;
    CartRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = v.findViewById(R.id.recycler_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartRecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);
        data = new ArrayList<>();

        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        data.add(new ProductDetails());
        adapter.givedata(data);
        return v;
    }


}
