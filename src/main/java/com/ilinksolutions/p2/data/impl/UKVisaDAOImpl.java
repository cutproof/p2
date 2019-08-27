package com.ilinksolutions.p2.data.impl;

import java.sql.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;

import com.ilinksolutions.p2.data.UKVisaDAO;
import com.ilinksolutions.p2.domains.UKVisaMessage;
import com.ilinksolutions.p2.rservices.P2RestController;

/**
 *  TODO: proper exception handling
 *  TODO: initialize schema whenever necessary (what if db is not persistent and is restarted while app is running)
 */

public class UKVisaDAOImpl implements UKVisaDAO
{
	Logger logger = LoggerFactory.getLogger(UKVisaDAOImpl.class);
	
	private final DataSource dataSource;

	public UKVisaDAOImpl()
	{
		dataSource = lookupDataSource();
		/**
		 *  
		 *  Schema should already be present.
		 *  
		 */
	}

	private DataSource lookupDataSource()
	{
		Context initialContext = null;
		Context envContext = null;
		try
		{
			initialContext = new InitialContext();
			try
			{
				return (DataSource) initialContext.lookup(System.getenv("DB_JNDI"));
			}
			catch (NameNotFoundException e)
			{
				// Tomcat places datasources inside java:comp/env
				envContext = (Context) initialContext.lookup("java:comp/env");
				return (DataSource) envContext.lookup(System.getenv("DB_JNDI"));
			}
		}
		catch (NamingException e)
		{
			throw new RuntimeException("UKVisaDAOImpl: lookupDataSource: Could not look up datasource", e);
		}
	}

	@Override
	public int save(UKVisaMessage entry)
	{
		logger.info("UKVisaDAOImpl: save: Begin.");
		Connection connection = null;
		PreparedStatement statement = null;
		int returnValue = 0;
		try
		{
			logger.info("UKVisaDAOImpl: save: " + entry.toString());
			connection = getConnection();
			connection.setAutoCommit(true);
			//	statement = connection.prepareStatement("INSERT INTO visadata (id, summary, description) VALUES (?, ?, ?)");
			statement = connection.prepareStatement("INSERT INTO public.visadata(person_id, first_name, last_name, contact_no, email) "
					+ "VALUES (?, ?, ?, ?, ?) RETURNING person_id");
			statement.setInt(1, (int) entry.getId());
			statement.setString(2, entry.getFirstName());
			statement.setString(3, entry.getLastName());
			statement.setString(4, entry.getContactNo());
			statement.setString(5, entry.getEmail());			
			ResultSet rs = statement.executeQuery();
			rs.next();
			returnValue = rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("list: SQLException: e: " + e.getMessage());
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				statement.close();
				connection.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("UKVisaDAOImpl: save: Could not communicate with DB.");
			}
		}
		logger.info("UKVisaDAOImpl: save: End.");
		return returnValue;
	}

	@Override
	public List<UKVisaMessage> list()
	{
		ResultSet rset = null;
		Statement statement = null;
		Connection connection = null;
		List<UKVisaMessage> list = null;
		try
		{
			connection = getConnection();
			statement = connection.createStatement();
			rset = statement.executeQuery("SELECT person_id, first_name, last_name, contact_no, email FROM visadata");
			list = new ArrayList<UKVisaMessage>();
			while (rset.next())
			{
				int id = rset.getInt(1);
				String firstName = rset.getString(2);
				String lastName = rset.getString(3);
				String contactNo = rset.getString(4);
				String email = rset.getString(5);
				list.add(new UKVisaMessage(id, firstName, lastName, contactNo, email));
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				rset.close();
				statement.close();
				connection.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("UKVisaDAOImpl: list: Could not communicate with DB.");
			}
		}
		return list;
	}

	public Connection getConnection() throws SQLException
	{
		return getDataSource().getConnection();
	}

	private DataSource getDataSource()
	{
		return dataSource;
	}

	private long getNextId()
	{
		return new Random().nextLong();
	}

	@Override
	public UKVisaMessage getEntry(int id)
	{
		ResultSet rset = null;
		Statement statement = null;
		Connection connection = null;
		UKVisaMessage returnValue= null;
		try
		{
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement("SELECT person_id, first_name, last_name, contact_no, email FROM visadata where person_id = ?");
		    ps.setInt(1, id);
		    ResultSet rs = ps.executeQuery();
		    returnValue = new UKVisaMessage();
			while (rset.next())
			{
			    returnValue.setId(rs.getInt(1));
			    returnValue.setFirstName(rs.getString(2));
			    returnValue.setLastName(rs.getString(3));
			    returnValue.setContactNo(rs.getString(4));
			    returnValue.setEmail(rs.getString(5));
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				rset.close();
				statement.close();
				connection.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("UKVisaDAOImpl: getEntry: Could not communicate with DB.");
			}
		}
		return returnValue;
	}
}
