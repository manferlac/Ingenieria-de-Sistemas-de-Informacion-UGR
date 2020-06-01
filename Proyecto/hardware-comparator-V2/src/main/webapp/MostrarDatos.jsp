<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<meta charset="UTF-8">
<title>Muestra de datos</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="index.html">HCC</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Cajas PC
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="/CajasMSI">MSI</a>
          <a class="dropdown-item" href="/CajasCorsair">CORSAIR</a>
        </div>
      </li>
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Monitores
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="/MonitoresMSI">MSI</a>
          <a class="dropdown-item" href="/MonitoresSamsung">SAMSUNG</a>
        </div>
      </li>
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Impresoras
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="/ImpresorasOKI">OKI</a>
        </div>
      </li>
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Auriculares
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="/AuricularesSony">Sony</a>
        </div>
      </li>
    </ul>
    <form onsubmit="return verificarEAN()" class="form-inline my-2 my-lg-0" action="/busqueda" name="formularioBusqueda" id="formularioBusqueda">
      <input class="form-control mr-sm-2" type="search" placeholder="EAN-13 o producto" name="palabra" id="palabra" aria-label="Search" required>
      <input class="btn btn-outline-success my-2 my-sm-0" type="submit" value="Buscar">
    </form>
  </div>
</nav>
	<!-- Page Content -->
<div class="container">
	
	<c:choose>
	    <c:when test="${elementos.size() > '0'}">
	        <!-- Page Heading -->
  <h1 class="my-4">Resultado de la búsqueda
  </h1>
  
  <h4>Artículos encontrados: ${elementos.size() }</h4>

  <div class="row">
  	<c:forEach items="${elementos}" var="elem">
    <div class="col-lg-4 col-md-4 col-sm-6 mb-4">
      <div class="card h-100" style="text-align: center;">
        <a href="#"><img style="max-width: 75%;" class="card-img-top" src="${elem.getImagen() }" alt=""></a>
        <div class="card-body">
        	<span style="font-size: small">Ref: ${elem.getId() }</span>
          <h4 class="card-title" style="font-size: small">
            <a href="#">${elem.getNombre() }</a>
          </h4>
           
          <c:forEach items="${elem.getOfertas() }" var="offer" >
          <span style="font-size: small">${offer.getSitio() }: ${offer.getPrecio() }</span><br>
          </c:forEach>
          
        </div>
      </div>
    </div>
    </c:forEach>
   
  </div>
  <!-- /.row -->
	        <br />
	    </c:when>    
	    <c:otherwise>
	        <h1 class="my-4">No se han encontrado resultados
  			</h1> 
	    </c:otherwise>
	</c:choose>
  

</div>
<!-- /.container -->
	<script type="text/javascript">
		function numDigits(x) {
		  return Math.max(Math.floor(Math.log10(Math.abs(x))), 0) + 1;
		}
	
		function verificarEAN(){
			var ean = document.getElementById('palabra');
			var numero = parseInt(ean.value);
			if(!isNaN(numero)){
				var tamanoNumero = numDigits(numero);
				if(tamanoNumero == 12){
					var primerNumero = parseInt(numero.substr(0,1));
					if(primerNumero == 0){
						tamanoNumero++;
					}
				}
				if(tamanoNumero != 13){
					alert('El código EAN debe tener 13 dígitos');
					return false;
				}
			}
			
			return true;
		}
	</script>
		
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>	
</body>
</html>