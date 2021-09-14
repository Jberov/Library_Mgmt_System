package demo.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
	@Autowired
	public HelloWorldController() {
	}
	
	@RequestMapping("/hello")
	public String hello(@RequestParam(required = false) String name) {
		return "Hello " + name;
	}
}
