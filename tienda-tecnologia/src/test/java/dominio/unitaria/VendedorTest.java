package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

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
		Calendar calendaioFinGarantia = Calendar.getInstance();    	
		calendaioFinGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia);
		String fechaFinGarantia = calendaioFinGarantia.getTime().toString();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
				
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar calcularfechaFinGarantia = vendedor.calcularFechaGarantia(diasGarantia);
		
		String calfechaFinGarantia = calcularfechaFinGarantia.getTime().toString();
		
		Assert.assertEquals(fechaFinGarantia, calfechaFinGarantia);
				
	}
	
	@Test
	public void calcularConGarantiadeDoscientosDiasTest(){
		
		int diasGarantia = 200;
		int lunesExtra = 28;
		
		Calendar calendaioFinGarantia = Calendar.getInstance();    	
		calendaioFinGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia+lunesExtra);
		String fechaFinGarantia = calendaioFinGarantia.getTime().toString();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
				
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar calcularfechaFinGarantia = vendedor.calcularFechaGarantia(diasGarantia);
		
		String calfechaFinGarantia = calcularfechaFinGarantia.getTime().toString();
		
		Assert.assertEquals(fechaFinGarantia, calfechaFinGarantia);
				
	}

	
}
