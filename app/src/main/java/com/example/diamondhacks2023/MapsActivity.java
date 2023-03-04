package com.example.diamondhacks2023;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.diamondhacks2023.databinding.ActivityMapsBinding;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void sortAddress() throws FileNotFoundException{
        ArrayList <Address> VotingPlacesInCity = new ArrayList<Address>();
        File places = new File("VotingPlaces.txt");
        Scanner sc = new Scanner(places);
        String street = "";
        String zip = "";
        String city = "";
        String state = "";
        String csvAddress = "";
        while (sc.hasNextLine()) {
            String fullLine = sc.nextLine();
            if (fullLine.indexOf("WAKE") == (fullLine.indexOf(",") + 1)) {
                int count = 0;
                for (int i = 0; i < fullLine.length(); i++) {
                    if (fullLine.charAt(i) == ',') {
                        count++;
                    }
                    if (count == 7) {
                        if (fullLine.lastIndexOf("RALEIGH") == (i + 1)) {
                            int counter = 0;
                            for (int j = 0; j < fullLine.length(); j++) {
                                if (fullLine.charAt(j) == ',') {
                                    counter++;
                                }
                                if (counter == 5) {
                                    csvAddress = fullLine.substring(j + 1);
                                }
                                break;
                            }
                            String[] stringArray = csvAddress.split(",");
                            street = stringArray[0] + " " + stringArray[1];
                            zip = stringArray[4];
                            city = stringArray[2];
                            state = stringArray[3];
                            Address address = new Address(street,city,state,zip);
                            VotingPlacesInCity.add(address);
                        }
                    }
                }
            }
        }
    }
}