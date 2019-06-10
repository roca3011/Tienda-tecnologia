package dominio.integracion;

import static org.junit.Assert.fail;

import java.util.Calendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Vendedor;
import dominio.Producto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	private static final String COMPUTADOR_LENOVO = "Computador Lenovo";
	
	private SistemaDePersistencia sistemaPersistencia;
	
	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	@Before
	public void setUp() {
		
		sistemaPersistencia = new SistemaDePersistencia();
		
		repositorioProducto = sistemaPersistencia.obtenerRepositorioProductos();
		repositorioGarantia = sistemaPersistencia.obtenerRepositorioGarantia();
		
		sistemaPersistencia.iniciar();
	}
	

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void generarGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		String nombreCliente = "Jhon Connor";

		// act
		vendedor.generarGarantia(producto.getCodigo(), nombreCliente);

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
		
	}

	@Test
	public void productoYaTieneGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		
		repositorioProducto.agregar(producto);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		String nombreCliente = "Jhon Connor";
		// act
		vendedor.generarGarantia(producto.getCodigo(), nombreCliente);
		try {
			
			vendedor.generarGarantia(producto.getCodigo(), nombreCliente);
			fail();
			
		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_TIENE_GARANTIA, e.getMessage());
		}
	}
	
	@Test
	public void consultarGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		String nombreCliente = "Jhon Connor";
		
		// act
		vendedor.generarGarantia(producto.getCodigo(), nombreCliente);		

		// assert
		Assert.assertEquals(nombreCliente, repositorioGarantia.obtener(producto.getCodigo()).getNombreCliente());
	}
	
	@Test
	public void productoConGarantiadeCienDiasTest() {

		// arrange
		String nombreCliente = "Jhon Connor";	
		double precio = 500000.0;
		int diasGarantia = 100;				
		  	
		Calendar calendaioFinGarantia = Calendar.getInstance();    	
		calendaioFinGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia);
		String fechaFinGarantia = calendaioFinGarantia.getTime().toString();		
		
		Producto producto = new ProductoTestDataBuilder().conPrecio(precio).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);	
		
		// act
		vendedor.generarGarantia(producto.getCodigo(), nombreCliente);			
		
		String fechaRepositorio = repositorioGarantia.obtener(producto.getCodigo()).getFechaFinGarantia().toString();
				
		// assert
		Assert.assertEquals(fechaFinGarantia, fechaRepositorio);
	}
	
	@Test
	public void productoConGarantiadedoscientosDiasTest() {

		// arrange
		String nombreCliente = "Jhon Connor";	
		double precio = 500001.0;
		int diasGarantia = 200;	
		int lunesExtra = 28;
		  	
		Calendar calendaioFinGarantia = Calendar.getInstance();    	
		calendaioFinGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia+lunesExtra);
		String fechaFinGarantia = calendaioFinGarantia.getTime().toString();		
		
		Producto producto = new ProductoTestDataBuilder().conPrecio(precio).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);	
		
		// act
		vendedor.generarGarantia(producto.getCodigo(), nombreCliente);			
		
		String fechaRepositorio = repositorioGarantia.obtener(producto.getCodigo()).getFechaFinGarantia().toString();
				
		// assert
		Assert.assertEquals(fechaFinGarantia, fechaRepositorio);
	}

}
