package com.urwex;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try
		{
			WhatsApp wa = new WhatsApp();
			Thread thread = new Thread(wa);
			thread.start();
			Thread.sleep(60000);			
			System.out.println("Enter 'Q' anytime at the prompt to quit");
			while(!sc.next().equalsIgnoreCase("Q"))
			{
				Thread.sleep(30000);
			}
			wa.continueRunning = false;
			thread.interrupt();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		finally
		{
			sc.close();
		}
	}
}
