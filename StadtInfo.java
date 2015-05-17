/*
 * Class to keep the information about
 * each city.
 */
package goeurotest;

/**
 *
 * @author João Luís M. Tavares
 */
public class StadtInfo {
    
    // The parameters
    private int _id;
    private String name;
    private String type;
    private float latitude;
    private float longitude;
    
    public StadtInfo(int _id, String name,
            String type, float latitude, float longitude) {
        
        this._id = _id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    
    }
    
    
    // Returning the _id
    public int getID() {
        return this._id;
    }
    
    // Returning the name
    public String getName() {
        return this.name;
    }
    
    // Returning the type
    public String getType() {
        return this.type;
    }
            
    // Returning the latitude
    public float getLat() {
        return this.latitude;
    }
    
    // Returning the longitude
    public float getLongit() {
        return this.longitude;
    }
}
