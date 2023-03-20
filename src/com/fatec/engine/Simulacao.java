package com.fatec.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.fatec.utils.Raycast;

	
/**
* @author Fenekito
*/

public class Simulacao extends Canvas implements Runnable, KeyListener {
    
	private static final long serialVersionUID = 1L;
	public static int width = 1024;
	public static int height = 512;
	
	private Dimension s;
	
	private Thread thread;
	
	private boolean isRunning;
	
	private BufferedImage image;
		
	JFrame frame;
	
	public static CPU cpus[] = new CPU[4];
	public static int cpuIndex = 0;
		
	public static boolean up, down, left, right;
	
	public static double Px = 300, Py = 300;
	
	public static double Pdx , Pdy, Pa;
	
	public static int mapX = 8, mapY = 8, mapS = 64;
	
	public double P2 = Math.PI/2;
	public double P3 = 3*Math.PI/2;
	public double DR = 0.0174533;
        	
	public static int map[] = 
		{
			1,1,1,1,1,1,1,1,
			1,0,0,0,0,0,0,1,
			1,0,0,0,0,0,0,1,
			1,0,0,0,0,0,0,1,
			1,0,0,1,0,0,0,1,
			1,0,1,1,0,0,0,1,
			1,0,1,0,0,0,0,1,
			1,1,1,1,1,1,1,1,
		};
	
	public static BufferedImage wallTex;
	
	private Raycast raycast;
	
	public Simulacao() {
		
		s = new Dimension((int)width , (int)height);
		Pdx=Math.cos(Raycast.degToRad(Pa)); Pdy=-Math.sin(Raycast.degToRad(Pa)); 
		initFrame();
		addKeyListener(this);
		
		for(int i = 0; i < 4; i++) {
			cpus[i] = new CPU();
		}
		
		raycast = new Raycast();
		
		try {
			wallTex = ImageIO.read(getClass().getResource("/Wall.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Simulacao simulacao = new Simulacao();
		simulacao.start();
	}
	
	public void update() {
		for(CPU cpu : cpus) {
			if(cpu.processqueue.size() > 0) {
				cpu.execute();
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		Graphics2D g2 = (Graphics2D) image.createGraphics();
				
		g2.setColor(Color.blue);
		g2.fillRect(0,0, width, height);
                
                g2.setColor(Color.yellow);
                for(int i = 0; i < cpus.length; i++) {
                    g2.drawString("CPU: " + i + "\n" + "Processos: " + cpus[i].processqueue.size(), 16, 24 + (i+1) * 32);
                }
                
		g2 = (Graphics2D) bs.getDrawGraphics();
		
		g2.drawImage(image, 0, 0, width, height, null);
		g2.dispose();
		bs.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long elapsedTime;
		double tickRate = 60.0;
		double ns = 1000000000 / tickRate;
		double sleepTime;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta+=(now-lastTime)/ns;
			elapsedTime = now - lastTime;
			sleepTime = (1000000000 / 60) - elapsedTime;
			lastTime = now;
			if(delta >= 1) {
				update();
				render();
				frames++;
				delta--;
				try {thread.sleep((long) ((lastTime-System.nanoTime() + sleepTime)/1000000));} catch(Exception e) {}
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				frames = 0;
				timer+=1000;
			}
		}stop();
		
	}
	/**
         * Inicializa uma Janela
         * 
         * 
         **/
	public void initFrame() {
		
		setPreferredSize(s);
		setMaximumSize(s);
		setMinimumSize(s);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		frame = new JFrame("Simulacao");
		
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		cpus[cpuIndex].addProcesso(new Processo(cpus[cpuIndex]));
		cpuIndex++;
		if(cpuIndex > 3) {
			cpuIndex = 0;
		}
		switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				right = false;
				left = true;
				break;
			case KeyEvent.VK_D:
				right = true;
				left = false;
				break;
			case KeyEvent.VK_W:
				up = true;
				down = false;
				break;
			case KeyEvent.VK_S:
				up = false;
				down = true;
				break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				left = false;
				break;
			case KeyEvent.VK_D:
				right = false;
				break;
			case KeyEvent.VK_W:
				up = false;
				break;
			case KeyEvent.VK_S:
				down = false;
				break;
		}
		
	}

}
