# Protocolo-2V2PL
Implementação do protocolo conservador 2V2PL, para controle de concorrência, com suporte à múltipla granulosidade de bloqueio, detecção e prevenção (bloqueio do tipo update) de deadlocks. Na implementação do protocolo, a entrada deverá ser um escalonamento (conjunto de transações e suas operações) qualquer. A saída deve mostrar a sincronização correta das operações do escalonamento fornecido. A detecção de deadlocks deverá utilizar a estratégia do grafo de espera.

pendências:

[x] WaitforGraph: função para: 
    
[x] Verificar a granulosidade

[x] Update lock

[x] Criar loop para verificar se os bloqueios não concedidos podem ser concedidos apos algum evento de mudança de status 


- Se houver operação de commit c, e ela não puder ser escalonada por que operações anteriores estão ainda aguardando, ela será adicionada na tabela, só 
com TId e  blockType C, status 2
- Se as operações anteriores foram escalonadas, mas c não consegue ser executada
por conflito com alguma das operações de escrita da transação de c para se converter
em certify lock, c também será adicionada na tabela
- Haverá um algoritmo de checagem toda vez que uma tupla tentar adicionada na 
tabela

- A operação ui(x) significa que será aplicado um bloqueio do tipo update

r4(v)r3(y)r1(y)r1(x)w2(u)r2(x)w1(y)r2(y)c1   
r4(v)r3(y)r1(y)r1(x)w2(u)r2(x)w1(y)r2(y)c1w4(u)r3(x)c4w2(x)c2w3(u)w3(z)c3
r2(v)r1(x)w2(x)r3(v)r1(y)w3(y)r2(z)w3(z)c3c1c2
r1(a)r2(b)w1(a)r3(c)r4(d)r2(a)w3(b)r1(b)w4(c)r2(c)c1w2(d)w3(e)c2c3c4
r2(b)r3(c)r4(d)r1(a)w1(a)r2(a)w3(b)r1(b)w4(c)r2(c)c1w2(d)w3(e)c2c3c4
r1(a)w1(a)r3(c)r4(d)r2(b)r2(a)w3(b)r1(b)w4(c)r2(c)c1w2(d)w3(e)c2c3c4 **
r3(u)r2(v)w2(u)r3(w)r1(v)w3(x)r4(y)r2(w)w3(u)c2c3c4c1
r2(u)r3(x)r1(x)r1(p)r2(x)w3(u)w2(z)c2w1(x)c1r3(v)r3(p)c3
u3(u)r2(v)w2(u)r3(w)u1(v)w3(x)r4(y)r2(w)w3(u)c2c3c4c1   