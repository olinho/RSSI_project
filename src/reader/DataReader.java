package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataReader {
	private BufferedReader bf = null;
	private String filename = "pomiary/Sala telewizyjna/5-pomiar-filtered.txt"; 
	private List<MeasurementSet> allSets = new ArrayList<>();
	final static int A = -77;
	final static double N = 1.30;
	
	public DataReader() throws FileNotFoundException {
		bf = new BufferedReader(new FileReader(filename));
		readDataFromFile();
	}
	
	public List<Measurement> getAllMeasurementForBeacon(String beaconId) {
		List<Measurement> measurements = new ArrayList<>();
		for (MeasurementSet set : allSets) {
			measurements.add(set.getMeasurementFromSetByBeaconId(beaconId));
		}
		return measurements;
	}
	
	public void readDataFromFile() {
		String line = "";
		MeasurementSet set = new MeasurementSet();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		
		try {
			String prevElTime = "";
			String prevElBeaconId = "";
			int prevElRssi;
			Date prevElDate = new Date();
			String [] elements;
			line = bf.readLine();
			
			do {
				elements = line.split("\\|");
				String time = elements[0];
				Date date = sdf.parse(time);
				String beaconId = elements[1];
				int rssi = Integer.valueOf(elements[2]);
				if (date.compareTo(prevElDate) != 0) {
					if (set.hasEnoughElements()) {
						allSets.add(set);
					}
					set = new MeasurementSet();
				}
				set.addMeasurement(new Measurement(beaconId, rssi));
				prevElDate = date;
				prevElBeaconId = beaconId;
				prevElRssi = rssi;
			} while ((line = bf.readLine()) != null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showAllSets() {
		for (MeasurementSet set : allSets) {
			set.show();
		}
	}
	
	public List<MeasurementSet> getAllSets(){
		return allSets;
	}
	
	public List<Measurement> getNextMeasurement() {
		List<Measurement> set = new ArrayList<>();
//		todo
		return set;
	}
}
