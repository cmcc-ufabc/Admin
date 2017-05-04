package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.Turma;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author erick
 */
public class TurmaLazyModel extends LazyDataModel<Turma> implements SelectableDataModel<Turma> {
    private List<Turma> datasource;

    public TurmaLazyModel(List<Turma> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Turma getRowData(String rowKey) {
        for (Turma t : datasource) {
            if (t.getID().equals(rowKey)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Turma t) {
        return t.getID();
    }

    @Override
    public List<Turma> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Turma> data = new ArrayList<Turma>();
        //filter
        for (Turma t : datasource) {
            boolean match = true;
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(t.getClass().getField(filterProperty).get(t));

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
                data.add(t);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(data, new TurmaLazySorter(sortField, sortOrder));
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
