package com.ilinksolutions.p2.bservices;

import com.ilinksolutions.p2.data.UKVisaDAO;
import com.ilinksolutions.p2.data.impl.UKVisaDAOImpl;
import com.ilinksolutions.p2.domains.UKVisaMessage;

import java.util.List;

/**
 *
 */
public class UKVisaService
{
	private UKVisaDAO dao = new UKVisaDAOImpl();

	public int addEntry(UKVisaMessage entry)
	{
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
}
