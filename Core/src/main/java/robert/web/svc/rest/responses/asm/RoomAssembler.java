package robert.web.svc.rest.responses.asm;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import robert.db.entity.Room;
import robert.web.svc.rest.responses.json.RoomDR;

public class RoomAssembler {

	public static List<RoomDR> convertToRoomDR(Collection<Room> rooms) {
		return rooms.stream()
				.map(room -> {
					RoomDR roomDR = new RoomDR();
					roomDR.setId(room.getId());
					roomDR.setNumber(room.getNumber());
					roomDR.setAppliances(ApplianceAssembler.convertToApplianceDR(
							room.getAppliances()
					));
					roomDR.setNumOfAppliances(room.getAppliances()
							.size());
					return roomDR;
				}).collect(Collectors.toList());
	}
}
