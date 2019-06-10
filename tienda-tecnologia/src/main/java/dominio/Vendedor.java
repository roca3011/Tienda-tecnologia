package dominio;

import dominio.repositorio.RepositorioProducto;

import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
    public static final String NO_APLICA_GARANTIA = "Este producto no cuenta con garantía extendida";
    public static final String EXPRESION_REGULAR = "(?i)(a|e|i|o|u)";
    
    /**Constantes garantia*/
    public static final double PONCENTAJE_20 = 0.2;
    public static final double PONCENTAJE_10 = 0.1;
    public static final double PRECIO_LIMITE = 500000;
    public static final int	DIAS_GARANTIA_200 = 200;
    public static final int	DIAS_GARANTIA_100 = 100;
    public static final int	DIA_DE_LA_SEMANA = 2;
    public static final int	DIAS_DE_LA_SEMANA = 7;

    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;

    }

    public void generarGarantia(String codigo, String nombreCliente) {
    	
    	/** Se valida el codigo del producto*/
    	if (codigo.split(EXPRESION_REGULAR).length>3) {
			throw new GarantiaExtendidaException(NO_APLICA_GARANTIA);
		}    	
    	
    	/** Se valida si el producto tiene garantia */
    	if (tieneGarantia(codigo)) {
    		throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
		};
    	
    	
		GarantiaExtendida garantiaExtendida = mappGarantiaExtendida(codigo, nombreCliente);
		repositorioGarantia.agregar(garantiaExtendida);
    }

    public boolean tieneGarantia(String codigo) {    	
    	
    	/** Obtiene la garantia*/
    	Producto producto = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
    	
    	return (producto != null ? true : false);        
    }
    
    /** Crea el objeto GarantiaExtendida*/
    private GarantiaExtendida mappGarantiaExtendida(String codigo, String nombreCliente){
    	
    	/** Se consulta el producto*/
    	Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
    	
    	double precioGarantia = producto.getPrecio(); 
    	
    	/**Se obtiene la fecha actual*/
    	Date hoy=new Date();
    	Calendar calfechaActual= Calendar.getInstance();
    	calfechaActual.setTime(hoy);
    	Date fechaSolicitudGarantia = calfechaActual.getTime();
    	
    	/**Se crea el objeto calendar para la fecha fin de garantia*/
    	Calendar CalendaFinGarantia;
    	Date fechaFinGarantia;
    	
    	/**Se valida el precio del producto*/
    	if (precioGarantia>PRECIO_LIMITE) {
    		precioGarantia = precioGarantia * PONCENTAJE_20;
    		CalendaFinGarantia = calcularFechaGarantia(DIAS_GARANTIA_200);    		
    		
    		/** Se valida si el dia final de la garantia es un domingo */
    		int day = CalendaFinGarantia.get(Calendar.DAY_OF_WEEK);    		
    		if (day==1) {
    			calcularFechaGarantia(1);
			}
    		
		}else{
			precioGarantia = precioGarantia * PONCENTAJE_10;
			CalendaFinGarantia = calcularFechaGarantia(DIAS_GARANTIA_100);
		}    
    	
    	fechaFinGarantia = CalendaFinGarantia.getTime();    	
    	
    	GarantiaExtendida garantiaExtendida = new GarantiaExtendida(producto, fechaSolicitudGarantia,
    			fechaFinGarantia, precioGarantia, nombreCliente);  
    	
    	return garantiaExtendida;
    }
    
    /** Se obtiene la fecha fin de la garantia*/
    public Calendar calcularFechaGarantia(int diasGarantia){    	
    	
    	Date hoy=new Date();
    	Calendar calfechaActual= Calendar.getInstance();
    	calfechaActual.setTime(hoy);
    	Calendar fechaFinGarantia;
    	int diasExtra = 0;
    	
    	if (diasGarantia == DIAS_GARANTIA_200) {
    		diasExtra = calcularDiasExtra(diasGarantia, calfechaActual.get(Calendar.DAY_OF_WEEK));
    		diasGarantia = diasGarantia+ diasExtra;
		}    	
    	fechaFinGarantia = (Calendar) calfechaActual.clone();
    	fechaFinGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia);    	
    	
    	return fechaFinGarantia;
		
    }
    
    /**Se obtiene los dìas extra segùn ls siguientes condiciones:
     * 1- Si dìa actual es lunes +1 
     * 2- Si los dìas restantes de la semana son mayor o igual
     * 	  al los dìas que actualmente faltan para ser lunes +1*/
    public int calcularDiasExtra(int diasGarantia, int diaActual){
    	
    	int Semanas=diasGarantia/DIAS_DE_LA_SEMANA;
        int diasFaltantes=diasGarantia%DIAS_DE_LA_SEMANA;
        int diasExtra = 0;        
        int diasFaltantesSemana = DIAS_DE_LA_SEMANA - diaActual;
        int diasFaltantesLunes = diasFaltantesSemana+DIA_DE_LA_SEMANA;
        
        /**Si el dìa actual es lunes se suma 1 dia a la garantìa*/
    	if (diaActual == DIA_DE_LA_SEMANA) {
    		diasExtra = diasExtra +1;
		}    	
    	
    	/**Si los dias restantes de las semanas de los 200 dìas son
    	 * mayor o igual a los dìas que actualmente faltan para ser lunes
    	 * se suma 1 dìa mas*/
    	if (diasFaltantes >= diasFaltantesLunes) {
    		diasExtra = diasExtra +1;
		}    	
    	
    	return diasExtra + Semanas;
    }

}
