package com.cst2335.cst2335finalproject.carDB;

/**
 * CarItem class to hold a car object and it's details.
 */
public class CarItem {
    private int _id;
    private int makeID;
    private int modelID;
    private String make;
    private String model;

    /**
     * public constructor for caritem.
     * @param _id id
     * @param makeID make id
     * @param modelID model id
     * @param make make
     * @param model model id
     */
    public CarItem(int _id, int makeID, int modelID, String make, String model) {
        this._id = _id;
        this.makeID = makeID;
        this.modelID = modelID;
        this.make = make;
        this.model = model;
    }

    /**
     * returns id
     * @return id
     */
    public int get_id() {
        return _id;
    }

    /**
     * returns make id
     * @return make id
     */
    public int getMakeID() {
        return makeID;
    }

    /**
     * returns model id
     * @return model id
     */
    public int getModelID() { return modelID; }

    /**
     * returns make / brand name
     * @return make/brand name
     */
    public String getMake() {
        return make;
    }

    /**
     * returns model name
     * @return model name
     */
    public String getModel() {
        return model;
    }
}
