import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mashape.unirest.http.exceptions.UnirestException;

import myapphardware.Componente;
import myapphardware.OfertasComponentes;

/**
 * Servlet implementation class CajasMSI
 */
@WebServlet(
		name = "MonitoresMSI",
		urlPatterns = "/MonitoresMSI"
		)

public class MonitoresMSI extends HttpServlet {
	
	Document documento;
	Elements nombresComponentes, elementosComponenteAlternate;
	Element nombre, imagen, codigoAlter;
	String url, codigo, nombreComponente,precioALTERNATE, precioPcComponentes, precioAmazon,precioComponente, urlImagen;
	Map<String, String> componentesAlternate;
	ArrayList<String> codigosALTERNATE, codigosIGUALES, ofertasAPI;
	ArrayList<Componente> componentesIguales;
	OfertasComponentes ofertaALTERNATE, ofertaPcComponentes, ofertaAmazon, ofertaTiendasAPI;
	Componente componente;
	ArrayList<OfertasComponentes> ofertas;
	boolean encontradoAPI;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		documento = Jsoup.connect("https://www.alternate.es/html/search.html?query=MSI&filterManufacturer=MSI&filter_-1=1500&filter_-1=162900&filter_416=812").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
		nombresComponentes = documento.select(".productLink");
		boolean encontrado;
		componentesAlternate = new HashMap<String, String>();
		int tamanio = nombresComponentes.size();
		if(tamanio >6)
			tamanio=6;
		for(int i=0;i<tamanio; i++) {
			nombre = nombresComponentes.get(i);
			url = nombre.attr("abs:href");
			documento = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
			elementosComponenteAlternate = documento.select(".c1");
			encontrado = false;
			int contador = 0;
			do {
				if(elementosComponenteAlternate.get(contador).text().equals("EAN"))
					encontrado = true;
				
				contador++;
			}while(!encontrado);
			codigoAlter = documento.select(".c4").get(contador-1);
			precioALTERNATE = documento.select("[itemprop=price]").first().text();
			componentesAlternate.put(codigoAlter.text(),precioALTERNATE);	
		}
		
componentesIguales = new ArrayList<Componente>();
		
		//AMAZON
		for (Map.Entry<String, String> entry : componentesAlternate.entrySet()) {
			
			documento = Jsoup.connect("https://www.amazon.es/s?k="+entry.getKey()+"&__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&ref=nb_sb_noss_2").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();		    	
			
			//API
			String idGoogle = null;
			try {
				idGoogle = GoogleAPI.ObtenerIdGoogle(entry.getKey());
				if(idGoogle!=null) {
					encontradoAPI = true;
				}else {
					encontradoAPI = false;
				}
			} catch (UnirestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ofertasAPI = new ArrayList<String>();
			try {
				if(encontradoAPI) {
					ofertasAPI = GoogleAPI.obtenerGoogleShopping(idGoogle);
				}
				
			} catch (UnirestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//FIN API
			
			if(documento.select(".s-image").first() != null) {
				nombre = documento.select(".s-image").first();
				nombreComponente = nombre.attr("alt");
				componente = new Componente();
				componente.setId(entry.getKey());
				componente.setNombre(nombreComponente);
				urlImagen = nombre.attr("src");
				componente.setImagen(urlImagen);
				
				if(documento.select(".a-price-whole").first() != null){
					nombre = documento.select(".a-price-whole").first();
					ofertaAmazon = new OfertasComponentes();
					ofertaAmazon.setSitio("Amazon");
					ofertaAmazon.setPrecio(nombre.text());
					
					ofertaALTERNATE = new OfertasComponentes();
					ofertaALTERNATE.setSitio("ALTERNATE.ES");
					ofertaALTERNATE.setPrecio(entry.getValue());
					
					ofertas = new ArrayList<OfertasComponentes>();
					ofertas.add(ofertaAmazon);
					ofertas.add(ofertaALTERNATE);
					
					if(encontradoAPI) {
						for(int i=0;i<ofertasAPI.size();i+=2) {
							ofertaTiendasAPI = new OfertasComponentes();
							ofertaTiendasAPI.setSitio(ofertasAPI.get(i));
							ofertaTiendasAPI.setPrecio(ofertasAPI.get(i+1)+" €");
							ofertas.add(ofertaTiendasAPI);
							//print(ofertasAPI.get(i) + " | " + ofertasAPI.get(i+1));
						}
					}
					
					componente.setOfertas(ofertas);
					componentesIguales.add(componente);
				}
							
			}
			
		}
		
		request.setAttribute("elementos", componentesIguales);
		RequestDispatcher despachador = request.getRequestDispatcher("/MostrarDatos.jsp");
		try {
			despachador.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void print(String string) {
		System.out.println(string);
	}
  
	public static void print(int string) {
		System.out.println(string);
	}

}