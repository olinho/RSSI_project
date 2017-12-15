package reader;

public class Measurement {
	private String beaconId;
	private int rssi;
	private String time;
	
	public Measurement(String beaconId, int rssi) {
		this.beaconId = beaconId;
		this.rssi = rssi;
	}
	
	public Measurement(String beaconId, int rssi, String time) {
		this.beaconId = beaconId;
		this.rssi = rssi;
		this.time = time;
	}
	
	public String getBeaconId() {
		return beaconId;
	}
	public void setBeaconId(String beaconId) {
		this.beaconId = beaconId;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public void show() {
		System.out.println("Measurement: " + beaconId + "|" + rssi);
		
	}
	
	
}
