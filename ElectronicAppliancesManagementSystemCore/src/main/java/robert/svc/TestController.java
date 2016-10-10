package robert.svc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String hello() {
        return "Hello World " + new Date().toString();
    }
}
