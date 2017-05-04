package util;

import java.lang.reflect.Field;
import java.util.Comparator;

import model.Pessoa;
import org.primefaces.model.SortOrder;

/**
 *
 * @author charles
 */
public class PessoaLazySorter implements Comparator<Pessoa> {

    private String sortField;

    private SortOrder sortOrder;

    /**
     * initializing sorting field and sorting order
     * @param sortField
     * @param sortOrder
     */
    public PessoaLazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    /**
     * Comparing object1 and object2 with reflection
     * @param object1
     * @param object2
     * @return
     */
    @Override
    public int compare(Pessoa p1, Pessoa p2) {
        try {
            Field field1 = p1.getClass().getDeclaredField(this.sortField);
            Field field2 = p2.getClass().getDeclaredField(this.sortField);
            field1.setAccessible(true);
            field2.setAccessible(true);
            Object value1 = field1.get(p1);
            Object value2 = field2.get(p2);

            int value = ((Comparable)value1).compareTo(value2);
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}