package com.fatec.engine;

import com.fatec.engine.interfaces.Administravel;
import com.fatec.engine.interfaces.ProcessoHandler;

public abstract class Fila implements Administravel, ProcessoHandler {
	public abstract void executar();

	//TODO: adicionar atributo que contenha o processo atual. NOME SUGERIDO: processoAtual (não sei se precisa ser aqui, na classe abstrata)

	//TODO: adicionar event handler que seja chamado pelo processo assim que ele esteja finalizado. NOME SUGERIDO: onProcessoFinalizado()

	//TODO: possivelmente adicionar um método privado para decidir o processo atual da fila. colocar aqui somente a tipagem do método, deixando as implementações para as classes que herdarem essa

	public static <T1 extends Fila, T2 extends Fila> T2 trocarAlgoritmo(T1 fila, T2 novaFila) {
		novaFila.addProcessos(fila.getProcessos());
		return novaFila;
	}
}
