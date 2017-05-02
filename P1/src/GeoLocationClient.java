
public class GeoLocationClient 
{

	public static void main(String[] args) 
	{
		//calls the GL constructor to create location objects
		GeoLocation stash = new GeoLocation(34.988889,-106.614444);
		GeoLocation studio = new GeoLocation(34.989978,-106.614357);
		GeoLocation fbi = new GeoLocation(35.131281,-106.61263);
		
		//prints out using the objects toString method
		System.out.println("the stash is at" + stash.toString());
		System.out.println("ABQ studio is at" + studio.toString());
		System.out.println("FBI building is at" + fbi.toString());
		

		//calls the distance from to calculate the distance using its lat/long data
		System.out.println("distance in miles between: ");
		System.out.println("       stash/studio = " + stash.distanceFrom(studio));
		System.out.println("       stash/fbi = " + stash.distanceFrom(fbi));
	}

}
