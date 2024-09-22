# Protocolo-2V2PL
Implementação do protocolo conservador 2V2PL, para controle de concorrência, com suporte à múltipla granulosidade de bloqueio, detecção e prevenção (bloqueio do tipo update) de deadlocks. Na implementação do protocolo, a entrada deverá ser um escalonamento (conjunto de transações e suas operações) qualquer. A saída deve mostrar a sincronização correta das operações do escalonamento fornecido. A detecção de deadlocks deverá utilizar a estratégia do grafo de espera.

pendências:

[x] WaitforGraph: função para: (levy)
    
[x] Verificar a granulosidade

[x] Update lock (bru)

[x] Criar loop para verificar se os bloqueios não concedidos podem ser concedidos apos algum evento de mudança de status (gui)


- Se houver operação de commit c, e ela não puder ser escalonada por que operações anteriores estão ainda aguardando, ela será adicionada na tabela, só 
com TId e  blockType C, status 2
- Se as operações anteriores foram escalonadas, mas c não consegue ser executada
por conflito com alguma das operações de escrita da transação de c para se converter
em certify lock, c também será adicionada na tabela
- Haverá um algoritmo de checagem toda vez que uma tupla tentar adicionada na 
tabela

Entrada: r4(v)r3(y)r1(y)r1(x)w2(u)r2(x)w1(y)r2(y)c1w4(u)r3(x)c4w2(x)c2w3(u)w3(z)c3

1. S = r4(v)r3(y)r1(y)r1(x)r3(x)w1(y)w4(u)c4w3(u)w3(z)c3c1
2. S = r4(v)r3(y)r1(y)r1(x)r3(x)w3(u)w3(z)c3
3. S = r4(v)r3(y)r1(y)r1(x)r3(x)w3(u)w3(z)c3