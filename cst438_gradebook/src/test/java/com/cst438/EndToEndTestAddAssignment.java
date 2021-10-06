package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 */

@SpringBootTest
public class EndToEndTestAddAssignment {

	public static final String CHROME_DRIVER_FILE_LOCATION = "J:/CST438/chromedriver.exe";

	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final String TEST_ASSIGNMENT_NAME = "TEST";
	public static final String TEST_ASSIGNMENT_DUEDATE = "2021-09-05";
	public static final String TEST_COURSE_ID = "123456";
	public static final int SLEEP_DURATION = 1000; // 1 second.

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;

	@Test
	public void addCourseTest() throws Exception {
		
		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);
		
		int testAssignId = 9999;

		try {
			// locate form for assignment submission
			WebElement we = driver.findElement(By.xpath("//div[@className='formContainer']//input"));
		 	we.click();


			// place data into inputs
	        we.findElement(By.xpath("input[@name='assignmentName']")).sendKeys(TEST_ASSIGNMENT_NAME);
	        we.findElement(By.xpath("input[@name='dueDate']")).sendKeys(TEST_ASSIGNMENT_DUEDATE);
	        we.findElement(By.xpath("input[@name='courseId']")).sendKeys(TEST_COURSE_ID);
			// Locate and click submit button
			driver.findElement(By.xpath("input[@type='submit']")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify that assignment has been added to database
			Iterable<Assignment> assignsAll = assignmentRepository.findAll();
			
			for (Assignment assign : assignsAll) {
				if (assign.getName().equals(TEST_ASSIGNMENT_NAME)){
					 assertEquals(TEST_ASSIGNMENT_NAME, assign.getName());
					 testAssignId = assign.getId();
					 break;
				}
			}

		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			Assignment cleanUp = assignmentRepository.findById(testAssignId);
			if (cleanUp!=null) assignmentRepository.delete(cleanUp);

			driver.quit();
		}

	}
}
