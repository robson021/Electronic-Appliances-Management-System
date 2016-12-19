package robert.web.svc.rest.responses.asm;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import robert.db.entity.Room;
import robert.web.svc.rest.responses.json.RoomDTO;

public class RoomAssembler {

	public static List<RoomDTO> convertToRoomDTO(Collection<Room> rooms) {
		return rooms.stream()
				.map(room -> {
					RoomDTO roomDTO = new RoomDTO();
					roomDTO.setId(room.getId());
					roomDTO.setNumber(room.getNumber());
					roomDTO.setAppliances(ApplianceAssembler.convertToApplianceDTO(
							room.getAppliances()
					));
					roomDTO.setNumOfAppliances(room.getAppliances()
							.size());
					return roomDTO;
				}).collect(Collectors.toList());
	}
}
