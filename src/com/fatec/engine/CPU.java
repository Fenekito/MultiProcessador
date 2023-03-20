package com.fatec.engine;

import java.util.LinkedList;

public class CPU {
	private Processo curProcess;
	public static LinkedList<Processo> processqueue;
	
	public CPU() {
		processqueue = new LinkedList<Processo>();
	}
	
	public void addProcesso(Processo p) {
		processqueue.add(p);
	}
	
	public void execute() {
		if(processqueue.size() > 0) {
			curProcess = processqueue.getFirst();
			processqueue.remove(curProcess);
			curProcess.start();
		}
	}
	
	public Processo getProcesso(int index) {
		return processqueue.get(index);
	}
}
