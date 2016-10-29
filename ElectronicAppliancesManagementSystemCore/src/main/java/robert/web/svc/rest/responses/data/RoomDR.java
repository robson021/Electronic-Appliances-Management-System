package robert.web.svc.rest.responses.data;

import java.util.List;

public class RoomDR {

	private String number;

	private List<ApplianceDR> appliances;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<ApplianceDR> getAppliances() {
		return appliances;
	}

	public void setAppliances(List<ApplianceDR> appliances) {
		this.appliances = appliances;
	}
}
