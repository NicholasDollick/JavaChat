
public class PlaceInformation 
{
	private String name, address, tag;
	private double latitude, longitude;

	
	//Class constructor
	public PlaceInformation(String name, String address, String tag, double latitude, double longitude)
	{
		this.name = name;
		this.address = address;
		this.tag = tag;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getAddress()
	{
		return this.address;
	}
	
	public String getTag()
	{
		return this.tag;
	}
	
	public String toString()
	{
		return (this.name + "(" + this.address + ", " + this.latitude + ", " + this.longitude + ")");
	}
	
	//creates and returns a refrence to a GL object, using PI info
	public GeoLocation getLocation()
	{
		GeoLocation spot = new GeoLocation(this.latitude, this.longitude);
		return spot;
	}
	
	public double distanceFrom(GeoLocation spot)
	{
		double distance;
		GeoLocation place = this.getLocation(); //creates a GL object using the info from calling object
		
		distance = place.distanceFrom(spot); //calls the GL distance from comparing the new object to the desired obj
		
		return distance; //returns the distance
	}
}
