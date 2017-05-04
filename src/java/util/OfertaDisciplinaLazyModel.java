package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.OfertaDisciplina;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public class OfertaDisciplinaLazyModel extends LazyDataModel<OfertaDisciplina> implements SelectableDataModel<OfertaDisciplina>{

    private final List<OfertaDisciplina> datasource;

    public OfertaDisciplinaLazyModel(List<OfertaDisciplina> datasource) {
        this.datasource = datasource;
    }

    @Override
    public OfertaDisciplina getRowData(String rowKey) {
        for (OfertaDisciplina oferta : datasource) {
            if (oferta.getID().equals(rowKey)) {
                return oferta;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(OfertaDisciplina oferta) {
        return oferta.getID();
    }

    @Override
    public List<OfertaDisciplina> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<OfertaDisciplina> data = new ArrayList<>();

        //filter
        for (OfertaDisciplina oferta : datasource) {
            boolean match = true;
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(oferta.getClass().getField(filterProperty).get(oferta));

                        if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                        match = false;
                    }
                }
            }
            if (match) {
                data.add(oferta);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(data, new OfertaDisciplinaLazySorter(sortField, sortOrder));
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
