package com.fatec.engine;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fatec.engine.interfaces.Administravel;
import com.fatec.engine.interfaces.CPUHandler;

public class CPUController implements Administravel<Void> {

	private ArrayList<CPU> cpus = new ArrayList<CPU>();
	private CPUHandler _handler;
	private ExecutorService _executor;
	
	public CPUController(CPUHandler handler, int cpuCount) {
		_handler = handler;
		_executor = Executors.newFixedThreadPool(cpuCount);
		addCPU(cpuCount);
	}

	private CPU getCPUMenosUsada() {
		cpus.sort(CPU.tempoRestanteComparator);
		return cpus.get(0);
	}

	private void addCPU(int count) {
		for (int i = 0; i < count; i++) {
			CPU cpu = new CPU(_handler);
			cpus.add(cpu);
			_executor.execute(cpu);
		}
	}

	@Override
	public long countTempoRestante() {
		long tempoRestanteAcumulado = 0;

		for (CPU cpu : cpus) {
			tempoRestanteAcumulado += cpu.countTempoRestante();
		}

		return tempoRestanteAcumulado;
	}

	@Override
	public int countProcessos() {
		int contagem = 0;

		for (CPU cpu : cpus) {
			contagem += cpu.countProcessos();
		}

		return contagem;
	}

	@Override
	public CopyOnWriteArrayList<Processo> getProcessos() {
		CopyOnWriteArrayList<Processo> processos = new CopyOnWriteArrayList<Processo>();

		for (CPU cpu : cpus) {
			processos.addAll(cpu.getProcessos());
		}

		return processos;
	}

	@Override
	public Void addProcesso(Processo processo) {
		getCPUMenosUsada()
			.addProcesso(processo)
			.atualizar();

		return null;
	}

	@Override
	public Void addProcessos(CopyOnWriteArrayList<Processo> processos) {
		for (Processo processo : processos) {
			getCPUMenosUsada().addProcesso(processo);
		}

		for (CPU cpu : cpus) {
			cpu.atualizar();
		}

		return null;
	}
}