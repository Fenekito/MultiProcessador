package com.fatec.engine;

import com.fatec.engine.interfaces.Administravel;

public abstract class Fila implements Administravel {

	public abstract void execute();

    public static <T1 extends Fila, T2 extends Fila> T2 trocarAlgoritmo(T1 fila, T2 novaFila) {
		novaFila.addProcessos(fila.getProcessos());
		return novaFila;
	}
}
