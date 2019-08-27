package com.ilinksolutions.p2.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailManager
{
	Logger logger = LoggerFactory.getLogger(EmailManager.class);

	private JavaMailSender javaMailSender;

	public void send(String message)
	{
		logger.info("EmailManager: send: Begin.");
		try
		{
			final ByteArrayOutputStream document = createInMemoryDocument(message);
			if(document == null)
			{
				logger.warn("EmailManager: send: document: NULL.");
			}
			else
			{
				logger.info("EmailManager: send: document: NOT NULL.");
				final InputStream inputStream = new ByteArrayInputStream(document.toByteArray());
				final DataSource attachment = new ByteArrayDataSource(inputStream, "application/octet-stream");
				sendMimeMessageWithAttachments("subject", "anonymous@xyz-mail.com", "anonymous@xyz-mail.com", attachment);				
			}
		}
		catch (IOException | MailException | MessagingException e)
		{
			logger.warn("EmailManager: send: " + e.getMessage());
			logger.warn(e.getMessage(), e);
		}
		logger.info("EmailManager: send: End.");
	}

	private void sendMimeMessageWithAttachments(String subject, String from, String to, DataSource dataSource)
			throws MessagingException
	{
		logger.info("EmailManager: sendMimeMessageWithAttachments: Begin.");
		javaMailSender = new JavaMailSenderImpl(); 
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setSubject("Test");
		helper.setFrom("samjsem@yahoo.com");
		helper.setTo("sungsam752729@gmail.com");
		helper.setReplyTo("samjsem@yahoo.com");
		helper.setText("stub", false);
		helper.addAttachment("message.eft", dataSource);
		javaMailSender.send(message);
		logger.info("EmailManager: sendMimeMessageWithAttachments: Sent Email.");
		logger.info("EmailManager: sendMimeMessageWithAttachments: End.");
	}

	private ByteArrayOutputStream createInMemoryDocument(String documentBody) throws IOException
	{
		logger.info("EmailManager: createInMemoryDocument: Begin.");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(documentBody.getBytes());
		logger.info("EmailManager: createInMemoryDocument: End: " + outputStream);
		return outputStream;
	}
}