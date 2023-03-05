package com.example.diamondhacks2023;

import androidx.fragment.app.FragmentActivity;
import android.location.Geocoder;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        votingPlaces = new ArrayList<>();
        sortAddress();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
    public void sortAddress() {
        AddressData addressData = new AddressData();
        ArrayList<String> data = addressData.data;

        for (int i = 0; i < data.size(); i++) {
            String item = data.get(i);
            if (item.lastIndexOf(",CARY,") != -1) {
                Scanner scanner = new Scanner(item);
                scanner.useDelimiter(",");

                for (int j = 0; j < 5; j++)
                    scanner.next();

                votingPlaces.add(new Address(scanner.next() + " " + scanner.next(), scanner.next(), scanner.next(), scanner.next()));
            }
        }
    }
}