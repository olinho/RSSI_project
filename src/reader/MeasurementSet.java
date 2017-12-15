package reader;

import java.util.ArrayList;
import java.util.List;

public class MeasurementSet {
	private List<Measurement> set = new ArrayList<>();
	
	public MeasurementSet() {
		
	}
	
	public MeasurementSet(List<Measurement> set) {
		this.set = set;
	}
	
	public void clearSet() {
		set.clear();
	}
	
	public void addMeasurement(Measurement m) {
		String beaconId = m.getBeaconId();
		if (isBeaconInSet(beaconId)) {
			updateMeasurement(m);
		} else {
			set.add(m);
		}
	}
	
	public void updateMeasurement(Measurement m) {
		String beaconId = m.getBeaconId();
		int index = getMeasurementIndexByBeaconId(beaconId);
		set.get(index).setRssi(m.getRssi());
	}
	
	public boolean hasEnoughElements() {
		return  getSize() == 3 ? true : false;
	}
	
	public int getSize() {
		return set.size();
	}
	
	public void removeMeasurementForBeacon(String beaconId) {
		for (Measurement m : set) {
			if (m.getBeaconId().equals(beaconId))
				set.remove(m);
		}
	}
	
	public boolean isBeaconInSet(String beaconId) {
		for (Measurement m : set) {
			if (m.getBeaconId().equals(beaconId))
				return true;
		}
		return false;
	}
	
	public int getMeasurementIndexByBeaconId(String beaconId){ 
		for (Measurement measurement : set) {
			if (measurement.getBeaconId().equals(beaconId))
				return set.indexOf(measurement);
		}
		return -1;
	}
	
	public Measurement getMeasurementFromSetByBeaconId(String beaconId) {
		for (Measurement measurement : set) {
			if (measurement.getBeaconId().equals(beaconId))
				return measurement;
		}
		return null;
	}
	
	public void show() {
		System.out.println("MeasurementSet");
		for (Measurement m : set) {
			m.show();
		}
		
	}
}
