package com.ilinksolutions.p2.data.impl;

import java.sql.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;

import com.ilinksolutions.p2.data.UKVisaDAO;
import com.ilinksolutions.p2.domains.UKVisaMessage;

/**
 *  TODO: proper exception handling
 *  TODO: initialize schema whenever necessary (what if db is not persistent and is restarted while app is running)
 */

public class UKVisaDAOImpl implements UKVisaDAO
{
	private final DataSource dataSource;

	public UKVisaDAOImpl()
	{
		dataSource = lookupDataSource();
		/**
		 *  
		 *  Schema should already be present.
		 *  
		 */
		//	initializeSchemaIfNeeded();
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

	private void initializeSchemaIfNeeded()
	{
		Statement statement = null;
		Connection connection = null;
		try
		{
			connection = getConnection();
			if (!isSchemaInitialized(connection))
			{
				connection.setAutoCommit(true);
				statement = connection.createStatement();
				statement.executeUpdate("CREATE TABLE visadata (id bigint, summary VARCHAR(255), description TEXT)");
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
				statement.close();
				connection.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("UKVisaDAOImpl: initializeSchemaIfNeeded: Could not communicate with DB.");
			}
		}
	}

	private boolean isSchemaInitialized(Connection connection) throws SQLException
	{
		ResultSet rset = connection.getMetaData().getTables(null, null, "visadata", null);
		try
		{
			return rset.next();
		}
		finally
		{
			rset.close();
		}
	}

	@Override
	public void save(UKVisaMessage entry)
	{
		Connection connection = null;
		PreparedStatement statement = null;
		try
		{
			connection = getConnection();
			connection.setAutoCommit(true);
			statement = connection.prepareStatement("INSERT INTO visadata (id, summary, description) VALUES (?, ?, ?)");
			statement.setLong(1, getNextId());
			statement.setString(2, entry.getSummary());
			statement.setString(3, entry.getDescription());
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
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
			rset = statement.executeQuery("SELECT id, summary, description FROM visadata");
			list = new ArrayList<UKVisaMessage>();
			while (rset.next())
			{
				Long id = rset.getLong(1);
				String summary = rset.getString(2);
				String description = rset.getString(3);
				list.add(new UKVisaMessage(id, summary, description));
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
}
