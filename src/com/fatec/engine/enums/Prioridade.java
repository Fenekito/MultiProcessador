package com.fatec.engine.enums;

public enum Prioridade {
  OS(0, "OS"),
  INTERACTIVE(1, "INTERACTIVE"),
  INTERACTIVE_EDITION(2, "INTERACTIVE_EDITION"),
  BATCH(3, "BATCH"),
  SECONDARY(4, "SECONDARY");

  private final int codigo;
  private final String nome;

  private Prioridade(int codigo, String nome) {
    this.codigo = codigo;
	this.nome = nome;
  }

  public int toInt() {
    return codigo;
  }

  public String toString() {
	return nome;
  }
  
  public Prioridade getNext() {
	if(codigo - 1  >= 0) {
		return values()[ordinal() - 1];
	}
	return values()[ordinal()];
  }

  //pega a próxima prioridade. se chegar na última, pega a primeira
  public Prioridade tiltNext() {
	if(codigo - 1  >= 0) {
		return values()[ordinal() - 1];
	} 

	return values()[4];
  }
}
