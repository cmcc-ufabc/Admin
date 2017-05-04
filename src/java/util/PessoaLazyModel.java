package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.Pessoa;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public class PessoaLazyModel extends LazyDataModel<Pessoa> implements SelectableDataModel<Pessoa>{

    private List<Pessoa> datasource;

    public PessoaLazyModel(List<Pessoa> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Pessoa getRowData(String rowKey) {
        for (Pessoa p : datasource) {
            if (p.getID().equals(rowKey)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Pessoa p) {
        return p.getID();
    }

    @Override
    public List<Pessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Pessoa> data = new ArrayList<Pessoa>();

        //filter
        for (Pessoa p : datasource) {
            boolean match = true;
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(p.getClass().getField(filterProperty).get(p));

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
                data.add(p);
            }
        }

        //sort
        if (sortField != null) {
            Collections.sort(data, new PessoaLazySorter(sortField, sortOrder));
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
