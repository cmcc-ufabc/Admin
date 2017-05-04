package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.Disponibilidade;
import org.primefaces.model.SelectableDataModel;

public class DisponibilidadeDataModel extends ListDataModel implements SelectableDataModel<Disponibilidade> {

    public DisponibilidadeDataModel() {
    }

    public DisponibilidadeDataModel(List<Disponibilidade> data) {
        super(data);
    }

    @Override
    public Disponibilidade getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
        List<Disponibilidade> disponibilidades = (List<Disponibilidade>) getWrappedData();

        for (Disponibilidade docente : disponibilidades) {
            if (docente.getId().equals(rowKey)) {
                return docente;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Disponibilidade disponibilidade) {
        return disponibilidade.getId();
    }
}
