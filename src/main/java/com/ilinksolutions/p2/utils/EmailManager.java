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
		try {
			final ByteArrayOutputStream document = createInMemoryDocument(message);
			final InputStream inputStream = new ByteArrayInputStream(document.toByteArray());
			final DataSource attachment = new ByteArrayDataSource(inputStream, "application/octet-stream");
			sendMimeMessageWithAttachments("subject", "anonymous@xyz-mail.com", "anonymous@xyz-mail.com", attachment);
		} catch (IOException | MailException | MessagingException e) {
			logger.warn(e.getMessage(), e);
		}
		logger.info("EmailManager: send: End.");
	}

	private void sendMimeMessageWithAttachments(String subject, String from, String to, DataSource dataSource)
			throws MessagingException
	{
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setSubject("Test");
		helper.setFrom("samjsem@yahoo.com");
		helper.setTo("sungsam752729@gmail.com");
		helper.setReplyTo("samjsem@yahoo.com");
		helper.setText("stub", false);
		helper.addAttachment("message.eft", dataSource);
		javaMailSender.send(message);
	}

	private ByteArrayOutputStream createInMemoryDocument(String documentBody) throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(documentBody.getBytes());
		return outputStream;
	}
}