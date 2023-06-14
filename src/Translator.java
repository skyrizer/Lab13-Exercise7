
/*
 * @author WAFIR DZIHNI BIN ROZUKI
 * this class to translate text from client to target language
 */
public class Translator {

	public static String translate(String word,String targetLanguage) {
		
		if(targetLanguage.equals("Bahasa Melayu")) {
			
			return "Selamat Pagi";
		}
		else{
			
			return "The language is not available";
		}
		
	}
}
