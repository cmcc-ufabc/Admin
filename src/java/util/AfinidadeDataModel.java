package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.Afinidade;
import org.primefaces.model.SelectableDataModel;

public class AfinidadeDataModel extends ListDataModel implements SelectableDataModel<Afinidade> {

    public AfinidadeDataModel() {
    }

    public AfinidadeDataModel(List<Afinidade> data) {
        super(data);
    }

    @Override
    public Afinidade getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
        List<Afinidade> afinidades = (List<Afinidade>) getWrappedData();
        for (Afinidade afinidade : afinidades) {
            if (afinidade.getId().equals(rowKey)) {
                return afinidade;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Afinidade afinidade) {
        return afinidade.getId();
    }
}
