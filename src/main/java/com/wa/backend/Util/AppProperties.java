package com.wa.backend.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 *
 * @author Benjamin.Abegunde
 */
@Component
public class AppProperties {

	@Autowired
	private Environment env;
	
	public String getTokenSecret()
	{
		return env.getProperty("tokenSecret");
	}
}
