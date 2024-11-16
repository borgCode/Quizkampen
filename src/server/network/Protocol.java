package server.network;

public enum Protocol {
    WAITING,
    SENT_CATEGORY,
    SENT_QUESTIONS,

//    private static final int WAITING = 0;
//    private static final int SENT_CATEGORY = 1;
//    private static final int SENT_QUESTIONS = 2;
//    private static final int EVALUATED_RESULTS = 3;
//
//    private int state = WAITING;
//
//    public Object processInput(Object o) {
//
//        return null;
//    }
}



/* 
1.Klient skickar spelarobjekt
2. Server kollar om det finns en annan spelare i lobbyn, om inte skickar tillbaka 3 kategorier, om finns så skickar server 3 frågor
3. När spelaren har har besvarat på alla frågorna skickar den reslutatet till servern, alt om spelaren inte har spelat att den tar emot en kategori. 
Spelaren får 3 nya frågor.
4. När bägge parter har besvarat på frågorna kommer resultatet att visas för båda klienterna.
5. Starta upp en ny rond med spel. Variera så att spelaren som inte valt kategori innan får välja. 
6. Slutresultat. Playerklassen (Spara resultatet)
7. Starta upp nytt spel.
 */