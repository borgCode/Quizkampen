package server.logic;

import server.entity.Question;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QuestionBank {
    private HashMap<String, ArrayList<Question>> questionMap;

    public QuestionBank() {
        questionMap = new HashMap<>();
        initQuestions();
    }

    private void initQuestions() {

        //Geografi frågor

        String[] geoOptions1 = {"Afrika", "Europa", "Asien", "Antarktis"};
        Question geoQ1 = new Question("Vilken kontinent är den största till ytan?", geoOptions1, "Asien");

        String[] geoOptions2 = {"Amazonas", "Nilen", "Yangtze", "Mississippi"};
        Question geoQ2 = new Question("Vilken flod är världens längsta?", geoOptions2, "Nilen");

        String[] geoOptions3 = {"Paris", "London", "Rom", "Berlin"};
        Question geoQ3 = new Question("Vilken stad kallas 'Ljusets stad'?", geoOptions3, "Paris");

        String[] geoOptions4 = {"Mount Everest", "K2", "Kangchenjunga", "Lhotse"};
        Question geoQ4 = new Question("Vilket berg är världens högsta?", geoOptions4, "Mount Everest");

        String[] geoOptions5 = {"Moskva", "Berlin", "Peking", "Tokyo"};
        Question geoQ5 = new Question("Vilken stad är världens största i befolkning?", geoOptions5, "Tokyo");

        String[] geoOptions6 = {"Sahara", "Kalahari", "Gobi", "Atacama"};
        Question geoQ6 = new Question("Vilken öken är den största i världen?", geoOptions6, "Sahara");

        ArrayList<Question> geographyList = new ArrayList<>(List.of(geoQ1, geoQ2, geoQ3, geoQ4, geoQ5, geoQ6));
        questionMap.put("geografi", geographyList);


        //Historia

        String[] histOptions1 = {"André the Giant", "Genghis Khan", "Alexander den store", "Napoleon Bonaparte"};
        Question histQ1 = new Question("Vem var ledare för det största sammanhängande imperiet i historien?", histOptions1, "Genghis Khan");

        String[] histOptions2 = {"Första världskriget", "Andra världskriget", "Napoleonskrigen", "Koreakriget"};
        Question histQ2 = new Question("Vilket krig började 1939?", histOptions2, "Andra världskriget");

        String[] histOptions3 = {"Karl XII", "Gustav Vasa", "Olof Skötkonung", "Erik XIV"};
        Question histQ3 = new Question("Vem grundade det svenska kungariket?", histOptions3, "Gustav Vasa");

        String[] histOptions4 = {"Cleopatra", "Nefertiti", "Hatshepsut", "Elizabeth I"};
        Question histQ4 = new Question("Vem var den sista faraonen i Egypten?", histOptions4, "Cleopatra");

        String[] histOptions5 = {"Martin Luther", "Johannes Gutenberg", "Galileo Galilei", "Isaac Newton"};
        Question histQ5 = new Question("Vem uppfann boktryckarkonsten?", histOptions5, "Johannes Gutenberg");

        String[] histOptions6 = {"Independence Day", "Bastilledagen", "Midsommar", "Valborg"};
        Question histQ6 = new Question("Vilket datum firas Bastilledagen, en viktig fransk nationaldag?", histOptions6, "14 juli");

        ArrayList<Question> historyList = new ArrayList<>(List.of(histQ1, histQ2, histQ3, histQ4, histQ5, histQ6));
        questionMap.put("historia", historyList);


        //Vetenskap

        String[] sciOptions1 = {"Albert Einstein", "Isaac Newton", "Nikola Tesla", "Marie Curie"};
        Question sciQ1 = new Question("Vem formulerade relativitetsteorin?", sciOptions1, "Albert Einstein");

        String[] sciOptions2 = {"Hydrogen", "Oxygen", "Nitrogen", "Carbon"};
        Question sciQ2 = new Question("Vilket är det lättaste grundämnet i det periodiska systemet?", sciOptions2, "Hydrogen");

        String[] sciOptions3 = {"Mars", "Venus", "Jupiter", "Saturnus"};
        Question sciQ3 = new Question("Vilken planet i vårt solsystem är känd som den röda planeten?", sciOptions3, "Mars");

        String[] sciOptions4 = {"Proton", "Neutron", "Electron", "Photon"};
        Question sciQ4 = new Question("Vilken subatomär partikel har en negativ laddning?", sciOptions4, "Electron");

        String[] sciOptions5 = {"Andromedagalaxen", "Vintergatan", "Triangelgalaxen", "Sombrerogalaxen"};
        Question sciQ5 = new Question("Vad heter galaxen som jorden är en del av?", sciOptions5, "Vintergatan");

        String[] sciOptions6 = {"Klorofyll", "Hemoglobin", "Keratin", "Insulin"};
        Question sciQ6 = new Question("Vilket ämne ger växter deras gröna färg och är viktigt för fotosyntesen?", sciOptions6, "Klorofyll");

        ArrayList<Question> scienceList = new ArrayList<>(List.of(sciQ1, sciQ2, sciQ3, sciQ4, sciQ5, sciQ6));
        questionMap.put("vetenskap", scienceList);

        //Sport

        String[] sportOptions1 = {"Pele", "Diego Maradona", "Lionel Messi", "Cristiano Ronaldo"};
        Question sportQ1 = new Question("Vem är känd för att ha gjort 'Guds hand'-målet i VM 1986?", sportOptions1, "Diego Maradona");

        String[] sportOptions2 = {"Basket", "Fotboll", "Baseboll", "Tennis"};
        Question sportQ2 = new Question("Vilken sport spelas med en boll och en basketkorg?", sportOptions2, "Basket");

        String[] sportOptions3 = {"Roger Federer", "Rafael Nadal", "Novak Djokovic", "Andy Murray"};
        Question sportQ3 = new Question("Vem har vunnit flest Grand Slam-titlar i tennis (herrsingel) fram till 2023?", sportOptions3, "Novak Djokovic");

        String[] sportOptions4 = {"Tour de France", "Giro d'Italia", "Vuelta a España", "Paris-Roubaix"};
        Question sportQ4 = new Question("Vilket är världens mest kända cykellopp?", sportOptions4, "Tour de France");

        String[] sportOptions5 = {"Michael Jordan", "Kobe Bryant", "LeBron James", "Shaquille O'Neal"};
        Question sportQ5 = new Question("Vilken basketspelare är känd för att bära tröja nummer 23 i Chicago Bulls?", sportOptions5, "Michael Jordan");

        String[] sportOptions6 = {"Golf", "Hockey", "Rugby", "Cricket"};
        Question sportQ6 = new Question("Vilken sport spelas vid Ryder Cup?", sportOptions6, "Golf");

        ArrayList<Question> sportsList = new ArrayList<>(List.of(sportQ1, sportQ2, sportQ3, sportQ4, sportQ5, sportQ6));
        questionMap.put("sport", sportsList);

        //Nöje

        String[] entOptions1 = {"The Godfather", "Titanic", "The Shawshank Redemption", "Pulp Fiction"};
        Question entQ1 = new Question("Vilken film är känd för citatet 'I'm gonna make him an offer he can't refuse'?", entOptions1, "The Godfather");

        String[] entOptions2 = {"Taylor Swift", "Beyoncé", "Adele", "Lady Gaga"};
        Question entQ2 = new Question("Vem sjöng hits som 'Rolling in the Deep' och 'Someone Like You'?", entOptions2, "Adele");

        String[] entOptions3 = {"Game of Thrones", "Breaking Bad", "The Sopranos", "Friends"};
        Question entQ3 = new Question("Vilken TV-serie handlar om en familj som slåss om kontrollen över de sju kungadömena i Westeros?", entOptions3, "Game of Thrones");

        String[] entOptions4 = {"Marvel", "DC", "Pixar", "Lucasfilm"};
        Question entQ4 = new Question("Vilket filmstudio-universum inkluderar karaktärer som Iron Man, Thor och Captain America?", entOptions4, "Marvel");

        String[] entOptions5 = {"Elvis Presley", "Michael Jackson", "Freddie Mercury", "Prince"};
        Question entQ5 = new Question("Vem kallas 'The King of Pop'?", entOptions5, "Michael Jackson");

        String[] entOptions6 = {"The Beatles", "The Rolling Stones", "Queen", "Led Zeppelin"};
        Question entQ6 = new Question("Vilket brittiskt band är känt för låtar som 'Hey Jude' och 'Let It Be'?", entOptions6, "The Beatles");

        ArrayList<Question> entertainmentList = new ArrayList<>(List.of(entQ1, entQ2, entQ3, entQ4, entQ5, entQ6));
        questionMap.put("nöje", entertainmentList);

        //Literatur

        String[] litOptions1 = {"J.K. Rowling", "J.R.R. Tolkien", "George R.R. Martin", "C.S. Lewis"};
        Question litQ1 = new Question("Vem skrev 'Sagan om ringen'-trilogin?", litOptions1, "J.R.R. Tolkien");

        String[] litOptions2 = {"Romeo och Julia", "Hamlet", "Macbeth", "Othello"};
        Question litQ2 = new Question("Vilket Shakespeare-verk innehåller citatet 'Att vara eller inte vara, det är frågan'?", litOptions2, "Hamlet");

        String[] litOptions3 = {"1984", "Brave New World", "Fahrenheit 451", "The Handmaid's Tale"};
        Question litQ3 = new Question("Vilken dystopisk roman skrevs av George Orwell?", litOptions3, "1984");

        String[] litOptions4 = {"Homer", "Virgil", "Sophocles", "Herodotus"};
        Question litQ4 = new Question("Vem är författaren till 'Iliaden' och 'Odysséen'?", litOptions4, "Homer");

        String[] litOptions5 = {"Jane Eyre", "Stolthet och fördom", "Emma", "Wuthering Heights"};
        Question litQ5 = new Question("Vilken roman skrevs av Jane Austen och handlar om Elizabeth Bennet och Mr. Darcy?", litOptions5, "Stolthet och fördom");

        String[] litOptions6 = {"Frankenstein", "Dracula", "Dorian Grays porträtt", "Dr. Jekyll och Mr. Hyde"};
        Question litQ6 = new Question("Vilken klassisk roman skrevs av Mary Shelley?", litOptions6, "Frankenstein");

        ArrayList<Question> literatureList = new ArrayList<>(List.of(litQ1, litQ2, litQ3, litQ4, litQ5, litQ6));
        questionMap.put("literatur", literatureList);

        //TV spel

        String[] gameOptions1 = {"Super Mario", "The Legend of Zelda", "Donkey Kong", "Metroid"};
        Question gameQ1 = new Question("Vilken är den mest kända plattformsspelserien skapad av Nintendo?", gameOptions1, "Super Mario");

        String[] gameOptions2 = {"Minecraft", "Fortnite", "Among Us", "Roblox"};
        Question gameQ2 = new Question("Vilket spel är känt för sin pixelgrafik och blockiga värld, där spelare kan bygga och överleva?", gameOptions2, "Minecraft");

        String[] gameOptions3 = {"The Elder Scrolls V: Skyrim", "Fallout 4", "World of Warcraft", "Dragon Age: Inquisition"};
        Question gameQ3 = new Question("Vilken rollspelsserie är känd för sina öppna världar och episka berättelser om fantasy?", gameOptions3, "The Elder Scrolls V: Skyrim");

        String[] gameOptions4 = {"Call of Duty", "Battlefield", "Halo", "Counter-Strike"};
        Question gameQ4 = new Question("Vilken populär first-person shooter-serie är känd för sina strider och krigsoperationer?", gameOptions4, "Call of Duty");

        String[] gameOptions5 = {"Overwatch", "League of Legends", "Dota 2", "Valorant"};
        Question gameQ5 = new Question("Vilket spel är känt för sitt fokus på teambaserad taktik och hjältekaraktärer?", gameOptions5, "Overwatch");

        String[] gameOptions6 = {"Pokémon", "Final Fantasy", "Dragon Quest", "Chrono Trigger"};
        Question gameQ6 = new Question("Vilken populär japansk RPG-serie handlar om att fånga och träna varelser i en fiktiv värld?", gameOptions6, "Pokémon");

        ArrayList<Question> gamesList = new ArrayList<>(List.of(gameQ1, gameQ2, gameQ3, gameQ4, gameQ5, gameQ6));
        questionMap.put("spel", gamesList);

        //Mat

        String[] foodOptions1 = {"Sushi", "Tacos", "Pizza", "Pasta"};
        Question foodQ1 = new Question("Vilken rätt kommer ursprungligen från Italien och består av en rund degbas med tomatsås och ost?", foodOptions1, "Pizza");

        String[] foodOptions2 = {"Sushi", "Dim Sum", "Pad Thai", "Ramen"};
        Question foodQ2 = new Question("Vilken japansk rätt består ofta av ris, rå fisk och andra ingredienser?", foodOptions2, "Sushi");

        String[] foodOptions3 = {"Gnocchi", "Frittata", "Risotto", "Lasagne"};
        Question foodQ3 = new Question("Vilken italiensk rätt är en krämig risbaserad maträtt som ofta innehåller svamp eller skaldjur?", foodOptions3, "Risotto");

        String[] foodOptions4 = {"Apple", "Banana", "Mango", "Pineapple"};
        Question foodQ4 = new Question("Vilken frukt är känd för att vara gul när den är mogen och har en söt, mjuk smak?", foodOptions4, "Banana");

        String[] foodOptions5 = {"Baguette", "Ciabatta", "Sourdough", "Pita"};
        Question foodQ5 = new Question("Vilken typ av bröd är lång och smal och kommer från Frankrike?", foodOptions5, "Baguette");

        String[] foodOptions6 = {"Tiramisu", "Gelato", "Cannoli", "Panna Cotta"};
        Question foodQ6 = new Question("Vilken italiensk efterrätt är gjord på mascarpone, kaffe och kakao?", foodOptions6, "Tiramisu");

        ArrayList<Question> foodList = new ArrayList<>(List.of(foodQ1, foodQ2, foodQ3, foodQ4, foodQ5, foodQ6));
        questionMap.put("mat", foodList);

        //TV serier

        String[] tvOptions1 = {"Breaking Bad", "The Sopranos", "Game of Thrones", "Friends"};
        Question tvQ1 = new Question("Vilken TV-serie handlar om en kemilärare som blir en droghandlare?", tvOptions1, "Breaking Bad");

        String[] tvOptions2 = {"Stranger Things", "The Mandalorian", "The Witcher", "Westworld"};
        Question tvQ2 = new Question("Vilken TV-serie utspelar sig i en värld där en grupp barn stöter på övernaturliga krafter?", tvOptions2, "Stranger Things");

        String[] tvOptions3 = {"The Office", "Parks and Recreation", "Brooklyn Nine-Nine", "Arrested Development"};
        Question tvQ3 = new Question("Vilken mockumentary-serie följer livet på ett kontor i Scranton, Pennsylvania?", tvOptions3, "The Office");

        String[] tvOptions4 = {"The Crown", "Downton Abbey", "Vikings", "The Tudors"};
        Question tvQ4 = new Question("Vilken TV-serie handlar om det brittiska kungahuset och drottning Elizabeth II:s regeringstid?", tvOptions4, "The Crown");

        String[] tvOptions5 = {"Friends", "How I Met Your Mother", "Big Bang Theory", "Two and a Half Men"};
        Question tvQ5 = new Question("Vilken TV-serie handlar om ett gäng vänner som bor i New York?", tvOptions5, "Friends");

        String[] tvOptions6 = {"Narcos", "Breaking Bad", "Peaky Blinders", "Ozark"};
        Question tvQ6 = new Question("Vilken serie handlar om en colombiansk drogkartell och dess ledare, Pablo Escobar?", tvOptions6, "Narcos");

        ArrayList<Question> tvList = new ArrayList<>(List.of(tvQ1, tvQ2, tvQ3, tvQ4, tvQ5, tvQ6));
        questionMap.put("tv", tvList);


    }

    public ArrayList<Question> getRandomQuestionsByCategory(String category) {
        ArrayList<Question> questions = new ArrayList<>(questionMap.get(category));

        if (questions.isEmpty()) {
            System.out.println("No such category exists or there are no questions in the category");
            return new ArrayList<>();
        }

        //blanda frågorna och returnera en lista med 3 frågor
        Collections.shuffle(questions);
        return new ArrayList<>(questions.subList(0, 3));

    }

}