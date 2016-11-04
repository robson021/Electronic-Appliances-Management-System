package robert.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import robert.enums.BeanNames;
import robert.jobs.ExecutableTask;
import robert.svc.api.TaskSchedulerService;
import robert.utils.api.AppLogger;

@Service
public class TaskSchedulerImpl implements TaskSchedulerService {

	private final AppLogger log;

	private final TaskExecutor taskExecutor;

	@Autowired
	public TaskSchedulerImpl(AppLogger log, @Qualifier(BeanNames.DEFAULT_TASK_EXECUTOR) TaskExecutor taskExecutor) {
		this.log = log;
		this.taskExecutor = taskExecutor;
	}

	@Override
	public void submitNewTask(ExecutableTask task) {
		log.debug("Invoking new task:", task);
		taskExecutor.execute(task);
	}
}
