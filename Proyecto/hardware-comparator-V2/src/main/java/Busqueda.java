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

@WebServlet(
	    name = "Busqueda",
	    urlPatterns = {"/busqueda"}
	)
	public class Busqueda extends HttpServlet {
		
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
		boolean encontrado, apis, encontradoAPI;
		
	  @Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		String busqueda = request.getParameter("palabra");	//Obtenemos la palabra introducida en el formulario
		
		//Conectamos con la página ALTERNATE con el parámetro de búsqueda
		documento = Jsoup.connect("https://www.alternate.es/html/search.html?query="+busqueda+"&x=0&y=0").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
		componentesAlternate = new HashMap<String, String>();
		if(isNumeric(busqueda) == true) {	//Si lo que obtenemos es número entra if
			//print(busqueda.length());
			//if(busqueda.length()==13) { //Controlamos el EAN-13 en el servidor
				apis = true;					//Búsqueda en API true
				if(documento.select(".c1").first() != null) {	//Si existe el elemento en ALTERNATE
					precioALTERNATE = documento.select("[itemprop=price]").first().text();	//Guardamos el precio
					componentesAlternate.put(busqueda,precioALTERNATE);						//Guardamos en el HashMap el ean del producto y el precio
					
					String idGoogle = null;
					try {
						idGoogle = GoogleAPI.ObtenerIdGoogle(busqueda);	//Obtenemos el id de google del producto a través del EAN
						if(idGoogle!=null) {							//Si el idGoogle no es nulo
							encontradoAPI = true;						//Ha sido encontrado por la API
						}else {
							encontradoAPI = false;
						}
					} catch (UnirestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ofertasAPI = new ArrayList<String>();
					try {
						if(encontradoAPI) {				//Si ha sido encontrado por la API
							ofertasAPI = GoogleAPI.obtenerGoogleShopping(idGoogle);	//Guardamos en un ARRAY todas las ofertas encontradas por la API
						}
					} catch (UnirestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			//}
			
			}
		}else {						//Si lo que buscamos no es el EAN
			apis = false;			//No buscamos APIS ya que el consumo de cuota podría ser excesivo
			nombresComponentes = documento.select(".productLink");	//Obtenemos los productos de ALTERNATE
			//componentesAlternate = new HashMap<String, String>();
			int tamanio = nombresComponentes.size();
			if(tamanio>6)	
				tamanio=6;		//En el caso de que haya más de 6 productos, solo vamos a recorrer como máximo 6 ya que podría excederse el tiempo de ejecucción
			for(int i=0;i<tamanio; i++) {
				nombre = nombresComponentes.get(i);	//Cogemos un producto
				url = nombre.attr("abs:href");		//Obtenemos la URL de ese producto
				//Entramos dentro de la ficha del producto mediante la url
				documento = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
				elementosComponenteAlternate = documento.select(".c1"); //Vamos a coger todos los elementos que tengan de clase "c1" para encontrar el EAN
				/*
				 * Con el código siguiente recorremos cada uno de los elementos con clase "c1" hasta encontrar uno que su contenido sea igual a "EAN"
				 * Cuando lo encontramos ya tenemos guardado en la variable contador la posición donde se encuentra
				 */
				encontrado = false;
				int contador = 0;
				do {
					if(elementosComponenteAlternate.get(contador).text().equals("EAN"))
						encontrado = true;
					
					contador++;
				}while(!encontrado);
				codigoAlter = documento.select(".c4").get(contador-1); //Como tenemos la posición donde se encuentra el EAN, guardamos el elemento de la clase "c4" de la posición contador-1 que es donde se encuentra el EAN del producto
				precioALTERNATE = documento.select("[itemprop=price]").first().text(); //Guardamos el precio del producto
				componentesAlternate.put(codigoAlter.text(),precioALTERNATE);	//Guardamos en el HashMap el ean del producto y el precio
			}
		}
		
		componentesIguales = new ArrayList<Componente>();
		
		//AMAZON
		for (Map.Entry<String, String> entry : componentesAlternate.entrySet()) { //Recorremos todo el HashMap que tenemos de los componentes de ALTERNATE
			
			//Conectamos con la pagina Amazon introduciendo el código EAN del producto que se encuentra en la KEY de cada elemento del HashMAp
			documento = Jsoup.connect("https://www.amazon.es/s?k="+entry.getKey()+"&__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&ref=nb_sb_noss_2").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();		    	
			if(documento.select(".s-image").first() != null) { //Si existe el producto
				nombre = documento.select(".s-image").first();	//Obtenemos el producto
				nombreComponente = nombre.attr("alt");			//Obtenemos el nombre del producto
				componente = new Componente();					//Creamos un objeto de la clase Componente
				componente.setId(entry.getKey());				//Insertamos el EAN
				componente.setNombre(nombreComponente);			//Insertamos el nombre
				urlImagen = nombre.attr("src");					//Obtenemos la url de la imagen del producto
				componente.setImagen(urlImagen);				//Insertamos url de imagen
				
				if(documento.select(".a-price-whole").first() != null){	//Si existe el precio del producto, hay veces que en amazon no sale el precio con la primera observación o por estar fuera de compra
					nombre = documento.select(".a-price-whole").first();	//Obtenemos el producto
					
					ofertaAmazon = new OfertasComponentes();	//Creamos ofertaAmazon
					ofertaAmazon.setSitio("Amazon");			//Insertamos el sitio de la oferta
					ofertaAmazon.setPrecio(nombre.text()+" €");	//Insertamos el Precio de la oferta
					
					ofertaALTERNATE = new OfertasComponentes();	//Creamos oferta ALTERNATE
					ofertaALTERNATE.setSitio("ALTERNATE.ES");	//Insertamos oferta ALTERNATE
					ofertaALTERNATE.setPrecio(entry.getValue().replace(",-*", ""));	//Insertamos el Precio de la oferta
					
					ofertas = new ArrayList<OfertasComponentes>();
					ofertas.add(ofertaAmazon);
					ofertas.add(ofertaALTERNATE);
					
					if(apis && encontradoAPI) {  //Si se ha usado la API
						for(int i=0;i<ofertasAPI.size();i+=2) {
							ofertaTiendasAPI = new OfertasComponentes();
							ofertaTiendasAPI.setSitio(ofertasAPI.get(i));
							ofertaTiendasAPI.setPrecio(ofertasAPI.get(i+1)+" €");
							ofertas.add(ofertaTiendasAPI);
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
	  
	  public static boolean isNumeric(String str) {
		  return (str.matches("[+-]?\\d*(\\.\\d+)?") && str.equals("")==false);
	  }
	  
	}