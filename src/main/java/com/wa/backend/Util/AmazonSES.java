package com.wa.backend.Util;

import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.wa.backend.vo.UserDto;

/**
 *
 * @author Benjamin.Abegunde
 */
 
@Service
public class AmazonSES {
	// This address must be verified with Amazon SES.
	private final String FROM = "taiwoabegunde@live.com";

	// The subject line for the email.
	private final String SUBJECT = "One last step to complete your registration";
	
	private final String SUBJECT2 = "You are leaving too soon";

	// The HTML body for the email.
	private final String HTMLBODY = "<h1>Please verify your email address</h1>"
			+ "<p>Thank you for registering with Oneworld Accuracy. To complete registration process,"
			+ " Use the following code for verification: "
			+ "$tokenValue"
			+ "<br/><br/>"
			+ "Thank you! And we are waiting for you inside!";

	// The email body for recipients with non-HTML email clients.
	private final String TEXTBODY = "Please verify your email address. "
			+ "Thank you for registering with Oneworld Accuracy. To complete registration process,"
			+ " open then the following URL in your browser window: "
			+ "$tokenValue"
			+ " Thank you! And we are waiting for you inside!";
	
        
        private final String HTMLBODY2 = "<h1>We are sad to let you go</h1>"
			+ "<p>We hope to see you again"+ "<br/><br/>"
			+ "Thank you!";

	// The email body for recipients with non-HTML email clients.
	private final String TEXTBODY2 = "We are sad to let you go. "
			+ "We hope to see you again."
                        + "Thank you!";
	
	

	public void verifyEmail(UserDto userDto) {

	
		//System.setProperty("aws.accessKeyId", "<YOUR KEY ID HERE>"); 
		//System.setProperty("aws.secretKey", "<SECRET KEY HERE>"); 
		
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.build();
 
		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(userDto.getEmail()))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);

		client.sendEmail(request);

		System.out.println("Email sent!");

	}
        
        
        
        public void offBoard(String email) {


		//System.setProperty("aws.accessKeyId", "<YOUR KEY ID HERE>"); 
		//System.setProperty("aws.secretKey", "<SECRET KEY HERE>"); 
		
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.build();
 
		

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(email))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY2))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY2)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT2)))
				.withSource(FROM);

		client.sendEmail(request);

		System.out.println("Email sent!");

	}
	
}
