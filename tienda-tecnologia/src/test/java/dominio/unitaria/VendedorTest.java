package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import dominio.Vendedor;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.Producto;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	@Test
	public void productoYaTieneGarantiaTest() {
		
		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoTestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertTrue(existeProducto);
	}
	
	@Test
	public void productoNoTieneGarantiaTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean existeProducto =  vendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertFalse(existeProducto);
	}
	
	
	@Test(expected = GarantiaExtendidaException.class)
	public void ExceptionProductoNoAplicaGarantiaTest(){
	
		//arrange
		String codigo = "A01ESA0150";
		String nombreCliente = "Javier Rincon";			
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
				
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		vendedor.generarGarantia(codigo, nombreCliente);				
		
	}
	
	@Test(expected = GarantiaExtendidaException.class)
	public void ExceptionProductoTieneGarantiaTest(){
	
		//arrange
		String nombreCliente = "Javier Rincon";			
		
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
				
		Producto producto = productoestDataBuilder.build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
				
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		// act 		
		vendedor.generarGarantia(producto.getCodigo(), nombreCliente);		
	}
	

	@Test
	public void calcularConGarantiadeCienDiasTest(){
		
		int diasGarantia = 100;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");	
		
		Calendar calendaioFinGarantia = Calendar.getInstance();   		
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
				
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);		
		
		calendaioFinGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia);
		String fechaFinGarantia = format1.format(calendaioFinGarantia.getTime());//calendaioFinGarantia.getTime().toString();
		
		Calendar calcularfechaFinGarantia = vendedor.calcularFechaGarantia(diasGarantia);
		
		String calfechaFinGarantia = format1.format(calcularfechaFinGarantia.getTime());
		
		Assert.assertEquals(fechaFinGarantia, calfechaFinGarantia);
				
	}
	
	@Test
	public void calcularConGarantiadeDoscientosDiasTest() throws ParseException{
		
		int diasGarantia = 200;
		int diasExtra = 0;
				
		Calendar calendaioFinGarantia = Calendar.getInstance();
		
		System.out.println("calendaioFinGarantia 2: "+calendaioFinGarantia.getTime());
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");				
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
				
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		diasExtra = vendedor.calcularDiasExtra(diasGarantia, calendaioFinGarantia.get(Calendar.DAY_OF_WEEK));
		
		calendaioFinGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia + diasExtra);
		
		String fechaFinGarantia = format1.format(calendaioFinGarantia.getTime());
		System.out.println("fechaFinGarantia: "+fechaFinGarantia);		
		
		Calendar calcularfechaFinGarantia = vendedor.calcularFechaGarantia(diasGarantia);
		
		System.out.println("calcularfechaFinGarantia: "+calcularfechaFinGarantia.getTime());
		
		String calfechaFinGarantia = format1.format(calcularfechaFinGarantia.getTime());//calcularfechaFinGarantia.getTime().toString();
				
		Assert.assertEquals(fechaFinGarantia, calfechaFinGarantia);
				
	}

	
}
