package com.ilinksolutions.p2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ilinksolutions.p2.utils.AES256Manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 *
 */
public class MainServlet extends HttpServlet
{
	String message = null;
    Logger logger = LoggerFactory.getLogger(MainServlet.class);
	
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

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
			  .append("<html>\r\n")
			  .append("		<head>\r\n")
			  .append("			<title>iLink Solutions: Phase Two Demo</title>\r\n")
			  .append("		</head>\r\n")
			  .append("		<body>\r\n");		
    	try
    	{
    		logger.info("MainServlet: doPost: Begin.");
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if(isMultipart)
            {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iter = upload.getItemIterator(request);
                while (iter.hasNext())
                {
                    FileItemStream item = iter.next();
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    logger.info("File field " + name + " with file name " + item.getName() + " detected.");
                    String unmarshalledString = convert(stream, Charset.defaultCharset());
                    message = AES256Manager.decryptMessage(unmarshalledString);
                    logger.info("AES256Manager: message decrypted: " + message);
                }                   	
                writer.append("Decrypted message: " + message);
            }
            else
            {
            	writer.append("No File.");
            	logger.info("Not multipart request.");
            }
        } 
        catch (FileUploadException ex)
        {
            logger.info("Upload: doPost: Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
		writer.append("		</body>\r\n")
		.append("</html>\r\n");
    }
    
    public String convert(InputStream inputStream, Charset charset) throws IOException
    {	 
    	StringBuilder stringBuilder = new StringBuilder();
    	String line = null;
    	
    	try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset)))
    	{	
    		while ((line = bufferedReader.readLine()) != null)
    		{
    			stringBuilder.append(line);
    		}
    	}
    	logger.info("MainServlet: doPost: End.");
    	return stringBuilder.toString();
    }
}
