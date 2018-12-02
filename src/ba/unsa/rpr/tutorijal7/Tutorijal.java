package ba.unsa.rpr.tutorijal7;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Tutorijal {
    public static void main(String[] args) {
        ArrayList<Grad> gradovi = Tutorijal.ucitajGradove();
        for(var x : gradovi) {
            System.out.println(x);
        }
    }

    public static ArrayList<Grad> ucitajGradove() {
        ArrayList<Grad> gradovi = new ArrayList<>();
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileReader("mjerenja.txt"));
        } catch(FileNotFoundException e) {
            System.out.println("Datoteka mjerenja.txt se ne može otvoriti");
            System.exit(1);
        }
        try {
            while(ulaz.hasNext()) {
                String[] podaci = ulaz.nextLine().split(",");
                Grad grad = new Grad();
                grad.setNaziv(podaci[0]);
                double[] niz = new double[podaci.length - 1];
                for(int i = 1; i < 1000; i++) {
                    if(i == podaci.length) {
                        break;
                    }
                    niz[i - 1] = Double.parseDouble(podaci[i]);
                }
                grad.setTemperature(niz);
                gradovi.add(grad);
            }
        } catch(Exception e) {
            System.out.println("Problem pri čitanju");
        } finally {
            try {
                ulaz.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return gradovi;
    }

    public static UN ucitajXml(ArrayList<Grad> gradovi) {
        UN un = new UN();
        Document xmldoc = null;
        try {
            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmldoc = docReader.parse(new File("drzave.xml"));
        } catch (Exception e) {
            System.out.println("drzave.xml nije validan XML dokument");
            System.exit(1);
        }
        obradiElement(xmldoc.getDocumentElement());
        return null;
    }

    private static void obradiElement(Element documentElement) {

    }
}
