package com.magpiehunt.magpie.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magpiehunt.magpie.Adapters.CollectionAdapter;
import com.magpiehunt.magpie.Database.MagpieDatabase;
import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.R;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends Fragment {

    private static final String TAG = "CollectionFragment";

    protected RecyclerView mRecyclerView;
    protected CollectionAdapter mModelAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<Collection> mDataset;
    protected MagpieDatabase magpieDatabase;

    private OnFragmentInteractionListener mListener;

    public CollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_my_collections, container, false);
        rootView.setTag(TAG);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        mModelAdapter = new CollectionAdapter(mDataset);
        // Set the adapter for RecyclerView.
        mRecyclerView.setAdapter(mModelAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //TODO: restore state of fragment
        }//end if
    }//end

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //TODO: saved state of fragment here
    }//end

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }//end if

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }//end

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    // TODO: Replace the test data within this with data from room DB
    private void initializeData() {
        mDataset = new ArrayList<>();

        Collection testCollection = new Collection();
        testCollection.setName("Test Walk Talk");
        testCollection.setAbbreviation("TWT");
        testCollection.setDescription("MUAHAHAHA the card has expanded and i am the description that lies within it!");
        mDataset.add(testCollection);

        testCollection = new Collection();
        testCollection.setName("Walk Test Mag");
        testCollection.setAbbreviation("WTM");
        testCollection.setDescription("A wild description has appeared!\nFlee?\tFight?");
        mDataset.add(testCollection);

        testCollection = new Collection();
        testCollection.setName("Card Walk Test");
        testCollection.setAbbreviation("CWT");
        testCollection.setDescription("I no longer like cards. these are a bitch to work with.");
        mDataset.add(testCollection);

        testCollection = new Collection();
        testCollection.setName("Walk onthe Wildside");
        testCollection.setAbbreviation("WOW");
        testCollection.setDescription("Test description");
        mDataset.add(testCollection);

        testCollection = new Collection();
        testCollection.setName("No Skipping Allowed");
        testCollection.setAbbreviation("NSA");
        testCollection.setDescription("Lets\nTest\nA\nBunch\nOf\nNew\nLines");
        mDataset.add(testCollection);

        testCollection = new Collection();
        testCollection.setName("Cards Are Easy");
        testCollection.setAbbreviation("CAE");
        testCollection.setDescription("lets test a long string with no newlines and see how this is delimited i hope it goes smoothly but i guess we'll find out here goes nothing");
        mDataset.add(testCollection);

        testCollection = new Collection();
        testCollection.setName("Walk With Me");
        testCollection.setAbbreviation("WWM");
        testCollection.setDescription("Walk With Me Description");
        mDataset.add(testCollection);

        testCollection = new Collection();
        testCollection.setName("The Last Walk");
        testCollection.setAbbreviation("TLW");
        testCollection.setDescription("This was the last walk... Go inside and play some video games or something, loser.");
        mDataset.add(testCollection);
    }//end
}
