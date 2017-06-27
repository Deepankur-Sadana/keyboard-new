
package com.vingeapp.android.googleLocationApiResponse;

import java.util.List;

public class Result {

    private List<Address_component> address_components = null;
    private String formatted_address;
    private Geometry geometry;
    private String place_id;
    private List<String> types = null;

    public List<Address_component> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<Address_component> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}
