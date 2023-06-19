package com.foxminded.javaspring.cardb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.foxminded.javaspring.cardb.generator.DbLoader;

@Component
@ConditionalOnProperty(prefix = "application", name = "env", havingValue = "prod")
public class ApplicationStartupRunner implements CommandLineRunner {

	private DbLoader dbLoader;

	@Autowired
	public ApplicationStartupRunner(DbLoader dbLoader) {
		this.dbLoader = dbLoader;
	}

	@Override
	public void run(String... args) throws Exception {
		dbLoader.loadDataToDb();
	}

}
