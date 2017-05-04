/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.lang.reflect.Field;
import java.util.Comparator;
import model.Disciplina;
import org.primefaces.model.SortOrder;

/**
 *
 * @author charles
 */
public class DisciplinaLazySorter implements Comparator<Disciplina> {

    private String sortField;

    private SortOrder sortOrder;

    /**
     * initializing sorting field and sorting order
     * @param sortField
     * @param sortOrder
     */
    public DisciplinaLazySorter(String sortField, SortOrder sortOrder) {
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
    public int compare(Disciplina disciplina1, Disciplina disciplina2) {
        try {
            Field field1 = disciplina1.getClass().getDeclaredField(this.sortField);
            Field field2 = disciplina2.getClass().getDeclaredField(this.sortField);
            field1.setAccessible(true);
            field2.setAccessible(true);
            Object value1 = field1.get(disciplina1);
            Object value2 = field2.get(disciplina2);

            int value = ((Comparable)value1).compareTo(value2);
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}