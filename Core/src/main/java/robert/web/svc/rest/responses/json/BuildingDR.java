package robert.web.svc.rest.responses.json;

import java.util.List;

public class BuildingDR {

	private String name;

	private List<RoomDR> rooms;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RoomDR> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomDR> rooms) {
		this.rooms = rooms;
	}
}
