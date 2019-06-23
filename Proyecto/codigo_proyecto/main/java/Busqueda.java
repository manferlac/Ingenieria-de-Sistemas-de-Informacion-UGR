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

import myapphardware.Componente;
import myapphardware.OfertasComponentes;

@WebServlet(
    name = "Busqueda",
    urlPatterns = {"/busqueda"}
)
public class Busqueda extends HttpServlet {
	
	Document documento;
	Elements nombres, buscadorALTERNATE;
	Element nombre, imagen;
	String url, codigo, precioALTERNATE, precioCU, urlImagen;
	Map<String, String> codigosCU;
	ArrayList<String> codigosALTERNATE, codigosIGUALES;
	ArrayList<Componente> componentesIguales;
	OfertasComponentes ofertaALTERNATE, ofertaCU;;
	Componente componente;
	ArrayList<OfertasComponentes> ofertas;
	
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	
	String busqueda = request.getParameter("palabra");	//Obtenemos la palabra introducida en el formulario
	
	documento = Jsoup.connect("https://www.computeruniverse.net/en/search?cid=0&q=" + busqueda).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
	nombres = documento.select(".at__productListItemTitle");
	codigosCU = new HashMap<String, String>();
	
	if(nombres.size() > 6) {
		for(int i=0; i<6; i++) {
			nombre = nombres.get(i);
			url = nombre.attr("abs:href");
			documento = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
			codigo = documento.select(".value").get(2).text();
			precioCU = documento.select(".at__productpricevalue").first().text();
			codigosCU.put(codigo, precioCU);
		}
	}else {
		for(int i=0; i<nombres.size(); i++) {
			nombre = nombres.get(i);
			url = nombre.attr("abs:href");
			documento = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
			codigo = documento.select(".value").get(2).text();
			precioCU = documento.select(".at__productpricevalue").first().text();
			codigosCU.put(codigo, precioCU);
		}
	}
	
	documento = Jsoup.connect("https://www.alternate.es/html/search.html?query="+busqueda+"&x=0&y=0").userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
	nombres = documento.select(".productLink");
	componentesIguales = new ArrayList<Componente>();
	if(nombres.size() > 6) {
		for(int i=0; i<6; i++) {
			nombre = nombres.get(i);
			url = nombre.attr("abs:href");
			documento = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
			buscadorALTERNATE = documento.select(".c4");
			Iterator it = codigosCU.keySet().iterator();
			while(it.hasNext()) {
				String key = (String) it.next();
				for(int j=0; j<buscadorALTERNATE.size(); j++) {
					if(buscadorALTERNATE.get(j).text().equalsIgnoreCase(key)) {
						
						precioALTERNATE = documento.select("[itemprop=price]").first().text();
						componente = new Componente();
						componente.setId(key);
						componente.setNombre(nombres.get(i).text().substring(0, 92));
						imagen = documento.select("img").get(1);
						urlImagen = imagen.attr("abs:src");
						componente.setImagen(urlImagen);
						
						ofertaCU = new OfertasComponentes();
						ofertaCU.setSitio("COMPUTER UNIVERSE");
						ofertaCU.setPrecio(codigosCU.get(key));
						ofertaALTERNATE = new OfertasComponentes();
						ofertaALTERNATE.setSitio("ALTERNATE.ES");
						ofertaALTERNATE.setPrecio(precioALTERNATE);
						
						
						ofertas = new ArrayList<OfertasComponentes>();
						ofertas.add(ofertaCU);
						ofertas.add(ofertaALTERNATE);
						
						componente.setOfertas(ofertas);
						
						
						componentesIguales.add(componente);

					}
				}
			}
		}
	}else {
		
		for(int i=0; i<nombres.size(); i++) {
			nombre = nombres.get(i);
			url = nombre.attr("abs:href");
			documento = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36").timeout(100000).get();
			buscadorALTERNATE = documento.select(".c4");
			Iterator it = codigosCU.keySet().iterator();
			while(it.hasNext()) {
				String key = (String) it.next();
				for(int j=0; j<buscadorALTERNATE.size(); j++) {
					if(buscadorALTERNATE.get(j).text().equalsIgnoreCase(key)) {
						
						precioALTERNATE = documento.select("[itemprop=price]").first().text();
						componente = new Componente();
						componente.setId(key);
						componente.setNombre(nombres.get(i).text().substring(0, 92));
						imagen = documento.select("img").get(1);
						urlImagen = imagen.attr("abs:src");
						componente.setImagen(urlImagen);
						
						ofertaCU = new OfertasComponentes();
						ofertaCU.setSitio("COMPUTER UNIVERSE");
						ofertaCU.setPrecio(codigosCU.get(key));
						ofertaALTERNATE = new OfertasComponentes();
						ofertaALTERNATE.setSitio("ALTERNATE.ES");
						ofertaALTERNATE.setPrecio(precioALTERNATE);
						
						
						ofertas = new ArrayList<OfertasComponentes>();
						ofertas.add(ofertaCU);
						ofertas.add(ofertaALTERNATE);
						
						componente.setOfertas(ofertas);
						
						//componentesIguales = new ArrayList<Componente>();
						componentesIguales.add(componente);

					}
				}
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

