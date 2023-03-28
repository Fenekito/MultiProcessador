package com.fatec.engine.interfaces;

import com.fatec.engine.Processo;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Administravel<T> {
	public T addProcesso(Processo processo);
	public T addProcessos(CopyOnWriteArrayList<Processo> processos);
	public CopyOnWriteArrayList<Processo> getProcessos();
	public int countProcessos();
	public long countTempoRestante();
}