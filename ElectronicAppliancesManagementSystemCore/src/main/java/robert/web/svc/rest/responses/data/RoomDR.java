package robert.web.svc.rest.responses.data;

import java.util.List;

public class RoomDR {

	private String number;

	private int numOfAppliances = 0;

	private List<ApplianceDR> appliances;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getNumOfAppliances() {
		return numOfAppliances;
	}

	public void setNumOfAppliances(int numOfAppliances) {
		this.numOfAppliances = numOfAppliances;
	}

	public List<ApplianceDR> getAppliances() {
		return appliances;
	}

	public void setAppliances(List<ApplianceDR> appliances) {
		this.appliances = appliances;
	}
}
