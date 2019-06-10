package dominio;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prueba_expre {
	
	private static final String expre = "(?i)[aeiou]{3,}";

	public static void main(String[] args) {
		
		String cadena = "Sa01eH1AT51";
		prueba(cadena);
		
		String[] dias={"Domingo","Lunes","Martes", "Miércoles","Jueves","Viernes","Sábado"};
        Date hoy=new Date();
      int numeroDia=0;
      Calendar cal= Calendar.getInstance();
      cal.setTime(hoy);
      numeroDia=cal.get(Calendar.DAY_OF_WEEK);
      System.out.println("hoy es "+ dias[numeroDia - 1]+"numeroDia: "+numeroDia);
      
      	//Date fechaSolicitudGarantia = Calendar.getInstance().getTime();
      	/*Calendar cldr;
  		cldr = (Calendar) cal.clone();
  		System.out.println(cldr.getTime());
  		cldr.add(Calendar.DAY_OF_YEAR, + 200);
  		System.out.println(cldr.getTime());
  		int numeroDia2=cldr.get(cldr.DAY_OF_WEEK);
  		System.out.println("cldr: "+cldr+"numeroDia: "+numeroDia2);*/
        
        Calendar fecha = calcularFechaGarantia(200);
        
        int day = fecha.get(Calendar.DAY_OF_WEEK);
        System.out.println("numeroDia: "+dias[day - 1]+" "+day);       
        
        System.out.println("dia final es "+ fecha.getTime());
        
        
		
		/*Calendar now = Calendar.getInstance();
		System.out.println(now.get(Calendar.DAY_OF_WEEK));*/
		
	}
	
	
	private static void prueba(String cadena) {
	
		Pattern pat = Pattern.compile(expre);
	    Matcher mat = pat.matcher(cadena);
	    if (mat.matches()) {
	        System.out.println("Válido");
	    } else {
	        System.out.println("No Válido");
	    }
	}
	
	/** Se obtiene la fecha fin de la garantia*/
    public static Calendar calcularFechaGarantia(int diasGarantia){    	
    	
    	Date hoy=new Date();
    	Calendar cal= Calendar.getInstance();
    	cal.setTime(hoy);
    	Calendar fechaFinGarantia;
    	int diasExtra = 0;
    	
    	if (diasGarantia == 200) {
    		System.out.println("diasGarantia = 200");
    		diasExtra = calcularDiasExtra(diasGarantia, cal.get(Calendar.DAY_OF_WEEK));
    		diasGarantia = diasGarantia+ diasExtra;
    		
    		System.out.println(diasGarantia);
		}    	
    	
    	fechaFinGarantia = (Calendar) cal.clone();
    	fechaFinGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia);    	
    	
    	return fechaFinGarantia;
		
    }
    
    private static int calcularDiasExtra(int diasGarantia, int diaActual){
    	
    	int cociente=diasGarantia/7;
        int residuo=diasGarantia%7;
        int diasExtra = 0;
        
        int aux2 = 7 - diaActual;
        aux2 = aux2+2;
        
        System.out.println("aux2: "+aux2);
    	
    	if (diaActual == 2) {
    		diasExtra = diasGarantia +1;
    		System.out.println("diasExtra 1: "+diasExtra);
		}    	
    	
    	if (residuo >= aux2) {
    		diasExtra = diasGarantia +1;
    		System.out.println("diasExtra 2: "+diasExtra);
		}
    	
    	return diasExtra + cociente;
    }
	
	
	
/*public static Calendar calcularFechaGarantia(int diasGarantia, int numeroDia){    	
    	
    	Date hoy=new Date();
    	Calendar cal= Calendar.getInstance();
    	cal.setTime(hoy);
    	
    	int cociente=diasGarantia/7;
        int residuo=diasGarantia%7;
        
        System.out.println("cociente: "+cociente);
        
        System.out.println("numeroDia: "+ numeroDia);
        
        int aux2 = 7 - numeroDia;
        aux2 = aux2+2;
        
        System.out.println("aux2: "+aux2);
    	
    	if (cal.get(Calendar.DAY_OF_WEEK) == 2) {
    		diasGarantia = diasGarantia +1;
    		System.out.println("diasGarantia 1: "+diasGarantia);
		}    	
    	
    	if (residuo >= aux2) {
    		diasGarantia = diasGarantia +1;
    		System.out.println("diasGarantia 2: "+diasGarantia);
		}
    	
    	Calendar fechaSolicitudGarantia;
    	fechaSolicitudGarantia = (Calendar) cal.clone();
    	fechaSolicitudGarantia.add(Calendar.DAY_OF_YEAR, + diasGarantia);
    	
    	System.out.println("fechaSolicitudGarantia: "+fechaSolicitudGarantia.getTime());
    	
    	return fechaSolicitudGarantia;
		
    } */
	
	
	

}
