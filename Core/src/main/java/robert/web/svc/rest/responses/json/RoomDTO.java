package robert.web.svc.rest.responses.json;

import java.util.List;

public class RoomDTO {

	private long id;

	private String number;

	private int numOfAppliances = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private List<ApplianceDTO> appliances;

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

	public List<ApplianceDTO> getAppliances() {
		return appliances;
	}

	public void setAppliances(List<ApplianceDTO> appliances) {
		this.appliances = appliances;
	}
}
