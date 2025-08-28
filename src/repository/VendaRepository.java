package repository;

import com.google.firebase.database.DatabaseReference;
import database.FirebaseConfig;
import model.Venda;
import model.VendaDTO;

public class VendaRepository {
    private DatabaseReference ref;

    public VendaRepository() {
        this.ref = FirebaseConfig.getDatabaseReference().child("vendas");
    }

    public void salvar(Venda venda) {
        VendaDTO dto = new VendaDTO(venda);
        ref.child(venda.getId()).setValueAsync(dto);
    }
}
