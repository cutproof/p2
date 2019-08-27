package com.ilinksolutions.p2.bservices;

import com.ilinksolutions.p2.data.UKVisaDAO;
import com.ilinksolutions.p2.data.impl.UKVisaDAOImpl;
import com.ilinksolutions.p2.domains.UKVisaMessage;
import com.ilinksolutions.p2.utils.AES256Manager;
import com.ilinksolutions.p2.utils.EmailManager;

import java.util.List;

/**
 *
 */
public class UKVisaService
{
	private UKVisaDAO dao = new UKVisaDAOImpl();

	public int addEntry(UKVisaMessage entry)
	{
		String messageString = "{\"id\": " + entry.getId() + "," +
								"\"firstName\": \"" + entry.getFirstName() + "\"," +
								"\"lastName\": \"" + entry.getLastName() + "\"," +
								"\"contactNo\": \"" + entry.getContactNo() + "\"," +
								"\"email\": \"" + entry.getContactNo() + "\"}";
		String encryptedString = AES256Manager.encryptMessage(messageString);
		EmailManager eMail = new EmailManager();
		eMail.send(encryptedString);
		return dao.save(entry);
	}

	public List<UKVisaMessage> getAllEntries()
	{
		return dao.list();
	}
	
	public UKVisaMessage getEntry(int id)
	{
		return dao.getEntry(id);
	}
	
	public UKVisaMessage updateEntry(int id, UKVisaMessage message)
	{
		return dao.updateEntry(id, message);
	}
}
