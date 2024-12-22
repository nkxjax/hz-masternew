package com.example.finalhomework.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.finalhomework.R;

import java.util.ArrayList;

public class KnowJiuZhaiItemFragment_4_Culture extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_know_jiu_zhai_item_4__culture, container, false);

        // Find ListView by its ID
        ListView listView = rootView.findViewById(R.id.listView_smartTravel);

        // Sample data for the ListView
        ArrayList<String> items = new ArrayList<>();
        items.add("1");
        items.add("2");
        items.add("3");
        items.add("4");

        // Create an ArrayAdapter to display the data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

        return rootView;
    }
}