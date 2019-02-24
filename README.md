# kartRace - A batalha
Kart Race é um programa onde competidores podem colocar um arquivo de log de suas voltas, em qualquer ordem que desejarem, e o programa se encarregará de fazer toda a ordenação e exibir os dados estatísticos sobre aquela corrida.

# O funcionamento
Para que o programa funcione corretamente, coloque o arquivo na pasta resource, localizada em: "kartRace/src/main/resources", nomeando o arquivo como kartRace.txt.
Mas não se preocupe, se o nome do arquivo não for o mesmo que esperamos, vamos lhe avisar.
Também iremos corrigir a formatação do documento para você, para isso separe os campos com no mínimo 2 espaços entre eles.

Após colocar o arquivo na pasta correspondente, execute o programa pelo framework que achar melhor, aqui, usei o IntelliJ por questões de afinidade.

Ao invés do framework, pode também compilar o projeto por linha de comando e executá-lo.
Abra o terminal, navegando até a pasta principal do projeto e digite: 
```sh
$ cd kartRace
$ mvn clean install package -DskipTests
```
Navegar até a pasta target que foi gerada pela compilação (kartRace/target) e executar:
```sh
$ cd target
$ java -jar kartRace-0.0.1-SNAPSHOT.jar  
```

# A ordem
![alt text](http://i64.tinypic.com/b8aaue.jpg)

Como é exibido na imagem acima, a ordem de chegada é mostrada com algumas estatísticas do piloto como: quantidade de voltas, velocidade média da corrida, melhor volta do piloto, tempo total e para quantidade de tempo que chegou depois do vencedor.

Espero que tenham um ótimo uso do programa.
