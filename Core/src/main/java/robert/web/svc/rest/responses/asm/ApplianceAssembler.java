package robert.web.svc.rest.responses.asm;

import robert.db.entity.Appliance;
import robert.web.svc.rest.responses.data.ApplianceDR;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ApplianceAssembler {

	public static List<ApplianceDR> convertToApplianceDR(Collection<Appliance> appliances) {
		return appliances.stream()
				.map(appliance -> {
					ApplianceDR applianceDR = new ApplianceDR();
					applianceDR.setName(appliance.getName());
					applianceDR.setId(appliance.getId());
					applianceDR.setReservations(
							ReservationAssembler.convertToReservationDR(appliance.getReservations())
					);
					return applianceDR;
				}).collect(Collectors.toList());
	}
}