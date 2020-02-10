package com.minhduc.tuto.springboot.bootstrapping.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Minh Duc Ngo
 *
 */
@Component
public class Scheduler {
    /**
     * Cron Expression <second> <minute> <hour> <day-of-month> <month>
     * <day-of-week> <year>
     * 
     * This method to execute the task every minute starting at 16:00 AM and
     * ending at 15:59 AM, every day Cron Expression
     * 
     */
    @Scheduled(cron = "0 * 16 * * ?")
    public void cronJobSch() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	Date now = new Date();
	String strDate = sdf.format(now);
	System.out.println("Java cron job expression:: " + strDate);
    }

    /**
     * Fixed Rate scheduler is used to execute the tasks at the specific time.
     * It does not wait for the completion of previous task. The values should
     * be in milliseconds.
     * 
     * A sample code for executing a task on every five seconds from the
     * application startup is shown here
     */
    @Scheduled(fixedRate = 10000)
    public void fixedRateSch() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	Date now = new Date();
	String strDate = sdf.format(now);
	System.out.println("Fixed Rate scheduler:: " + strDate);
    }

    /**
     * Fixed Delay scheduler is used to execute the tasks at a specific time. It
     * should wait for the previous task completion.
     * 
     * An example to execute the task for every 10 seconds after 3 seconds from the application startup has been completed is shown below 
     * 
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 3000)
    public void fixedDelaySch() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	Date now = new Date();
	String strDate = sdf.format(now);
	System.out.println("Fixed Delay scheduler:: " + strDate);
    }
}
