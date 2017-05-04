package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.OfertaDisciplina;
import org.primefaces.model.SelectableDataModel;

public class OfertaDisciplinaDataModel extends ListDataModel implements SelectableDataModel<OfertaDisciplina> {

    public OfertaDisciplinaDataModel() {
    }

    public OfertaDisciplinaDataModel(List<OfertaDisciplina> data) {
        super(data);
    }

    @Override
    public OfertaDisciplina getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
        List<OfertaDisciplina> ofertas = (List<OfertaDisciplina>) getWrappedData();
        for (OfertaDisciplina oferta : ofertas) {
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
}
