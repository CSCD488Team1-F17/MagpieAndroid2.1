package com.magpiehunt.magpie.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magpiehunt.magpie.Entities.Collection;
import com.magpiehunt.magpie.R;

import java.util.List;

/**
 * Created by Blake Impecoven on 1/22/18.
 * TODO:
 * - setup listeners for deletion and expansion of the cards.
 *      - tap or swipe to delete?
 * - setup expansion functionality of the cards.
 *      - make any corresponding changes to collection_cardn_card.xml.
 *      - make any corresponding changes to CollectionModel.java.
 * - setup data set for testing of the cards.
 *      - setup dummy data set for testing sooner (waiting on room data).
 * - tweak collection_card.xmlrd.xml font/expansion arrow.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionHolder> {

    private static final String TAG = "LandmarkAdapter";

    private List<Collection> collectionList;

    public class CollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "CollectionHolder";

        private int position;

        // We may need to add more fields here for expanding of the cards.
        // fields for CardView (Condensed)
        private TextView collectionTitle;
        private TextView collectionAbbreviation;
        private ImageView imgThumb, imgArrow;
        private Collection currentObject;

        // fields for CardView (Expanded)
        private TextView description;
        private TextView rating;


        public CollectionHolder(View itemView) {
            super(itemView);
            this.collectionTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            this.collectionAbbreviation = (TextView)itemView.findViewById(R.id.tvAbbreviation);
            this.imgThumb = (ImageView)itemView.findViewById(R.id.img_thumb);
            this.imgArrow = (ImageView)itemView.findViewById(R.id.expandArrow);
        }//end DVC

        public void setData(Collection currentObject, int position) {
            // fields for CardView (Condensed)
            this.collectionTitle.setText(currentObject.getName());
            this.collectionAbbreviation.setText(currentObject.getAbbreviation());
            // use the following line once images are in the DB. for now, we will use a dummy.
//            this.imgThumb.setImageResource(currentObject.getImage());
            this.imgThumb.setImageResource(R.drawable.magpie_test_cardview_collectionimage);

            // fields for CardView (Expanded)
//            this.description.setText(currentObject.getDescription());
//            this.rating = currentObject.getRating();

            this.position = position;
            this.currentObject = currentObject;

//            setListeners(); // uncomment when click functionality implemented.
        }//end setData

        public void setListeners() {
            imgArrow.setOnClickListener(CollectionHolder.this);
            //TODO: change this listener to respond to a click of the whole card?
            imgThumb.setOnClickListener(CollectionHolder.this);
        }//end setListeners

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.expandArrow:
                    //TODO: implement drop-down functionality
                    break;

                case R.id.img_thumb:
                    //TODO: implement opening the collection (view landmarks)
                    break;

                //TODO: implement deletion below.
//                case delete item:
//                    removeItem(position);
//                    break;

                default:
                    break;
            }//end switch
        }//end onClick

        // will be used at some point.
        //TODO: decide on gesture or button removal.
        public void removeItem(int position) {
            collectionList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, collectionList.size());
        }//end removeItem

        public void addItem(int position, Collection currentObject) {
            collectionList.add(position, currentObject);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, collectionList.size());
        }//end addItem

    }//end inner class: CollectionHolder

    public CollectionAdapter(List<Collection> collectionList) {
        this.collectionList = collectionList;
    }//end DVC

    // Create new views (invoked by the layout manager)
    @Override
    public CollectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_card, parent, false);

        return new CollectionHolder(view);
    }//end onCreateViewHolder

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CollectionHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        holder.setData(collectionList.get(position), position);
    }//end onBindViewHolder

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

}//end CollectionAdapter
