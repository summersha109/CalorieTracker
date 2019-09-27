package com.example.calorietracker;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class DisplayMapFragment extends Fragment implements OnMapReadyCallback{
    View vDisplayMap;
    GoogleMap mMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        vDisplayMap = inflater.inflate(R.layout.fragment_map, container,false);
        SupportMapFragment mapf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fmap);
        mapf.getMapAsync(this);
        return vDisplayMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getActivity().getIntent();;
        Bundle bundle = intent.getExtras();
        String address = bundle.getString("address");
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(address,5);
            if(list != null && list.size() >0){
                LatLng latLng = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
                SearchLocation searchLocation = new SearchLocation();
                searchLocation.execute();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class SearchLocation extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids){

            List<String> location =  FourSquare.getLocation(FourSquare.search(
                    new String[]{"query","ll","radius","limit"},
                    new String[]{"park","-37.877, 145.045","5000","50"}
                    )
            );
            return location;
        }

        @Override
        protected void onPostExecute(List<String> location){

            for (int i = 0; i < location.size(); i++) {
                String[] loc = location.get(i).split(",");
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(loc[0]),Double.valueOf(loc[1]))).title("Park"+" "+i).snippet("This is a park!")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
        }
    }

}
