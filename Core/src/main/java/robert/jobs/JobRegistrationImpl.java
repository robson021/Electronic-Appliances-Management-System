package robert.jobs;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import robert.db.entity.Reservation;
import robert.db.repository.ReservationRepository;
import robert.enums.TaskType;
import robert.jobs.api.JobRegistration;
import robert.svc.api.TaskSchedulerService;
import robert.utils.api.AppLogger;

import static robert.enums.BeanNames.DEFAULT_JOB_REGISTRATION;

@Component(DEFAULT_JOB_REGISTRATION)
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
					final long threeDaysAgo = new DateTime().minusDays(3).toDate().getTime();
					final int[] cleaned = {0};
					allReservations.forEach(reservation -> {
						if (reservation.getValidTill() <= threeDaysAgo) {
							log.debug("Remove old reservation:", reservation);
							reservationRepository.delete(reservation);
							++cleaned[0];
						}
					});
					log.info("Old reservations cleaning done. Cleaned:", cleaned[0]);
				})
				.build();
	}
}
