import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class FlappyBird implements Jogo {
	
	public double scenario_offset = 0;
	public double ground_offset = 0;
	public double gvx = 100;	
	public Passaro passaro;
	public ArrayList<Cano> canos = new ArrayList<Cano>();
	public Random gerador = new Random();
	public Timer timer_cano;
	
	
	public FlappyBird() {
		passaro = new Passaro(35, (getLargura() - 112)/2 + 24/2);
		timer_cano = new Timer(2, true, addCano());
		addCano().executa();
	}
	
	private Acao addCano() {
		return new Acao() {
			public void executa() {				
				canos.add(new Cano(getLargura() + 10, gerador.nextInt(getAltura() - 112 - Cano.HOLESIZE), -gvx));
			}
		};
	}
	

	@Override
	public String getTitulo() {
		return "FlappyBirdByBlackFyree";
	}

	@Override
	public int getLargura() {
		return 384;
	}

	@Override
	public int getAltura() {
		return 512;
	}

	@Override
	public void tecla(String tecla) {
		if (tecla.equals(" ")) {
			passaro.flap();
		}
		
	}
	
	@Override
	public void tique(Set<String> teclas, double dt) {
		scenario_offset += dt * 25;
		scenario_offset = scenario_offset % 288;
		ground_offset += dt * gvx;
		ground_offset = ground_offset % 308;
		
		timer_cano.tique(dt);
		
		passaro.atualiza(dt);
		if (passaro.y+24 >= getAltura()-112) {
			System.out.println("GAME OVER");		
		} else if(passaro.y <= 0) {
			System.out.println("GAME OVER");
		}
		
		for (Cano cano: canos) {
			cano.atualiza(dt);
			if (passaro.box.intersecao(cano.boxcima) != 0 || passaro.box.intersecao(cano.boxbaixo) !=0) {
				System.out.println("GAME OVER");
				
			}
		}
		
		if (canos.size()>0 && canos.get(0).x < -50) {
			canos.remove(0);
		}
		
	}	
	@Override
	public void desenhar(Tela tela) {
		//FUNDO
		tela.imagem("flappy.png", 0, 0, 288, 512, 0, (int) -scenario_offset, 0);
		tela.imagem("flappy.png", 0, 0, 288, 512, 0, (int) (288 - scenario_offset), 0);
		tela.imagem("flappy.png", 0, 0, 288, 512, 0, (int)  ((288*2) - scenario_offset), 0);
		
		for (Cano cano: canos) {
			cano.desenha(tela);
		}
		
		//CHAO
		tela.imagem("flappy.png", 292, 0, 308, 112, 0, -ground_offset, getAltura() - 112);
		tela.imagem("flappy.png", 292, 0, 308, 112, 0, 208 - ground_offset, getAltura() - 112);
		tela.imagem("flappy.png", 292, 0, 308, 112, 0, 208 * 2 - ground_offset, getAltura() - 112);
		
		
		
		passaro.desenhar(tela);
	}

	public static void main(String[] args) {
		roda();
	}

	private static void roda() {
		new Motor(new FlappyBird());
		
	}
	
	
}
