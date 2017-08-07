package com.example.eviat.mines.Fragments;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.eviat.mines.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static MapView mMapView;
    private static GoogleMap mGoogleMap;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mapView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView)mapView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try{
            MapsInitializer.initialize((getActivity().getApplicationContext()));
        }catch(Exception e){
            Log.e("map", e.getMessage());
        }

        mMapView.getMapAsync(this);

        return mapView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    mGoogleMap = googleMap;

        if (
                (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED) &&

                (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED)
                ) {
            return;
        }

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);



    }

    public static String getLocationAddress(double lat, double lng){

        Geocoder gc = new Geocoder(mMapView.getContext(), Locale.getDefault());
        List<Address> list = null;

        try {
            list = gc.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list == null || list.size() == 0)
            return "";

        Address address = list.get(0);

        return address.getAddressLine(0);


    }

    public static void makeMarker(double lat, double lng, String name, String score){

        if (mGoogleMap != null) {
            mGoogleMap.addMarker(new MarkerOptions().title("Name: "+name+" Score: "+score)
                    .position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker()));
        }
    }

    public static void zoomIn(double lat, double lng){

        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
        }

    public static void clearMap(){
        if (mGoogleMap != null)
        mGoogleMap.clear();
    }


}
