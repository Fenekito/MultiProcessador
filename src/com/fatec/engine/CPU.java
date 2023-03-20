package com.fatec.engine;

import java.util.ArrayList;
import java.util.LinkedList;

public class CPU {
	public static Processo curProcess;
	public static ArrayList<Processo> processqueue;
	
	public CPU() {
		processqueue = new ArrayList<Processo>();
	}
	
	public void addProcesso(Processo p) {
		processqueue.add(p);
	}
	
	public void execute() {
		if(processqueue.size() > 0) {
			if(curProcess == null) {
				curProcess = processqueue.get(0);
				curProcess.start();
			}
		}
	}
	
	public Processo getProcesso(int index) {
		return processqueue.get(index);
	}
}
