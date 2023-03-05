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
    private ArrayList<Address> votingPlaces;
    private ArrayList<String> addressData;
    private EditText search;
    private ImageView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        votingPlaces = new ArrayList<>();
        addressData = new AddressData().data;
        sortNearbyAddress(new LatLng(35.81495928321916, -78.86485158659445));

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        search = findViewById(R.id.search);
        searchButton = findViewById(R.id.search_button);

        updateMarkers();

        searchButton.setOnClickListener(this::geoLocate);
    }

    private void geoLocate(View view) {
        String city = search.getText().toString().trim();
        sortAddress(city.toUpperCase());
        updateMarkers();
    }

    private void updateMarkers() {
        mMap.clear();
        for (Address address : votingPlaces) {
            LatLng latLng = AddressLatLng(address.toString());
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    /**
     * Converts String representation of {@link com.example.diamondhacks2023.Address} to {@link com.google.android.gms.maps.model.LatLng}
     */
    private LatLng AddressLatLng(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> posList;
        try {
            posList = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        android.location.Address pos = posList.get(0);

        return new LatLng(pos.getLatitude(), pos.getLongitude());
    }

    /**
     * Converts {@link com.google.android.gms.maps.model.LatLng} to {@link android.location.Address}
     */
    private android.location.Address LatLngAddress(LatLng pos) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addressList;
        try {
            addressList = geocoder.getFromLocation(pos.latitude, pos.longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return addressList.get(0);
    }

    // TODO Figure out why file paths aren't working
    private void sortAddress(String location) {
        votingPlaces.clear();

        for (String item : addressData) {
            if (item.lastIndexOf(location) != -1) {
                Scanner scanner = new Scanner(item);
                scanner.useDelimiter(",");

                for (int j = 0; j < 5; j++)
                    scanner.next();

                votingPlaces.add(new Address(scanner.next() + " " + scanner.next(), scanner.next(), scanner.next(), scanner.next()));
            }
        }
    }

    private void sortNearbyAddress(LatLng pos) {
        votingPlaces.clear();
//        String locality = LatLngAddress(pos).getLocality();

        for (String item : addressData) {
            if (item.lastIndexOf(",CARY,") != -1) {
                Scanner scanner = new Scanner(item);
                scanner.useDelimiter(",");

                for (int j = 0; j < 5; j++)
                    scanner.next();

                Address address = new Address(scanner.next() + " " + scanner.next(), scanner.next(), scanner.next(), scanner.next());
                if (MapsMath.distLatLng(pos, AddressLatLng(address.toString())) * 10000000 < 2) {
                    votingPlaces.add(address);
                }
            }
        }
    }
}