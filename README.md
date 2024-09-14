# Protocolo-2V2PL
Implementação do protocolo conservador 2V2PL, para controle de concorrência, com suporte à múltipla granulosidade de bloqueio, detecção e prevenção (bloqueio do tipo update) de deadlocks. Na implementação do protocolo, a entrada deverá ser um escalonamento (conjunto de transações e suas operações) qualquer. A saída deve mostrar a sincronização correta das operações do escalonamento fornecido. A detecção de deadlocks deverá utilizar a estratégia do grafo de espera.

duvidas:
- como ficará a tabela de compatibilidade

pendências:
[] WaitforGraph: função para: (levy)
    [] criar grafo
    [] pra verificar se tem loop
    [] se houver ciclo, abortar a transação mais recente
[] Verificar a granulosidade
[] Update lock (bru)
[x] Criar loop para verificar se os bloqueios não concedidos podem ser concedidos apos algum evento de mudança de status (gui)

- Se houver operação de commit c, e ela não puder ser escalonada por que operações anteriores estão ainda aguardando, ela será adicionada na tabela, só 
com TId e  blockType C, status 2
- Se as operações anteriores foram escalonadas, mas c não consegue ser executada
por conflito com alguma das operações de escrita da transação de c para se converter
em certify lock, c também será adicionada na tabela
- Haverá um algoritmo de checagem toda vez que uma tupla tentar adicionada na 
tabela
