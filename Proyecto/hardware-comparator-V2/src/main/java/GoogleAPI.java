import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;





public class GoogleAPI {
public static String ObtenerIdGoogle(String ean) throws UnirestException{
		
    	
    	String idGoogle = null;
    	String cadena1;
    	boolean encontradoID = false;
    	HttpResponse<String> response = Unirest.get("https://google-shopping.p.rapidapi.com/eanToGoogleShoppingId?country=ES&ean="+ean+"&language=ES")
    			.header("x-rapidapi-host", "google-shopping.p.rapidapi.com")
    			.header("x-rapidapi-key", "103a86961emsh663705ec211487ep16bccfjsn4a1c95988c80")
    			.asString();
    	cadena1 = response.getBody();
    	
    	List<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile("\"\\w+\"|\"\\w+(\\s\\w+)*\"");
		Matcher regexMatcher = regex.matcher(cadena1);
		while (regexMatcher.find()) {
		    matchList.add(regexMatcher.group());
		} 
		
		for(int i=0; i<matchList.size(); i++) {
			if(matchList.get(i).equals("\"google_shopping_id\"")) {
				idGoogle = matchList.get(i+1);
				encontradoID = true;
			}
				
		}
		
		if(encontradoID)
			idGoogle = idGoogle.replace("\"", "");
		return idGoogle;
	}
	 
	 public static ArrayList<String> obtenerGoogleShopping(String idProducto) throws UnirestException {
		 String idGoogle = idProducto;
		 ArrayList<String> ofertasAPI = new ArrayList();
		 //String cadenaOfertas = "{\"offers\": [{\"sold_by\": \"MacnificosSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 17.99, \"total_price\": 22.98, \"currency\": \"EUR\"}, {\"sold_by\": \"ManoMano.esSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 28.61, \"total_price\": 33.56, \"currency\": \"EUR\"}, {\"sold_by\": \"TDTprofesionalSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 24.66, \"total_price\": 29.61, \"currency\": \"EUR\"}, {\"sold_by\": \"MyTrendyPhone.esSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 32.3, \"total_price\": 34.7, \"currency\": \"EUR\"}, {\"sold_by\": \"eBaySe abrira en una ventana nueva\", \"details_and_special_offers\": [\"Envio gratuito\"], \"item_price\": 24.99, \"total_price\": 24.99, \"currency\": \"EUR\"}, {\"sold_by\": \"ielectro.esSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 16.95, \"total_price\": 21.27, \"currency\": \"EUR\"}, {\"sold_by\": \"MeQuedoUno.comSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 24.99, \"total_price\": 30.98, \"currency\": \"EUR\"}, {\"sold_by\": \"ER PreciosSe abrira en una ventana nueva\", \"details_and_special_offers\": [\"Envio gratuito\"], \"item_price\": 16.98, \"total_price\": 16.98, \"currency\": \"EUR\"}, {\"sold_by\": \" XtremmediaSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 17.7, \"total_price\": 22.6, \"currency\": \"EUR\"}, {\"sold_by\": \"Latiendadeinformatica.comSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 24.0, \"total_price\": 30.0, \"currency\": \"EUR\"}, {\"sold_by\": \"PreciosyaSe abrira en una ventana nueva\", \"details_and_special_offers\": [\"Envio gratuito\"], \"item_price\": 21.35, \"total_price\": 21.35, \"currency\": \"EUR\"}, {\"sold_by\": \"Phone HouseSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 22.99, \"total_price\": 26.99, \"currency\": \"EUR\"}, {\"sold_by\": \"TuCompraEnOfertaSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 17.14, \"total_price\": 22.84, \"currency\": \"EUR\"}, {\"sold_by\": \"GreaTecnoSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 26.6, \"total_price\": 31.55, \"currency\": \"EUR\"}, {\"sold_by\": \"Neotronics Europe, S.L.Se abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 27.9, \"total_price\": 32.85, \"currency\": \"EUR\"}, {\"sold_by\": \"Todoelectro.esSe abrira en una ventana nueva\", \"details_and_special_offers\": [], \"item_price\": 23.7, \"total_price\": 30.96, \"currency\": \"EUR\"}, {\"sold_by\": \"Grooves.Land EspanaSe abrira en una ventana nueva\", \"details_and_special_offers\": [\"Envio gratuito\"], \"item_price\": 22.99, \"total_price\": 22.99, \"currency\": \"EUR\"}], \"title\": \"TP-Link TL-WA850RE - Extensor de rango Wi-Fi\", \"image_url\": \"https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcRDJo4tIv06ZlCMcjyRUhDYWYGofxVVEYwauV6yhkRdomfOjQLCwoHwj0gtYFQm-Bns8mPSuIzETn3cZiTyBFqnpuexdp7XB6hQqMMk6vTNleh3qxD6ptQr&usqp=CAY\", \"google_shopping_id\": \"3443753458077214713\"}";
		 HttpResponse<String> response = Unirest.get("https://google-shopping.p.rapidapi.com/offers?country=ES&google_shopping_id="+idGoogle+"&language=ES")
					.header("x-rapidapi-host", "google-shopping.p.rapidapi.com")
					.header("x-rapidapi-key", "103a86961emsh663705ec211487ep16bccfjsn4a1c95988c80")
					.asString();
		 String cadenaOfertas = response.getBody();
		 
		 String[] partes = cadenaOfertas.split(",");
		 String[] partesDentro = null;
		 String sitioSinCadenas;
		 String nombreCorrecto;
		 String precio;
		 for(int i =0; i<partes.length;i++) {
			 partesDentro = partes[i].split(":");
			 for(int j =0; j<partesDentro.length;j++) {
				 if(partesDentro[j].contains("sold_by")) {
					 sitioSinCadenas = partesDentro[j+1].replace("\"", "");
					 nombreCorrecto = sitioSinCadenas.replace("Se abrira en una ventana nueva", "");
					 //print(nombreCorrecto);
					 ofertasAPI.add(nombreCorrecto);
					 //print(partesDentro[j+1]);
				 }
				 if(partesDentro[j].contains("total_price")) {
					 precio = partesDentro[j+1];
					 ofertasAPI.add(precio);
				 }
				 
			 }
		 }
		 
		 //print(cadenaOfertas);
		 return ofertasAPI;
		 
	 }
    
    public static void print(String string) {
		System.out.println(string);
	}
  
	public static void print(int string) {
		System.out.println(string);
	}
}
