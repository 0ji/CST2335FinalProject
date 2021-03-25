package com.cst2335.cst2335finalproject;

/**
 * CarItem class to hold a car object and it's details.
 */
public class CarItem {
    private int _id;
    private int makeID;
    private String make;
    private String model;

    public CarItem(int _id, int makeID, String make, String model) {
        this._id = _id;
        this.makeID = makeID;
        this.make = make;
        this.model = model;
    }

    public int get_id() {
        return _id;
    }

    public int getMakeID() {
        return makeID;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
}
