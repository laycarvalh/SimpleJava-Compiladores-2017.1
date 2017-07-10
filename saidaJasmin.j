.class publicprog
.super java/lang/Object
; _____________________________inicio funcao read de inteiros_____________________________
.method public static read()I

   .limit stack 10  ; up to five items can be pushed
   .limit locals 100
	
	; __________________________________________________
	
	; inicializa flag que indica se o n�mero � negativo
	
	ldc 0
	
	; posicao que armazena zero para vari�vel positiva e 1 para vari�vel negativa
	
	istore 50
	
	; __________________________________________________
   
	; Inicializa o acumulador
   
	; carrega a constante zero na pilha
   
    ldc 0
	
	; inteiro para o resultado armazenado no endere�o 52 (Acumulador)
	
	istore 52
	
	; __________________________________________________
 Loop:
	
	; __________________________________________________
	
	; Inicia Leitura
	
	; faz a leitura de um caractere
	
    getstatic java/lang/System/in Ljava/io/InputStream; ; chama classe de entrada do usu�rio
    invokevirtual java/io/InputStream/read()I ; entrada de um inteiro pelo usu�rio
	
	; armazena o caractere lido nas vari�veis locais
    istore 51 ; valor entrado pelo usu�rio armazenado no endere�o 1
	
	; __________________________________________________

	; Inicia compara��es de quebra de ciclo
	
	; Compara��o com retorno de carro
	
	; carrega o valor lido
	
    iload 51
	
	; c�digo ascii para retorno de carro em windows
	
    ldc 13
	
	; subtrai o valor digitado pelo valor de parada deixando o valor da subtra��o na pilha
	
    isub 

	; se o valor de subtra�ao for zero ifeq pula para o Loop, para ler o caractere de nova linha antes de sair da leitura
	
    ifeq RetornoDeCarro
	
	; __________________________________________________
   
    ; verifica se o caractere lido � uma nova linha
	
	; carrega o valor lido
	
    iload 51
	
	; carrega o c�digo ascii da nova linha
	
    ldc 10
	
	; subtrai esses dois valores
	
    isub
	
	; se a subtra��o for zero, termina o loop
	
    ifeq NovaLinha ; se o valor de subtra�ao for zero ifeq salta para TerminarLoop
	
	; __________________________________________________
	
	; verifica se o valor lido � um espa�o em branco
		
	; carrega o valor lido
	
	iload 51
	
	; carrega o c�digo ascii do espa�o em branco
		
    ldc 32
	
	; faz a subtra��o entre o valor lido e o c�digo ascii do espa�o em branco
	
    isub
	
	; testa o resultado
	
	ifeq EspacoBranco
	
	; __________________________________________________
	
	; verifica se o valor lido � um sinal negativo
	
	; carrega o valor lido
	
	iload 51
	
	; carrega o c�digo ascii do sinal negativo
		
    ldc 45
	
	; faz a subtra��o entre o valor lido e o c�digo ascii do espa�o em branco
	
    isub

	; testa o resultado
	
	ifeq MudaSinal
	
	; __________________________________________________
	
	; Caso o valor lido seja um n�mero
	
	; carrega o valor lido
	
	; verifica flag decimal
	
	iload 51
	
	; carrega o c�digo ascii para o valor zero
	
    ldc 48
	
	; subtrai os dois valores, ao final dessa subtra��o o valor inteiro do caractere estar� na mem�ria
	
    isub
	
	; carrega a constante 10 para a mem�ria, ser� usada para multiplicar o valor do acumulador
	
    ldc 10
	
	; carrega o valor do acumulador
	
    iload 52
	
	; multiplica o valor acumulado por 10
	
    imul
	
	; soma o valor acumulado multiplicado por 10, pelo valor lido recentemente
	
    iadd
	
	; salva o resultado no acumulador
	
    istore 52
	
	; reinicia o loop
	
    goto Loop
  
  ; __________________________________________________
  
	MudaSinal:
	
	ldc 1
	
	istore 50
	
	goto Loop
	
	; __________________________________________________
	
	NovaLinha:
	
	goto TerminarLoop
	
	; __________________________________________________
	
	EspacoBranco:
	
	goto TerminarLoop
	
	; __________________________________________________
	
	RetornoDeCarro:
	
	goto Loop
	
	; __________________________________________________
	
	TerminarLoop:
	
	; __________________________________________________
	
	; verifica se o n�mero � negativo
	
	iload 50
	
	ifeq RetornoPositivo
	
	; carrega o valor zero para a pilha
	
	ldc 0
	
	; carrega o valor do acumulador
	
    iload 52
	
	isub
	
	goto Retorno
	
	; __________________________________________________
	
	RetornoPositivo:
	
	; carrega o valor do acumulador para a pilha
	
    iload 52
	
	Retorno:
	
    ireturn
	
.end method
; _____________________________fim funcao read de inteiros_____________________________
; _____________________________inicio funcao read de float_____________________________
.method public static scan()F

   .limit stack 10  ; up to five items can be pushed
   .limit locals 100
	
	; __________________________________________________
	
	; inicializa flag que redireciona o fluxo para incluir os valores decimais
	
	ldc 0
	
	istore 48 ; flag loop decimal
	
	; __________________________________________________
	
	; inicializa valor que gera decimais
	
	ldc 10.0
	
	fstore 49
	
	; __________________________________________________
	
	; inicializa flag que indica se o n�mero � negativo
	
	ldc 0
	
	; posicao que armazena zero para vari�vel positiva e 1 para vari�vel negativa
	
	istore 50
	
	; __________________________________________________
   
	; Inicializa o acumulador
   
	; carrega a constante zero na pilha
   
    ldc 0.0
	
	; inteiro para o resultado armazenado no endere�o 52 (Acumulador)
	
	fstore 52
	
	; __________________________________________________
 Loop:
	
	; __________________________________________________
	
	; Inicia Leitura
	
	; faz a leitura de um caractere
	
    getstatic java/lang/System/in Ljava/io/InputStream; ; chama classe de entrada do usu�rio
    invokevirtual java/io/InputStream/read()I ; entrada de um inteiro pelo usu�rio
	
	; armazena o caractere lido nas vari�veis locais
    istore 51 ; valor entrado pelo usu�rio armazenado no endere�o 1

	; imprime o caractere lido
	
	; __________________________________________________

	; Inicia compara��es de quebra de ciclo
	
	; Compara��o com retorno de carro
	
	; carrega o valor lido
	
    iload 51
	
	; c�digo ascii para retorno de carro em windows
	
    ldc 13
	
	; subtrai o valor digitado pelo valor de parada deixando o valor da subtra��o na pilha
	
    isub 

	; se o valor de subtra�ao for zero ifeq pula para o Loop, para ler o caractere de nova linha antes de sair da leitura
	
    ifeq Loop
	
	; __________________________________________________
   
    ; verifica se o caractere lido � uma nova linha
	
	; carrega o valor lido
	
    iload 51
	
	; carrega o c�digo ascii da nova linha
	
    ldc 10
	
	; subtrai esses dois valores
	
    isub
	
	; se a subtra��o for zero, termina o loop
	
    ifeq TerminarLoop ; se o valor de subtra�ao for zero ifeq salta para TerminarLoop
	
	; __________________________________________________
	
	; verifica se o valor lido � um espa�o em branco
		
	; carrega o valor lido
	
	iload 51
	
	; carrega o c�digo ascii do espa�o em branco
		
    ldc 32
	
	; faz a subtra��o entre o valor lido e o c�digo ascii do espa�o em branco
	
    isub
	
	; testa o resultado
	
	ifeq TerminarLoop
	
	; __________________________________________________
	
	; verifica se o valor lido � um sinal negativo
	
	; carrega o valor lido
	
	iload 51
	
	; carrega o c�digo ascii do sinal negativo
		
    ldc 45
	
	; faz a subtra��o entre o valor lido e o c�digo ascii do espa�o em branco
	
    isub

	; testa o resultado
	
	ifeq MudaSinal
	
	; __________________________________________________
	
	; verifica se o valor lido � um ponto
	
	; carrega o valor lido
	
	iload 51
	
	; carrega o c�digo ascii do sinal negativo
		
    ldc 46
	
	; faz a subtra��o entre o valor lido e o c�digo ascii do espa�o em branco
	
    isub

	; testa o resultado
	
	ifeq MudaFlagDecimal
	
	; __________________________________________________
	
	; Caso o valor lido seja um n�mero
	
	; carrega o valor lido
	
	; verifica flag decimal
	
	iload 48
	
	ldc 1
	
	isub
	
	ifeq SomarDecimais
	
	iload 51
	
	; carrega o c�digo ascii para o valor zero
	
    ldc 48
	
	; subtrai os dois valores, ao final dessa subtra��o o valor inteiro do caractere estar� na mem�ria
	
    isub
	
	; carrega a constante 10 para a mem�ria, ser� usada para multiplicar o valor do acumulador
	
	i2f
	
    ldc 10.0
	
	; carrega o valor do acumulador
	
    fload 52
	
	; multiplica o valor acumulado por 10
	
    fmul
	
	; soma o valor acumulado multiplicado por 10, pelo valor lido recentemente
	
    fadd
	
	; salva o resultado no acumulador
	
    fstore 52
	
	; reinicia o loop
	
    goto Loop
  
  ; __________________________________________________
  
	MudaSinal:
	
	ldc 1
	
	istore 50
	
	goto Loop
	
	; __________________________________________________
	
	MudaFlagDecimal:
	
	ldc 1
	
	istore 48
	
	goto Loop
	
	; __________________________________________________
	
	SomarDecimais:
	
	iload 51
	
	ldc 48
	
	isub
	
	i2f
	
	fload 49
	
	fdiv
	
	fload 52
	
	fadd
	
	fstore 52
	
	fload 49
	
	ldc 10.0
	
	fmul
	
	fstore 49
	
	goto Loop
	
	; __________________________________________________
	
	TerminarLoop:
	
	; __________________________________________________
	
	; verifica se o n�mero � negativo
	
	iload 50
	
	ifeq RetornoPositivo
	
	; carrega o valor zero para a pilha
	
	ldc 0.0
	
	; carrega o valor do acumulador
	
    fload 52
	
	fsub
	
	goto Retorno
	
	; __________________________________________________
	
	RetornoPositivo:
	
	; carrega o valor do acumulador para a pilha
	
    fload 52
	
	Retorno:
	
    freturn
	
.end method
; _____________________________fim funcao read de float_____________________________
