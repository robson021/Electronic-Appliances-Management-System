package robert.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import robert.svc.api.TaskSchedulerService;

@Service
public class TaskSchedulerImpl implements TaskSchedulerService {

    private final TaskExecutor taskExecutor;

    @Autowired
    public TaskSchedulerImpl(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void submitNewTask(Runnable runnable) {
        // todo
    }
}
