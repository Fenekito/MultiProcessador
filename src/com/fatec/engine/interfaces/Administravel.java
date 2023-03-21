package com.fatec.engine.interfaces;

import com.fatec.engine.Processo;
import java.util.ArrayList;

public interface Administravel {
	public void addProcesso(Processo processo);
	public void addProcessos(ArrayList<Processo> processos);
	public ArrayList<Processo> getProcessos();
	public int countProcessos();
}