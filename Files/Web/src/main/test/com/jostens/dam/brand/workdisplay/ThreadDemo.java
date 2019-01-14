package com.jostens.dam.brand.workdisplay;

/**
 * Demo of another way to handle threads.  This could be a cleaner solution allowing an immediate
 * interrupt that stops the Thread instead of currently having to wait for a Sleep time to finish.
 */
public abstract class ThreadDemo extends Thread
{
	// If set to true, thread should stop running because sleep was interrupted
	private boolean sleepInterrupted = false;

	/**
	 * Over-riding the Thread run() method
	 */
    @Override
    public void run()
    {
    	System.out.println("STARTING RUN");
    	sleepInterrupted = false;
 
    	// Initialize Thread
    	initializeThread();
    	
    	while (true)
    	{
    		// The thread action is always called once
    		performThreadAction();
    		
    		// Call the performThreadSleep method to delay an implementor amount of seconds if desired
    		performThreadSleep();
    		
    		if (threadShouldStop())
    		{
    			break;
    		}
//    		System.out.println("Sleeping for 5");
//    		sleep(5000);
    	}

    	// Cleanup Thread
    	cleanupThread();
    	System.out.println("ENDING RUN");
    }
    
//    @Override
//    public void interrupt() 
//    {
//    	// Override interrupt method
//    	System.out.println("INTERRUPT CALLED");
//    	super.interrupt();
//    	System.out.println("POST INTERRUPT CALLED");
//    }
    
    /*
     * Perform needed checks to determine if this thread should stop processing
     */
    private boolean threadShouldStop()
    {
    	// If interrupted, should stop
    	if (isInterrupted())
    	{
    		return true;
    	}
    	// If sleeping was interrupted
    	if (sleepInterrupted)
    	{
    		return true;
    	}
    	// If sleeping for 0 seconds, should stop because this Thread only processes once
    	if (getSleepSeconds() == 0)
    	{
    		return true;
    	}
    	
    	return false;		// Continue for anothe loop
    }

    private void performThreadSleep()
    {
    	if (getSleepSeconds() == 0)
    	{
    		// No sleep is performed
    	}
  
//    	System.out.println("SLEEP SECONDS=" + getSleepSeconds());
    	try
		{
			super.sleep(getSleepSeconds() * 1000);
		} catch (InterruptedException e)
		{
//			System.out.println("INTERRUPTED EXCEPTION");
			sleepInterrupted = true;
		}
    }
    
    /**
     * Return seconds to sleep between processing cycles.  A value of 0 means there isn't any sleep
     * to happen so the thread will execute only once.
     */
    protected abstract int getSleepSeconds();
    
    /**
     * Method to implement if any Thread initialization is needed
     */
    protected abstract void initializeThread();
    
    /**
     * Method to implement if any Thread cleanup is needed
     */
    protected abstract void cleanupThread();
    
    /**
     * Method that should contain any processing that needs to occur each cycle.
     */
    protected abstract void performThreadAction();
}
