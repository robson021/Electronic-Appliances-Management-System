package robert.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.enums.TaskType;
import robert.jobs.api.JobRegistration;
import robert.svc.api.TaskSchedulerService;
import robert.utils.api.AppLogger;

import static robert.enums.BeanNames.DEFAULT_JOB_REGISTRATION;

@Component(DEFAULT_JOB_REGISTRATION)
public class JobRegistrationImpl implements JobRegistration {

	private final AppLogger log;

	private final TaskSchedulerService taskScheduler;

	private final ApplianceBuildingRoomManagementDao abrmDao;

	@Autowired
	public JobRegistrationImpl(AppLogger log, TaskSchedulerService taskScheduler, ApplianceBuildingRoomManagementDao abrmDao) {
		this.log = log;
		this.taskScheduler = taskScheduler;
		this.abrmDao = abrmDao;

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
				.withThreadSleep(hourSleep * 5)
				.withTask(() -> {
					int cleaned = abrmDao.cleanOldReservations();
					log.info("Old reservations cleaning done. Cleaned:", cleaned);
				})
				.build();
	}
}
