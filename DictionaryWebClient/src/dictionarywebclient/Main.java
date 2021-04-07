package dictionarywebclient;


public class Main {

    
    public static void main(String[] args) {
       
        // Offered languages
        String serbian = "sr";
        String english = "en";
        String french  = "fr";
        
        String wordFromTranslate = "Jabuka";
       
        DictionaryClient dc = new DictionaryClient();
        
        System.out.println("Translated word: " + dc.translate(wordFromTranslate, serbian, english));
    }
    
}
