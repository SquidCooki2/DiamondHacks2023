package com.example.diamondhacks2023;

import androidx.fragment.app.FragmentActivity;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.diamondhacks2023.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ArrayList<Address> votingPlaces;
    ArrayList<String> addressData;
    EditText search;
    ImageView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        votingPlaces = new ArrayList<>();
        addressData = new AddressData().data;
        sortAddress("MORRISVILLE");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        search = findViewById(R.id.search);
        searchButton = findViewById(R.id.search_button);
        mMap = googleMap;

        updateMarkers();

        searchButton.setOnClickListener(this::geoLocate);
    }

    private void geoLocate(View view) {
        String city = search.getText().toString();
        sortAddress(city.toUpperCase());
        updateMarkers();
    }

    private void updateMarkers() {
        mMap.clear();
        for (Address address : votingPlaces) {
            LatLng latLng = AddressLatLng(address.street + ", " + address.city + ", " + address.stateName + " " + address.zip);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    public LatLng AddressLatLng(String location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        android.location.Address address = addressList.get(0);

        return new LatLng(address.getLatitude(), address.getLongitude());
    }

    // TODO Figure out why file paths aren't working
    public void sortAddress(String city) {
        votingPlaces.clear();

        for (int i = 0; i < addressData.size(); i++) {
            String item = addressData.get(i);
            if (item.lastIndexOf("," + city + ",") != -1) {
                Scanner scanner = new Scanner(item);
                scanner.useDelimiter(",");

                for (int j = 0; j < 5; j++)
                    scanner.next();

                votingPlaces.add(new Address(scanner.next() + " " + scanner.next(), scanner.next(), scanner.next(), scanner.next()));
            }
        }
    }
}