package com.ilinksolutions.p2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ilinksolutions.p2.utils.AES256Manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Upload", urlPatterns = {"/uploadFile"})
public class Upload extends HttpServlet 
{
	String message = null;
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(Upload.class);
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	try
    	{
    		logger.info("sdfk");
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
            }
            else
            {
            	logger.info("Not multipart request.");
            }
        } 
        catch (FileUploadException ex)
        {
            logger.info("Upload: doPost: Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        response.sendRedirect("/decrypt.html");
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
    	return stringBuilder.toString();
    }
}