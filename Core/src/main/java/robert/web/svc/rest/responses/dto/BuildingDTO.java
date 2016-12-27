package robert.web.svc.rest.responses.dto;

import java.util.List;

public class BuildingDTO {

	private String name;

	private List<RoomDTO> rooms;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RoomDTO> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomDTO> rooms) {
		this.rooms = rooms;
	}
}
