package robert.web.svc.rest.responses.asm;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import robert.db.entity.Appliance;
import robert.web.svc.rest.responses.json.ApplianceDTO;

public class ApplianceAssembler {

	public static List<ApplianceDTO> convertToApplianceDTO(Collection<Appliance> appliances) {
		return appliances.stream()
				.map(appliance -> {
					ApplianceDTO applianceDTO = new ApplianceDTO();
					applianceDTO.setName(appliance.getName());
					applianceDTO.setId(appliance.getId());
					applianceDTO.setReservations(ReservationAssembler.convertToReservationDTO(appliance.getReservations())
					);
					return applianceDTO;
				}).collect(Collectors.toList());
	}
}
