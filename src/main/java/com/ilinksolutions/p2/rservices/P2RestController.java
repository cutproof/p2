package com.ilinksolutions.p2.rservices;

import java.util.List;
import java.util.Random;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ilinksolutions.p2.domains.UKVisaMessage;
import com.ilinksolutions.p2.bservices.UKVisaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class P2RestController
{
	Logger logger = LoggerFactory.getLogger(P2RestController.class);
	
    @GetMapping("/hello")
    public Collection<String> sayHello()
    {
    	logger.info("sayHello: started.");
    	UKVisaService service = new UKVisaService();
    	UKVisaMessage entry = new UKVisaMessage();
    	entry.setContactNo("202-277-0788");
    	entry.setEmail("harjeet.parmar@ilinksolutions.com");
    	entry.setFirstName("Harjeet");
    	entry.setLastName("Parmar");
    	entry.setId(12);
    	service.addEntry(entry);
    	logger.info("sayHello: " + entry.toString());
    	/*
    	List<UKVisaMessage> persons = service.getAllEntries();
    	for(int i = 0; i < persons.size(); i++)
    	{
    		person = persons.get(i);
    		System.out.println("Person: Person ID: " + person.getId());
    		System.out.println("Person: List: Completed: **************************************");
    	}
    	*/
    	logger.info("sayHello: Ended.");
        return IntStream.range(0, 10).mapToObj(i -> "Hello number " + i).collect(Collectors.toList());
    }
}