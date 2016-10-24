package robert.svc.api;

import robert.jobs.ExecutableTask;

public interface TaskSchedulerService {
    void submitNewTask(ExecutableTask task);
}
