package com.fatec.engine;

import com.fatec.utils.Raycast;

public class RaycastProcesso extends Processo {
	
	@Override
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		try {
			if(Simulacao.right) {
				Simulacao.Pa-=5; 
				Simulacao.Pa=Raycast.FixAng((int) Simulacao.Pa); 
				Simulacao.Pdx=Math.cos(Raycast.degToRad(Simulacao.Pa));
				Simulacao.Pdy=-Math.sin(Raycast.degToRad(Simulacao.Pa));
			}
			if(Simulacao.left)  {
				Simulacao.Pa+=5; 
				Simulacao.Pa=Raycast.FixAng((int) Simulacao.Pa); 
				Simulacao.Pdx=Math.cos(Raycast.degToRad(Simulacao.Pa)); 
				Simulacao.Pdy=-Math.sin(Raycast.degToRad(Simulacao.Pa));
			}
			if(Simulacao.up) {
				Simulacao.Px += Simulacao.Pdx;
				Simulacao.Py += Simulacao.Pdy;

			}
			if(Simulacao.down) {
				Simulacao.Px -= Simulacao.Pdx;
				Simulacao.Py -= Simulacao.Pdy;
			}
			
			thread.interrupt();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
