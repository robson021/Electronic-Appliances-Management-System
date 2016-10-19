package robert.svc.api;

public interface TaskSchedulerService {
    void submitNewTask(Runnable runnable);
}
