package com.fatec.engine.interfaces;

import com.fatec.engine.Processo;
import java.util.ArrayList;

public interface Administravel<T> {
	public T addProcesso(Processo processo);
	public T addProcessos(ArrayList<Processo> processos);
	public ArrayList<Processo> getProcessos();
	public int countProcessos();
	public long countTempoRestante();
}