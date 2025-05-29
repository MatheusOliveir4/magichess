# MagiChess
Magichess é um projeto idealizado e executado por Pedro Mota Mendes, Matheus Oliveira, Artur Lima Pinto Bacalhau e Davi Magalhaes para a cadeira de Programação Orientada a Objetos do professor Lucas Rodolfo Celestino de Farias, mestrada na Universidade Católica de Penambuco, Periodo 2025.1.

MagiChess é um jogo de xadrez por turnos com cartas mágicas, desenvolvido em Java, que adiciona elementos estratégicos e imprevisíveis ao tradicional xadrez. Cada jogador pode usar cartas especiais para alterar o curso da partida, criando novas possibilidades táticas e desafios.

## Funcionalidades

- **Xadrez Clássico:** Todas as regras tradicionais do xadrez são mantidas.
- **Sistema de Cartas:** Cada jogador possui uma mão de cartas mágicas, que podem ser usadas para influenciar o tabuleiro, bloquear movimentos, reviver peças, silenciar oponentes e muito mais.
- **Cooldown e Descarte:** Cartas usadas entram em cooldown e vão para o descarte, retornando à mão apenas quando o tempo de recarga termina.
- **Limite de Cartas por Turno:** Só é possível usar uma carta por turno.
- **Efeitos Especiais:** Colunas congeladas, barreiras, silêncios, teleporte de peças, entre outros efeitos mágicos.
- **Interface de Texto:** O jogo roda no terminal, com comandos simples para jogar, mover peças e usar cartas.

## Como Jogar

1. **Inicie o jogo:**  
   Compile e execute o arquivo `Main.java`.

2. **Comandos disponíveis:**  
   - `move O D` — Move uma peça da origem O para o destino D (ex: `move e2 e4`)
   - `use N` — Usa a carta de número N da sua mão
   - `help` — Mostra a ajuda
   - `exit` — Sai do jogo

3. **Cartas:**  
   - Cada jogador começa com 2 cartas e recebe uma nova a cada turno (exceto no primeiro turno do preto).
   - Só é possível usar uma carta por turno.
   - Cartas usadas vão para o descarte e só podem ser reutilizadas após o cooldown.

## Estrutura do Projeto

- `src/models/` — Modelos principais do jogo (peças, jogadores, cartas, tabuleiro)
- `src/models/CardSystem/Cards/` — Implementação das cartas mágicas
- `src/models/GameLogic.java` — Lógica principal do jogo
- `src/Main.java` — Ponto de entrada do jogo

## Requisitos

- Java 8 ou superior

## Como compilar e executar

No terminal, execute:

```sh
javac -d bin src/**/*.java
java -cp bin Main