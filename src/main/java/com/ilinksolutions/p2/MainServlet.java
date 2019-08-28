package com.ilinksolutions.p2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 */
public class MainServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		BufferedReader reader = new BufferedReader(new InputStreamReader(req.getServletContext().getResourceAsStream("/WEB-INF/index.html"), "UTF-8"));
		try
		{
			String line;
			boolean insideLoop = false;
			StringBuilder sb = new StringBuilder();
			sb.append("This is a test.");
			while ((line = reader.readLine()) != null)
			{
				if (line.trim().equals("<!-- begin repeat for each entry -->"))
				{
					insideLoop = true;
				}
				else if (line.trim().equals("<!-- end repeat for each entry -->"))
				{
					insideLoop = false;
					String entryTemplate = sb.toString();
				}
				else if (insideLoop)
				{
					sb.append(line).append("\n");
				}
				else
				{
					out.println(line);
				}
			}
		}
		finally
		{
			reader.close();
		}
	}

	private String escapeHtml(String text)
	{
		return text.replace("<", "&lt;");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String summary = req.getParameter("summary");
		String description = req.getParameter("description");
		resp.sendRedirect("dbMessages.html");
	}
}
