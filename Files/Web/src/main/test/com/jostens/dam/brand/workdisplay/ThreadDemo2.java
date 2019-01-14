package com.jostens.dam.brand.workdisplay;

public class ThreadDemo2 extends ThreadDemo
{

	@Override
	protected int getSleepSeconds()
	{
		return 30;
	}

	@Override
	protected void initializeThread()
	{
		System.out.println("INIT THREAD");

	}

	@Override
	protected void cleanupThread()
	{
		System.out.println("CLEANUP THREAD");
		try
		{
			Thread.sleep(4000);
		} catch (InterruptedException e)
		{
		}

	}

	@Override
	protected void performThreadAction()
	{
		System.out.println("PERFORM ACTION");

	}

}
