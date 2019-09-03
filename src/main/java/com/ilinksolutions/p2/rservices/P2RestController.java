package com.ilinksolutions.p2.rservices;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ilinksolutions.p2.domains.UKVisaMessage;
import com.ilinksolutions.p2.bservices.UKVisaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class P2RestController
{
	Logger logger = LoggerFactory.getLogger(P2RestController.class);
	
    @GetMapping("/getmsg/{id}")
    public ResponseEntity<UKVisaMessage> readEntry(@PathVariable String id)
    {
    	logger.info("P2RestController: readEntry: Begin!");
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
    
    @PostMapping("/savemsg")
    public ResponseEntity<UKVisaMessage> registerMessage(@RequestBody UKVisaMessage message)
    {
    	logger.info("registerMessage: registerMessage: Begin.");
    	logger.info("registerMessage: registerMessage: Transform: " + message.toString());
    	UKVisaService service = new UKVisaService();
    	UKVisaMessage returnValue = service.addEntry(message);
    	if (returnValue == null)
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
    
    @PutMapping("/updatemsg/{id}")
    public ResponseEntity<UKVisaMessage> update(@RequestBody UKVisaMessage message, @PathVariable int id)
    {
        UKVisaService service = new UKVisaService();
        UKVisaMessage returnValue = service.updateEntry(id, message);
        if (returnValue == null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok(returnValue);
        }
    }
}