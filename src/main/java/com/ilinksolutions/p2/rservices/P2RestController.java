package com.ilinksolutions.p2.rservices;

import java.util.List;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

import com.ilinksolutions.p2.domains.UKVisaMessage;
import com.ilinksolutions.p2.bservices.UKVisaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class P2RestController
{ 
    //	@GetMapping("/hello")
    @GetMapping("/getJSON")
    public Collection<String> sayHello()
    {
    	UKVisaMessage person = null;
    	UKVisaService service = new UKVisaService();
    	List<UKVisaMessage> persons = service.getAllEntries();
    	for(int i = 0; i < persons.size(); i++)
    	{
    		person = persons.get(i);
    		System.out.println("Person: Person ID: " + person.getId());
    		System.out.println("Person: List: Completed: **************************************");
    	}
        return IntStream.range(0, 10).mapToObj(i -> "Hello number " + i).collect(Collectors.toList());
    }
}