package com.fatec.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import com.fatec.engine.Simulacao;

public class Raycast {
		
	public static double degToRad(double ra) { return ra*Math.PI/180.0;}
	
	public static int FixAng(int a){ if(a>359){ a-=360;} if(a<0){ a+=360;} return a;}
	
	public static double dist(double ax, double ay, double bx, double by, double ang) {
		
		return (Math.sqrt(bx-ax) * (bx-ax) + (by -ay) * (by - ay));
	}
	
	public void drawRay2D(Graphics2D g2) {
		
		int r, mx, my, mp, dof, side;
		double vx,vy,rx,ry,ra,xo,yo,disV,disH;
		
		Color color = Color.green;
		
		xo = 0;
		yo = 64;
		ra=FixAng((int) (Simulacao.Pa+30));                 
		//ray set back 30 degrees
		 
		 for(r=0;r<60;r++)
		 {
		  //---Vertical--- 
		  dof=0; side=0; disV=10000;
		  double Tan=Math.tan(degToRad(ra));
		       if(Math.cos(degToRad(ra))> 0.001){ rx=(((int)Simulacao.Px>>6)<<6)+64;      ry=(Simulacao.Px-rx)*Tan+Simulacao.Py; xo= 64; yo=-xo*Tan;}//looking left
		  else if(Math.cos(degToRad(ra))<-0.001){ rx=(((int)Simulacao.Px>>6)<<6) -0.0001; ry=(Simulacao.Px-rx)*Tan+Simulacao.Py; xo=-64; yo=-xo*Tan;}//looking right
		  else { rx=Simulacao.Px; ry=Simulacao.Py; dof=8;}                                                  //looking up or down. no hit  

		  while(dof<8) 
		  { 
		   mx=(int)(rx)>>6; my=(int)(ry)>>6; mp=my*Simulacao.mapX+mx;                     
		   if(mp>0 && mp<Simulacao.mapX*Simulacao.mapY && Simulacao.map[mp]==1){ dof=8; disV=Math.cos(degToRad(ra))*(rx-Simulacao.Px)-Math.sin(degToRad(ra))*(ry-Simulacao.Py);}//hit         
		   else{ rx+=xo; ry+=yo; dof+=1;}                                               //check next horizontal
		  } 
		  vx=rx; vy=ry;

		  //---Horizontal---
		  dof=0; disH=1000000;
		  Tan=1.0/Tan; 
		       if(Math.sin(degToRad(ra))> 0.001){ ry=(((int)Simulacao.Py>>6)<<6) -0.0001; rx=(Simulacao.Py-ry)*Tan+Simulacao.Px; yo=-64; xo=-yo*Tan;}//looking up 
		  else if(Math.sin(degToRad(ra))<-0.001){ ry=(((int)Simulacao.Py>>6)<<6)+64;      rx=(Simulacao.Py-ry)*Tan+Simulacao.Px; yo= 64; xo=-yo*Tan;}//looking down
		  else{ rx=Simulacao.Px; ry=Simulacao.Py; dof=8;}                                                   //looking straight left or right
		 
		  while(dof<8) 
		  { 
		   mx=(int)(rx)>>6; my=(int)(ry)>>6; mp=my*Simulacao.mapX+mx;                          
		   if(mp>0 && mp<Simulacao.mapX*Simulacao.mapY && Simulacao.map[mp]==1){ 
			   dof=8; 
			   disH=Math.cos(degToRad(ra))*(rx-Simulacao.Px)-Math.sin(degToRad(ra))*(ry-Simulacao.Py);
		   }//hit         
		   else{ rx+=xo; ry+=yo; dof+=1;}                                               //check next horizontal
		  }
		  
		  	if(disV<disH){color = Color.green; rx=vx; ry=vy; disH=disV;}
		  	else {color = Color.green.darker(); rx=vx; ry=vy; disV=disH;
		  	g2.setColor(color);}
		  	//g2.drawLine((int) Px, (int) Py, (int) rx, (int) ry);

		  	int ca=FixAng((int) (Simulacao.Pa-ra)); disH=disH*Math.cos(degToRad(ca));                            //fix fisheye 
		  	int lineH = (int) ((Simulacao.mapS*320)/(disH)); if(lineH>Simulacao.height){ lineH=Simulacao.height;}                     //line height and limit
		  	int lineOff = 260 - (lineH>>1);                                               //line offset
		  
		  	g2.setPaint(new GradientPaint(r*18+Simulacao.width/-280, lineOff, color, r*18+Simulacao.width/4+-280, lineOff+lineH+32, Color.black));
		 	g2.drawLine(r*18+Simulacao.width/4-280, lineOff, r*18+Simulacao.width/4-280, lineOff+lineH);
		 	g2.setColor(new Color(0, 0, 0, (int)Math.min(lineOff, 255)));
		 	g2.drawLine(r*18+Simulacao.width/4-280, lineOff, r*18+Simulacao.width/4-280, lineOff+lineH);
		 	g2.setStroke(new BasicStroke(18));
		 	ra=FixAng((int) (ra-1));
		 }
	}
}
