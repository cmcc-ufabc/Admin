/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.Disciplina;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author charles
 */
public class DisciplinaLazyModel extends LazyDataModel<Disciplina> implements SelectableDataModel<Disciplina>{

    private List<Disciplina> datasource;

    public DisciplinaLazyModel(List<Disciplina> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Disciplina getRowData(String rowKey) {
        for (Disciplina disciplina : datasource) {
            if (disciplina.getID().equals(rowKey)) {
                return disciplina;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Disciplina disciplina) {
        return disciplina.getID();
    }

    @Override
    public List<Disciplina> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Disciplina> data = new ArrayList<Disciplina>();

        //filter
        for (Disciplina disciplina : datasource) {
            boolean match = true;
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(disciplina.getClass().getField(filterProperty).get(disciplina));

                        if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (Exception e) {
                        match = false;
                    }
                }
            }
            if (match) {
                data.add(disciplina);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(data, new DisciplinaLazySorter(sortField, sortOrder));
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }
}
