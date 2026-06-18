package it.almaviva.mic.etl.divi.parsers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvMapper 
{
	/* metodo di associazione degli elementi di uno String[] */
    public static <T> T associaCampi(String[] values, Class<T> clazz) {
        try 
        {
        	/* creazione di un'istanza della classe richiesta */
            T instance = clazz.getDeclaredConstructor().newInstance();

            /* iterazione sui campi dato della classe (compresi quelli ereditati)*/
            for (Field field : getAllFields(clazz)) {
            	/* estrazione dell'annotazione associata al campo */
                CsvPosition annotation = field.getAnnotation(CsvPosition.class);

                /* se il field non e' annotato, si prosegue al successivo */
                if (annotation == null) 
                	continue;

                /* estrazione del valore associato all'annotazione */
                int index = annotation.value();

                /* se l'indice supera il massimo consentito nell'array di stringhe, 
                 * si passa al field successivo */
                if (index >= values.length) 
                	continue;

                /* valore estratto dall'array */
                String rawValue = values[index];
//                if(rawValue != null)
//                	rawValue = rawValue.trim();

                /* conversione della stringa nel valore/tipo richiesto dal field 
                 * della classe target */
                Object convertedValue = convert(rawValue, field.getType());

                /* riempimento del field */
                field.setAccessible(true);
                field.set(instance, convertedValue);
            }

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Errore durante il mapping CSV -> DTO", e);
        }
    }

    /*
     * Recupera tutti i campi della classe, inclusi quelli ereditati
     */
    private static List<Field> getAllFields(Class<?> clazz) 
    {
        List<Field> fields = new ArrayList<>();

        while (clazz != null && clazz != Object.class) 
        {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    /*
     * Conversione base String -> tipo campo
     */
    private static Object convert(String value, Class<?> targetType) {

        if (value == null || value.isEmpty()) 
        	return null;

        if (targetType == String.class) 
        	return value;

        if (targetType == Integer.class || targetType == int.class)
            return Integer.parseInt(value);

        if (targetType == Long.class || targetType == long.class)
            return Long.parseLong(value);

        if (targetType == Double.class || targetType == double.class)
            return Double.parseDouble(value);

        if (targetType == Boolean.class || targetType == boolean.class)
            return Boolean.parseBoolean(value);

        /* restituzione della stringa come comportamento di default */
        return value;
    }
}