package com.jostens.dam.brand.workdisplay;

import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;

public class ThreadDemoTest extends BrandBaseTest
{

	@Test
	public void testThreadDemo() throws InterruptedException
	{
		System.out.println("Starting ThreadDemo");
		Thread thread = new ThreadDemo2();
		thread.start();
		System.out.println("IS Alive #1" + thread.isAlive());
		Thread.sleep(79000);
		System.out.println("Interrupt Thread");
		thread.interrupt();
		System.out.println("IS Alive #2" + thread.isAlive());
		Thread.sleep(1000);
		System.out.println("IS Alive #3" + thread.isAlive());
		Thread.sleep(1000);
		System.out.println("IS Alive #4" + thread.isAlive());
		Thread.sleep(1000);
		System.out.println("IS Alive #5" + thread.isAlive());
		Thread.sleep(1000);
		System.out.println("IS Alive #6" + thread.isAlive());
		Thread.sleep(1000);
		System.out.println("IS Alive #7" + thread.isAlive());
		Thread.sleep(20000);
		System.out.println("Ending ThreadDemo");
	}

}
