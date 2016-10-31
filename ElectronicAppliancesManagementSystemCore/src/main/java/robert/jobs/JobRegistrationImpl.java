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

import static robert.enums.BeanNames.DEFAULT_JOB_REGISTARTION;

@Component(DEFAULT_JOB_REGISTARTION)
public class JobRegistrationImpl implements JobRegistration {

	private final AppLogger log;

	private final TaskSchedulerService taskScheduler;

	private final ReservationRepository reservationRepository;

	@Autowired
	public JobRegistrationImpl(AppLogger log, TaskSchedulerService taskScheduler, ReservationRepository reservationRepository) {
		this.log = log;
		this.taskScheduler = taskScheduler;
		this.reservationRepository = reservationRepository;

		this.registerAllJobs();
	}

	@Override
	public void registerAllJobs() {
		log.info("Registration of new jobs.");
		taskScheduler.submitNewTask(oldReservationsCleaningJob());
	}

	private ExecutableTask oldReservationsCleaningJob() {
		final long hourSleep = 1000 * 60 * 60;
		return ExecutableTask.newBuilder()
				.withTaskType(TaskType.PERIODICAL_JOB)
				.withThreadSleep(hourSleep * 5L)
				.withTask(() -> {
					Iterable<Reservation> allReservations = reservationRepository.findAll();
					final long currentTime = new Date().getTime();
					allReservations.forEach(reservation -> {
						if (reservation.getValidTill() < currentTime) {
							log.debug("Remove old reservation:", reservation);
							reservationRepository.delete(reservation);
						}
					});
					log.info("Old reservations cleaning done.");
				})
				.build();
	}
}
