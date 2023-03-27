package com.fatec.engine.interfaces;

import com.fatec.engine.CPU;
import com.fatec.engine.Fila;
import com.fatec.engine.Processo;

public interface CPUHandler {
	public void onNovoProcesso(Processo processo, CPU cpu);
	public void onNovaFila(Fila fila, CPU cpu);
}
