import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Hangman {
	/*
	 * Jogo da forca(hangman)
	 * Passado como exercício de aprendizagem do curso da Academia do Java, VOFFICE
	 * pelo instrutor Vinny
	 * Aluno: Ladislau Felisbino
	 * */
	
	public static void main(String[] args) {
		
Scanner sc = new Scanner(System.in);

		//O jogador se identifica e o jogo apresenta o objetivo ao jogador
		
		System.out.println("Informe seu nome:");
		String nome = sc.nextLine();
		System.out.println("Ola " + nome + " bem vindo ao Hangman Game!!!");
		System.out.println("O objetivo do jogo e acertar a palavra sorteada no minimo de tentativas possiveis.");
		System.out.println("Teremos dois niveis neste jogo:");
		System.out.println("No primeiro vc, " + nome + ", tera que tentar acertar uma entre as 50 palavras reservadas do java,");
		System.out.println("No segundo nivel uma entre as 10 palavras reservadas do java relacionadas a tipos,");
		System.out.println("Isto envolve os tipos primitivos, o unico operador que e palavra reservada e a palavra reservada usada quando um metodo nao retorna valor.");
		System.out.println("A cada acerto a letra aparecera no console e voce podera tentar a proxima letra.");
		System.out.println("A cada erro uma parte da forca e criada.");
		System.out.println("No primeiro nivel voce podera errar ate 5 letras, no sexto erro o personagem e enforcado e o jogo acaba.");
		System.out.println("Aperte ENTER quando estiver pronto e que o jogo comece!!!");
		sc.nextLine();
		
		/*
		 * Criando e inicializando os arrays de String com as palavras dos dois níveis.
		 * OBS.: Eu sei que poderia inicializar o array de strings do nível 2 apenas em caso de o jogador passar para o nível 2
		 * mas nesta fase do desenvolvimento o impacto gerado por essa alocação de memória não impacta no desempenho do jogo
		 * portanto preferi deixar assim e me preocupar mais em fazer funcionar!!
		 * */
		
		String[] wordsLevel1 = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
							    "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
							    "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
							    "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
							    "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while"};

		String[] wordsLevel2 = {"boolean", "byte", "char", "double", "float", "instanceof", "int", "long", "short", "void"};
		
				
		//Criando as variáveis que vão servir de referência para o limite de tentativas de cada nível
		int maxGuesses = 0;
		int level = 1;
		String[] words = {};
			
		//Escolha aleatória das palavras
		Random choosedWord = new Random();
		
		char guess = ' ';
		
		//variáveis que irão controlar o andamento do jogo
		int tries = 0;
		int played = 1;
		boolean finished = false;
		boolean wordWasDiscovered = false;
		
		//Vou criar uma lista de caracteres digitados porque percebi que se eu digitar uma letra que já digitei e está certa
		//o programa decrementa a variável "tries".
		ArrayList<Character> tipedLetters = new ArrayList<Character>();
			
			//Enquanto o status do jogo não for "Finished"
			while(!finished) {
				wordWasDiscovered = false;
				tries = 0;
				played = 1;
				
				//Verificar o nível do jogo e adequar as palavras e as chances do jogador
				if(level == 1) {
					maxGuesses = 6;
					words = wordsLevel1;
				} else if(level == 2) {
					maxGuesses = 5;
					words = wordsLevel2;
				} else {
					finished = true;
				}
				
				//pegar a palavra escolhida aleatoriamente e transformar em um array de caracteres
				char[] wordToGuess = words[choosedWord.nextInt(words.length)].toCharArray();
				
				//Verifica o numero de caracteres da palavra e imprime "_" na tela
				int amountOfChars = wordToGuess.length;
				char[] showedSpaces = new char[amountOfChars];
				for (int i = 0; i < showedSpaces.length; i++){
					showedSpaces[i] = '_';
				}
				
				//Enquanto a palavra não for descoberta e a quantidade de tentativas não ultrapassar o limite
				while(!wordWasDiscovered && tries != maxGuesses){
					clearScreen();
					hangmanImage(tries, level);
					System.out.println("Tentativa: " + played);
					System.out.printf("Voce pode errar %d vezes.\n", maxGuesses - tries);
					printArray(showedSpaces);
					System.out.println("digite uma letra");
					guess = sc.nextLine().charAt(0);
	
					//Desconsiderar letras já digitadas anteriormente
					if(tipedLetters.contains(guess)){
						System.out.println("Letra já digitada anteriormente, tente outra");
						continue;
					}
	
					tries++;
					played++;
					
					//Se o jogador quiser sair do jogo basta digitar o caractere underscore
					if (guess == '_'){
						finished = true;
						break;
					//Enquanto houver caractere para descobrir...	
					} else {
						for(int i = 0; i < wordToGuess.length; i++){
		
							if(wordToGuess[i] == guess){
								showedSpaces[i] = guess;
								//Essa linha a seguir coloquei porque percebi que quando eu acertava a primeira letra
								//o jogo incrementava a quantidade de possibilidades de erros
								if(tries <= maxGuesses) tries--;
							}
		
						}
						//Se o jogador acertar a palavra
						if (isTheWordGuessed(showedSpaces)){
							wordWasDiscovered = true;
							System.out.println("Parabens, voce acertou a palavra!!! ");
							printArray(wordToGuess);
							System.out.println();
							if(level == 1) {
								System.out.println("No segundo nivel voce podera errar ate 4 letras, no quinto erro o personagem e enforcado e o jogo tambem acaba.");
								System.out.println("Aperte ENTER quando estiver pronto e vamos continuar!!!");
								sc.nextLine();
								level++;
							} else {
								finished = true;
							}
						}
					}
				}
				//Se a palavra não for descoberta
				if(!wordWasDiscovered){
					clearScreen();
					hangmanImage(tries, level);
					System.out.println("Acabaram as suas chances!!!");
					System.out.println("A palavra era:");
					printArray(wordToGuess);
					System.out.println();
					finished = true;
				} 
			}
	
			System.out.println("Fim de Jogo!!!");
			finished = true;
	
		}
	

	//Este método lê o array de caracteres e imprime
	public static void printArray(char[] array){
		for (int i = 0; i < array.length; i++){
			System.out.print(array[i] + " ");
		}

		System.out.println();
	}
	
	//Método que verifica se todos os '_'(underscores) já foram substituídos por letras e determina se o jogador descobriu a palavra
	public static boolean isTheWordGuessed(char[] array){
		for (int i = 0; i < array.length; i++){
			if(array[i] =='_') return false;
		}
		return true;
	}
	//Método criado para limpar a tela após cada rodada
	public static void clearScreen() { 
		for (int i = 0; i < 300; ++i)
    	System.out.println ();
	}

	//Método para desenhar a forca no console usando "switch", a variável tries pq ela incrementa com a quantidade de erros e o level pra diferenciar
	public static void hangmanImage(int tries, int level) {
			if(level == 1){
			switch (tries) {
				case 0:
				System.out.println("Nivel "+ level);
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println("___|___");
				System.out.println();
				break;

				case 1:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("___|___");
				break;

				case 2:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   ____________");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   | ");
				System.out.println("___|___");
				break;

				case 3:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   ____________");
				System.out.println("   |          _|_");
				System.out.println("   |         /   \\");
				System.out.println("   |        |     |");
				System.out.println("   |         \\_ _/");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("___|___");
				break;

				case 4:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   ____________");
				System.out.println("   |          _|_");
				System.out.println("   |         /   \\");
				System.out.println("   |        |     |");
				System.out.println("   |         \\_ _/");
				System.out.println("   |           |");
				System.out.println("   |           |");
				System.out.println("   |");
				System.out.println("___|___");
				break;

				case 5:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   ____________");
				System.out.println("   |          _|_");
				System.out.println("   |         /   \\");
				System.out.println("   |        |     |");
				System.out.println("   |         \\_ _/");
				System.out.println("   |           |");
				System.out.println("   |           |");
				System.out.println("   |          / \\ ");
				System.out.println("___|___      /   \\");
				break;

				case 6:
				System.out.println("ACABOU, VOCE PERDEU!!!!!");
				System.out.println("   ____________");
				System.out.println("   |          _|_");
				System.out.println("   |         /   \\");
				System.out.println("   |        |     |");
				System.out.println("   |         \\_ _/");
				System.out.println("   |          _|_");
				System.out.println("   |         / | \\");
				System.out.println("   |          / \\ ");
				System.out.println("___|___      /   \\");
				break;
			}
		}

		if(level == 2){
			switch (tries) {

				case 0:
				System.out.println("Nivel "+ level +" Vamos recomeçar");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("___|___");
				break;

				case 1:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   ____________");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   | ");
				System.out.println("___|___");
				break;

				case 2:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   ____________");
				System.out.println("   |          _|_");
				System.out.println("   |         /   \\");
				System.out.println("   |        |     |");
				System.out.println("   |         \\_ _/");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("   |");
				System.out.println("___|___");
				break;

				case 3:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   ____________");
				System.out.println("   |          _|_");
				System.out.println("   |         /   \\");
				System.out.println("   |        |     |");
				System.out.println("   |         \\_ _/");
				System.out.println("   |           |");
				System.out.println("   |           |");
				System.out.println("   |");
				System.out.println("___|___");
				break;

				case 4:
				System.out.println("Nivel "+ level +" Errou, Tente novamente");
				System.out.println("   ____________");
				System.out.println("   |          _|_");
				System.out.println("   |         /   \\");
				System.out.println("   |        |     |");
				System.out.println("   |         \\_ _/");
				System.out.println("   |           |");
				System.out.println("   |           |");
				System.out.println("   |          / \\ ");
				System.out.println("___|___      /   \\");
				break;

				case 5:
				System.out.println("ACABOU, VOCE PERDEU!!!!!");
				System.out.println("   ____________");
				System.out.println("   |          _|_");
				System.out.println("   |         /   \\");
				System.out.println("   |        |     |");
				System.out.println("   |         \\_ _/");
				System.out.println("   |          _|_");
				System.out.println("   |         / | \\");
				System.out.println("   |          / \\ ");
				System.out.println("___|___      /   \\");
				break;
			}
		}
	}

}