package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.Turma;
import org.primefaces.model.SelectableDataModel;

public class TurmaDataModel extends ListDataModel implements SelectableDataModel<Turma> {

    public TurmaDataModel() {
    }

    public TurmaDataModel(List<Turma> data) {
        super(data);
    }

    @Override
    public Turma getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
        List<Turma> turmas = (List<Turma>) getWrappedData();
        for (Turma turma : turmas) {
            if (turma.getID().equals(rowKey)) {
                return turma;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(Turma turma) {
        return turma.getID();
    }
}
