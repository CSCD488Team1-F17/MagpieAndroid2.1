package com.magpiehunt.magpie.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.magpiehunt.magpie.Helper.GPSTracker;
import com.magpiehunt.magpie.R;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoogleMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoogleMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//TODO handle denied permissions requests
//Fix map not reloading on reload of map fragment
public class GoogleMapFragment extends Fragment
        implements OnMapReadyCallback/*, LocationSource.OnLocationChangedListener*/ {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //private bool hasPosition, mapActive;
    private MenuItem addLocButton, saveLocButton;
    private float zoom;
    private ArrayList<MarkerOptions> marks;
    private ArrayList<Marker> markerList;
    private final int PERMISSION_REQUEST = 1;
    private GoogleMap gMap;
    //private FusedLocationProviderClient fusedLoc;
    private Location currLoc;
    private OnFragmentInteractionListener mListener;
    //private GoogleMapFragment mapFrag;
    private LatLng start = null;//curr loc
    GPSTracker gpsTracker;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * @return A new instance of fragment GoogleMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoogleMapFragment newInstance() {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        currLoc = new Location((LocationManager.GPS_PROVIDER));
        setHasOptionsMenu(true);


        Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle("Map View");

        ActivityCompat.requestPermissions(getActivity(),
                new java.lang.String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currLoc = location;
                start = new LatLng(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        LocationManager mLocationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
        }

        //fusedLoc = LocationServices.getFusedLocationProviderClient(getActivity());
        markerList = new ArrayList<Marker>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if(savedInstanceState != null){
            //do stuff if saved
        }
        else{
            zoom = -1;
            marks = new ArrayList<MarkerOptions>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {// Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        return v;
    }

    @Override
    public void onResume(){

        super.onResume();
        SupportMapFragment mapFrag = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        if(mapFrag == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFrag = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFrag).commit();
        }
        mapFrag.getMapAsync(this);
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap = googleMap;
        initMap();
    }

    private void initMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_REQUEST);
            if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                gMap.setMyLocationEnabled(true);
            }
            else{
                return;
            }
        }
        UiSettings mapSettings = gMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setRotateGesturesEnabled(true);
        if(zoom != -1){
            gMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
        for(MarkerOptions m: marks){
            markerList.add(gMap.addMarker(m));
        }

        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Location loc = getLocation();
                if(loc != null){moveToLocation(loc);}
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 200); //call functions in runn avter delay*/
        //int res = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(checkLocationPermission()/*res == PackageManager.PERMISSION_GRANTED*/) {
            Log.d("onResume OK", "");
            gpsTracker = new GPSTracker(getActivity());
            if (gpsTracker.canGetLocation()) {
                start = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                currLoc.setLatitude(start.latitude);
                currLoc.setLongitude(start.longitude);
                moveToLocation(currLoc);
                double meters = 150;
                double coef = meters * 0.0000089;
                //double new_lat = my_lat + coef;
                placeMarker(new LatLng(currLoc.getLatitude() + coef, currLoc.getLongitude()), "Test Marker");
            } else {
                Toast.makeText(getActivity(), "Permissions required to proceed.", Toast.LENGTH_SHORT).show();
                //finish();
                //gpsTracker.showSettingsAlert();
                //do something
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.add_location:
                addLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        switch(requestCode){
            case 1:{//request accepted
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    gpsTracker = new GPSTracker(getActivity());
                    if(gpsTracker.canGetLocation()){
                        start = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    }
                    else{
                        //permission denied
                    }
                    return;
                }
            }
            //other cases can check for other permissions i might need
        }
    }

    private void placeMarker(LatLng loc, String title){
        gMap.addMarker(new MarkerOptions().position(loc).title(title));
    }


    private void addLocation(){
        placeMarker(new LatLng(currLoc.getLatitude(), currLoc.getLongitude()), "New Loc");
    }

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
    public void onDestroyView(){
        super.onDestroyView();
        gpsTracker.stopUsingGPS();
        addLocButton.setVisible(false);
        saveLocButton.setVisible(false);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu){
       addLocButton = menu.findItem(R.id.add_location);
        saveLocButton = menu.findItem(R.id.save_locations);
        addLocButton.setVisible(true);
        saveLocButton.setVisible(true);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //helper functions==========================
    private boolean hasPermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    public  void typePressed(View v){
        int type = gMap.getMapType();
        if(type == GoogleMap.MAP_TYPE_NORMAL){
            gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if(type == GoogleMap.MAP_TYPE_TERRAIN){
            gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        else if(type == GoogleMap.MAP_TYPE_TERRAIN){
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void markPressed(View v){
        Location l = getLocation();
        LatLng coords = new LatLng(l.getLatitude(), l.getLongitude());
        marks.add(new MarkerOptions());
        marks.get(marks.size() - 1).position(coords);
        marks.get(marks.size() - 1).title("Mark " + marks.size());
        gMap.addMarker(marks.get(marks.size() - 1));
    }

    private Location getLocation(){
        LocationRequest lr = new LocationRequest();
        lr.setInterval(0);//0 means do asap
        lr.setFastestInterval(0);
        lr.setNumUpdates(1);//stop after one recieved
        lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return null;
    }

    private void moveToLocation(Location l){
        LatLng coords = new LatLng(l.getLatitude(), l.getLongitude());
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 14));
    }
}
