package robert.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import robert.db.entity.Reservation;
import robert.db.repository.ReservationRepository;
import robert.enums.TaskType;
import robert.jobs.api.JobRegistration;
import robert.svc.api.TaskSchedulerService;
import robert.utils.api.AppLogger;

import java.util.Date;

@Component
public class JobRegistrationImpl implements JobRegistration {

	private final AppLogger log;

	private final TaskSchedulerService taskScheduler;

	private final ReservationRepository reservationRepository;

	@Autowired
	public JobRegistrationImpl(AppLogger log, TaskSchedulerService taskScheduler, ReservationRepository reservationRepository) {
		this.log = log;
		this.taskScheduler = taskScheduler;
		this.reservationRepository = reservationRepository;
	}

	@Override
	public void run(String... strings) throws Exception {
		log.info("Registration of new jobs.");
		taskScheduler.submitNewTask(oldReservationsCleaningJob());
	}

	private ExecutableTask oldReservationsCleaningJob() {
		return ExecutableTask.newBuilder()
				.withTaskType(TaskType.PERIODICAL_JOB)
				.withThreadSleep(1000 * 60 * 15)
				.withTask(() -> {
					Iterable<Reservation> allReservations = reservationRepository.findAll();
					long currentTime = new Date().getTime();
					allReservations.forEach(reservation -> {
						if (reservation.getValidTill() < currentTime) {
							log.debug("Remove old reservation:", reservation);
							reservationRepository.delete(reservation);
						}
					});
					log.debug("Old reservations cleaning done.");
				})
				.build();
	}
}
