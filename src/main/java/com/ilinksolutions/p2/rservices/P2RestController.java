package com.ilinksolutions.p2.rservices;

import java.util.List;
import java.util.Random;
import java.net.URI;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ilinksolutions.p2.domains.UKVisaMessage;
import com.ilinksolutions.p2.bservices.UKVisaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    	int returnValue = service.addEntry(entry);
    	logger.info("sayHello: " + entry.toString());
    	/*
    	 * 
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
    
    @GetMapping("/getmsg/{id}")
    public ResponseEntity<UKVisaMessage> readEntry(@PathVariable String id)
    {
    	logger.info("P2RestController: readEntry: Begin.");
    	logger.info("P2RestController: readEntry: Path Variable: " + id);
        UKVisaService service = new UKVisaService();
        UKVisaMessage returnValue = service.getEntry(new Integer(id).intValue());
        if (returnValue == null)
        {
        	logger.info("P2RestController: readEntry: returnValue: NULL");
            return ResponseEntity.notFound().build();
        }
        else
        {
            logger.info("P2RestController: readEntry: returnValue: " + returnValue.toString());
            return ResponseEntity.ok(returnValue);
        }
    }
    
    /*
    @RequestMapping(method = RequestMethod.POST, value="/savemsg")
    @ResponseBody*/
    @PostMapping("/savemsg")
    public ResponseEntity<UKVisaMessage> registerMessage(@RequestBody UKVisaMessage message)
    {
    	logger.info("registerMessage: registerMessage: Begin.");
    	logger.info("registerMessage: registerMessage: Transform: " + message.toString());
    	UKVisaService service = new UKVisaService();
    	
    	int id = service.addEntry(message);
    	UKVisaMessage returnValue = new UKVisaMessage();
    	returnValue.setId(id);
    	if (id == 0)
    	{
    		logger.info("registerMessage: registerMessage: id: NULL.");
            return ResponseEntity.notFound().build();
        }
    	else
    	{
    		logger.info("registerMessage: registerMessage: id: End.");
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(returnValue.getId()).toUri();
            return ResponseEntity.created(uri).body(returnValue);
        }
    }
}