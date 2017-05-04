package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.Disp;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author erick
 */
public class DispDataModel extends ListDataModel implements SelectableDataModel<Disp> {

    public DispDataModel() {
    }

    public DispDataModel(List<Disp> data) {
        super(data);
    }

    @Override
    public Disp getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<Disp> disponibilidades = (List<Disp>) getWrappedData();
        for (Disp docente : disponibilidades) {
            if (docente.getId().equals(rowKey)) {
                return docente;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Disp disponibilidade) {
        return disponibilidade.getId();
    }
}
