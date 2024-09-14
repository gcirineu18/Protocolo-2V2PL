# Protocolo-2V2PL
Implementação do protocolo conservador 2V2PL, para controle de concorrência, com suporte à múltipla granulosidade de bloqueio, detecção e prevenção (bloqueio do tipo update) de deadlocks. Na implementação do protocolo, a entrada deverá ser um escalonamento (conjunto de transações e suas operações) qualquer. A saída deve mostrar a sincronização correta das operações do escalonamento fornecido. A detecção de deadlocks deverá utilizar a estratégia do grafo de espera.

- Se houver operação de commit, ela não puder ser escalonada por que operações anteriores estão ainda aguardando, ela será adicionada na tabela, só 
com TId e  blockType C, status 2
- Se as operações anteriores foram escalonadas, mas c não consegue ser executada
por conflito com alguma das operações de escrita da transação de c, c também será adicionada na tabela

- Haverá um algoritmo de checagem toda vez que uma tupla tentar adicionada na 
tabela
