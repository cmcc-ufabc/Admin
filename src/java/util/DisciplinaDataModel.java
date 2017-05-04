package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import model.Disciplina;
import org.primefaces.model.SelectableDataModel;

public class DisciplinaDataModel extends ListDataModel implements SelectableDataModel<Disciplina> {

    public DisciplinaDataModel() {
    }

    public DisciplinaDataModel(List<Disciplina> data) {
        super(data);
    }

    @Override
    public Disciplina getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<Disciplina> disciplinas = (List<Disciplina>) getWrappedData();
        for (Disciplina disciplina : disciplinas) {
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
}
