package util;

import java.lang.reflect.Field;
import java.util.Comparator;
import model.Turma;
import org.primefaces.model.SortOrder;

/**
 *
 * @author erick
 */
public class TurmaLazySorter implements Comparator<Turma> {
    private String sortField;

    private SortOrder sortOrder;

    /**
     * initializing sorting field and sorting order
     * @param sortField
     * @param sortOrder
     */
    public TurmaLazySorter(String sortField, SortOrder sortOrder) {
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
    public int compare(Turma t1, Turma t2) {
        try {
            Field field1 = t1.getClass().getDeclaredField(this.sortField);
            Field field2 = t2.getClass().getDeclaredField(this.sortField);
            field1.setAccessible(true);
            field2.setAccessible(true);
            Object value1 = field1.get(t1);
            Object value2 = field2.get(t2);

            int value = ((Comparable)value1).compareTo(value2);
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
