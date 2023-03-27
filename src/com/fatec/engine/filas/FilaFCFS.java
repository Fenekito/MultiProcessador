package com.fatec.engine.filas;

import com.fatec.engine.Fila;
import com.fatec.engine.Processo;
import com.fatec.engine.interfaces.FilaHandler;
import com.fatec.engine.enums.Prioridade;

public class FilaFCFS extends Fila {

	public FilaFCFS(FilaHandler handler, Prioridade priority) {
		super(handler, priority);
	}

	@Override
	protected Processo getProximoProcesso() {
		Processo proximoProcesso = null;

		if(processos.size() > 0) {
			proximoProcesso = processos.get(0);
		}

		return proximoProcesso;
	}
}
