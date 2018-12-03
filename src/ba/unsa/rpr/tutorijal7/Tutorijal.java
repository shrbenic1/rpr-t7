package ba.unsa.rpr.tutorijal7;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        for (var x : gradovi) {
            System.out.println(x);
        }
        UN un = Tutorijal.ucitajXml(gradovi);
    }

    public static ArrayList<Grad> ucitajGradove() {
        ArrayList<Grad> gradovi = new ArrayList<>();
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileReader("mjerenja.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Datoteka mjerenja.txt se ne može otvoriti");
            System.exit(1);
        }
        try {
            while (ulaz.hasNext()) {
                String[] podaci = ulaz.nextLine().split(",");
                Grad grad = new Grad();
                grad.setNaziv(podaci[0]);
                double[] niz = new double[podaci.length - 1];
                for (int i = 1; i < 1000; i++) {
                    if (i == podaci.length) {
                        break;
                    }
                    niz[i - 1] = Double.parseDouble(podaci[i]);
                }
                grad.setTemperature(niz);
                gradovi.add(grad);
            }
        } catch (Exception e) {
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
        NodeList drzave = xmldoc.getDocumentElement().getChildNodes();
        ArrayList<Drzava> spisakDrzava = new ArrayList<>();
        for (int i = 0; i < drzave.getLength(); i++) {
            Node drzava = drzave.item(i);
            Drzava novaDrzava = new Drzava();
            if (drzava instanceof Element) {
                Element e = (Element) drzava;
                novaDrzava.setBrojStanovnika(Integer.parseInt(e.getAttribute("stanovnika")));
                NodeList podaci = e.getChildNodes();
                for (int j = 0; j < podaci.getLength(); j++) {
                    Node podatak = podaci.item(j);
                    if (podatak instanceof Element) {
                        Element noviPodatak = (Element) podatak;
                        switch (noviPodatak.getTagName()) {
                            case "naziv":
                                novaDrzava.setNaziv(noviPodatak.getTextContent());
                                break;
                            case "glavnigrad":
                                Grad glavniGrad = new Grad();
                                glavniGrad.setBrojStanovnika(Integer.parseInt(noviPodatak.getAttribute("stanovnika")));
                                NodeList nazivi = noviPodatak.getChildNodes();
                                for (int k = 0; k < nazivi.getLength(); k++) {
                                    Node naziv = nazivi.item(k);
                                    if (naziv instanceof Element) {
                                        if (((Element) naziv).getTagName().equals("naziv")) {
                                            glavniGrad.setNaziv(naziv.getTextContent());
                                            boolean imaMjerenja = false;
                                            for (var x : gradovi) {
                                                if (glavniGrad.getNaziv().equals(x.getNaziv())) {
                                                    glavniGrad.setTemperature(x.getTemperature());
                                                    imaMjerenja = true;
                                                }
                                            }
                                            if (!imaMjerenja) {
                                                double[] niz = {};
                                                glavniGrad.setTemperature(niz);
                                            }
                                            novaDrzava.setGlavniGrad(glavniGrad);
                                        }
                                    }
                                }
                                break;
                            case "povrsina":
                                novaDrzava.setJedinicaZaPovrsinu(noviPodatak.getAttribute("jedinica"));
                                novaDrzava.setPovrsina(Double.parseDouble(noviPodatak.getTextContent()));
                                break;
                        }
                    }
                }
                spisakDrzava.add(novaDrzava);
            }
        }
        un.setDrzave(spisakDrzava);
        return un;
    }
}
