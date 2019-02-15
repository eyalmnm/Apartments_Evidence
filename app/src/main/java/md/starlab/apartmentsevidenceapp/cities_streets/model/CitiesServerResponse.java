package md.starlab.apartmentsevidenceapp.cities_streets.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CitiesServerResponse {

    @SerializedName("CityWithStreets")
    private ArrayList<CityData> cities;

    public CitiesServerResponse(ArrayList<CityData> cities) {
        this.cities = cities;
    }

    public ArrayList<CityData> getCities() {
        return cities;
    }
}
