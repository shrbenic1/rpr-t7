package ba.unsa.rpr.tutorijal7;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UN implements Serializable {
    private List<Drzava> drzave = new ArrayList<>();

    public UN() {
    }

    public List<Drzava> getDrzave() {
        return drzave;
    }

    public void setDrzave(List<Drzava> drzave) {
        this.drzave = drzave;
    }
}
