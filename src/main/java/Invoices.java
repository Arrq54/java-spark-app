import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class Invoices {

    public static void basicinvoice(Car car, ArrayList<String> models, HashMap<String, BaseColor> colors){
        try{
            Document document = new Document(); // dokument pdf
            String path = "invoices/"+car.getUuid()+".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk("Faktura dla: "+car.getUuid(), font); // akapit
            document.add(chunk);
            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
            Paragraph p = new Paragraph("Model = "+car.getModel(), font2);
            document.add(p);
            String color = car.getColor();
            List<String> keysAsArray = new ArrayList<String>(colors.keySet());
            if(keysAsArray.contains(color)){
                Font font3 = FontFactory.getFont(FontFactory.HELVETICA, 14, colors.get(color));
                Paragraph p2 = new Paragraph("kolor: "+car.getColor(), font3);
                document.add(p2);
            }
            Font font4 = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Paragraph p3 = new Paragraph("Rok = "+car.getYear(), font4);
            document.add(p3);
            Paragraph p4 = new Paragraph("Rok = "+car.getYear(), font4);
            ArrayList<Airbag> a = car.getAirbags();
            for(Airbag x : a){
                p4 = new Paragraph("poduszka: "+x.getDescription() + "-" + x.isValue(), font4);
                document.add(p4);
            }
            if(models.contains(car.getModel())){
                Image img = Image.getInstance("invoices/imgs/"+car.getModel()+".jpg");
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() - 0) / img.getWidth()) * 100;
                img.scalePercent(scaler);
                document.add(img);
            }
            document.close();
        }catch(Exception x ){
            System.out.println(x.toString());
        }
    }



    public static String allcars(ArrayList<Car> cars){
        try{
            LocalDate currentdate = LocalDate.now();
            Date date = new Date();   // given date
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            Document document = new Document(); // dokument pdf
            Invoice invoice = new Invoice(
                    String.valueOf(currentdate.getYear()),
                    String.valueOf(currentdate.getMonthValue()),
                    String.valueOf(currentdate.getDayOfMonth()),
                    String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),
                    String.valueOf(calendar.get(Calendar.MINUTE)),
                    String.valueOf(calendar.get(Calendar.SECOND)),
                    "Faktura za wszystkie auta",
                    ": firma sprzedajca auta",
                    "Nabywca",
                    cars
                    );
            String path = "invoices/allcars"+invoice.getDay()+"_"+invoice.getMoth()+"_"+invoice.getYear() + "_"  + invoice.getHour() + "_"+invoice.getMinutes() +"_"+invoice.getSeconds()+ ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font2 = FontFactory.getFont(FontFactory.TIMES_BOLD, 20, BaseColor.BLACK);
            Paragraph p = new Paragraph("Faktura:"+ invoice.generateNumber(), font2);
            document.add(p);
            Font font3 = FontFactory.getFont(FontFactory.TIMES, 14, BaseColor.BLACK);
            Paragraph p1 = new Paragraph("Nabywca: "+ invoice.getBuyer() + ", Firma sprzedająca auta"+invoice.getSeller(),font3);
            document.add(p1);
            Font font4 = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.RED);
            Paragraph p2 = new Paragraph(invoice.getTitle(),font4);
            document.add(p2);
            Font enter = FontFactory.getFont(FontFactory.TIMES_BOLD, 50, BaseColor.WHITE);
            Paragraph enterR = new Paragraph(".",enter);
            document.add(enterR);
            DecimalFormat df = new DecimalFormat("0.00");
            PdfPTable table = new PdfPTable(4);
            Font fonttable = FontFactory.getFont(FontFactory.TIMES, 10, BaseColor.BLACK);
            PdfPCell th1 = new PdfPCell(new Phrase("LP.", fonttable));
            PdfPCell th2 = new PdfPCell(new Phrase("Cena", fonttable));
            PdfPCell th3 = new PdfPCell(new Phrase("VAT", fonttable));
            PdfPCell th4 = new PdfPCell(new Phrase("Wartość", fonttable));
            table.addCell(th1);
            table.addCell(th2);
            table.addCell(th3);
            table.addCell(th4);
            double sum = 0;
            for(int i=0;i<cars.size();i++){
                PdfPCell td1 = new PdfPCell(new Phrase(String.valueOf(i+1), fonttable));
                PdfPCell td2 = new PdfPCell(new Phrase(String.valueOf(cars.get(i).getPrice()), fonttable));
                HashMap<String, Double> vats = new HashMap<String, Double>();
                vats.put("7%", 0.07);
                vats.put("0%", 0.0);
                vats.put("22%", 0.22);
                Random generator = new Random();
                Object[] keys = vats.keySet().toArray();
                Object vat = keys[generator.nextInt(keys.length)];
                PdfPCell td3 = new PdfPCell(new Phrase((String) vat, fonttable));
                PdfPCell td4 = new PdfPCell(new Phrase(String.valueOf(df.format(cars.get(i).getPrice() + (cars.get(i).getPrice() * vats.get(vat)))), fonttable));
                sum += cars.get(i).getPrice() + (cars.get(i).getPrice() * vats.get(vat));
                table.addCell(td1);
                table.addCell(td2);
                table.addCell(td3);
                table.addCell(td4);
            }
            document.add(table);
            Font font6 = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.BLACK);
            Paragraph p6 = new Paragraph("Do zapłaty: "+df.format(sum)+" PLN", font6);
            document.add(p6);
            document.close();
            return "allcars"+invoice.getDay()+"_"+invoice.getMoth()+"_"+invoice.getYear() + "_"  + invoice.getHour() + "_"+invoice.getMinutes() +"_"+invoice.getSeconds()+".pdf";
        }catch(Exception x ){
            System.out.println(x.toString());
        }
        return "";
    }

    public static String byyear(ArrayList<Car> cars, int year){
        try{
            LocalDate currentdate = LocalDate.now();
            Date date = new Date();   // given date
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            Document document = new Document(); // dokument pdf
            Invoice invoice = new Invoice(
                    String.valueOf(currentdate.getYear()),
                    String.valueOf(currentdate.getMonthValue()),
                    String.valueOf(currentdate.getDayOfMonth()),
                    String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),
                    String.valueOf(calendar.get(Calendar.MINUTE)),
                    String.valueOf(calendar.get(Calendar.SECOND)),
                    "Faktura za auta z roku: "+String.valueOf(year),
                    ": firma sprzedajca auta",
                    "Nabywca",
                    cars
            );
            String path = "invoices/byyear"+invoice.getDay()+"_"+invoice.getMoth()+"_"+invoice.getYear() + "_"  + invoice.getHour() + "_"+invoice.getMinutes() +"_"+invoice.getSeconds()+ ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font2 = FontFactory.getFont(FontFactory.TIMES_BOLD, 20, BaseColor.BLACK);
            Paragraph p = new Paragraph("Faktura:"+ invoice.generateNumber(), font2);
            document.add(p);
            Font font3 = FontFactory.getFont(FontFactory.TIMES, 14, BaseColor.BLACK);
            Paragraph p1 = new Paragraph("Nabywca: "+ invoice.getBuyer() + ", Firma sprzedająca auta"+invoice.getSeller(),font3);
            document.add(p1);
            Font font4 = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.RED);
            Paragraph p2 = new Paragraph(invoice.getTitle(),font4);
            document.add(p2);
            Font enter = FontFactory.getFont(FontFactory.TIMES_BOLD, 50, BaseColor.WHITE);
            Paragraph enterR = new Paragraph(".",enter);
            document.add(enterR);
            DecimalFormat df = new DecimalFormat("0.00");
            PdfPTable table = new PdfPTable(5);
            Font fonttable = FontFactory.getFont(FontFactory.TIMES, 10, BaseColor.BLACK);
            PdfPCell th1 = new PdfPCell(new Phrase("LP.", fonttable));
            PdfPCell th5 = new PdfPCell(new Phrase("Rok", fonttable));
            PdfPCell th2 = new PdfPCell(new Phrase("Cena", fonttable));
            PdfPCell th3 = new PdfPCell(new Phrase("VAT", fonttable));
            PdfPCell th4 = new PdfPCell(new Phrase("Wartość", fonttable));
            table.addCell(th1);
            table.addCell(th5);
            table.addCell(th2);
            table.addCell(th3);
            table.addCell(th4);
            double sum = 0;
            int counter = 0;
            for(int i=0;i<cars.size();i++){
                if(cars.get(i).getYear() == year){
                    PdfPCell td1 = new PdfPCell(new Phrase(String.valueOf(counter+1), fonttable));
                    PdfPCell td15 = new PdfPCell(new Phrase(String.valueOf(cars.get(i).getYear()), fonttable));
                    PdfPCell td2 = new PdfPCell(new Phrase(String.valueOf(cars.get(i).getPrice()), fonttable));

                    HashMap<String, Double> vats = new HashMap<String, Double>();
                    vats.put("7%", 0.07);
                    vats.put("0%", 0.0);
                    vats.put("22%", 0.22);
                    Random generator = new Random();
                    Object[] keys = vats.keySet().toArray();
                    Object vat = keys[generator.nextInt(keys.length)];
                    PdfPCell td3 = new PdfPCell(new Phrase((String) vat, fonttable));
                    PdfPCell td4 = new PdfPCell(new Phrase(String.valueOf(df.format(cars.get(i).getPrice() + (cars.get(i).getPrice() * vats.get(vat)))), fonttable));
                    sum += cars.get(i).getPrice() + (cars.get(i).getPrice() * vats.get(vat));
                    table.addCell(td1);
                    table.addCell(td15);
                    table.addCell(td2);
                    table.addCell(td3);
                    table.addCell(td4);
                    counter +=1;
                }

            }
            document.add(table);
            Font font6 = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.BLACK);
            Paragraph p6 = new Paragraph("Do zapłaty: "+df.format(sum)+" PLN", font6);
            document.add(p6);
            document.close();
            return "byyear"+invoice.getDay()+"_"+invoice.getMoth()+"_"+invoice.getYear() + "_"  + invoice.getHour() + "_"+invoice.getMinutes() +"_"+invoice.getSeconds()+".pdf";
        }catch(Exception x ){
            System.out.println(x.toString());
        }
        return "";
    }

    public static String byprice(ArrayList<Car> cars, int from, int to){
        try{
            LocalDate currentdate = LocalDate.now();
            Date date = new Date();   // given date
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            Document document = new Document(); // dokument pdf
            Invoice invoice = new Invoice(
                    String.valueOf(currentdate.getYear()),
                    String.valueOf(currentdate.getMonthValue()),
                    String.valueOf(currentdate.getDayOfMonth()),
                    String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),
                    String.valueOf(calendar.get(Calendar.MINUTE)),
                    String.valueOf(calendar.get(Calendar.SECOND)),
                    "Faktura za auta w cenach: "+String.valueOf(from)+"-"+to+" PLN",
                    ": firma sprzedajca auta",
                    "Nabywca",
                    cars
            );

            String path = "invoices/byprice"+invoice.getDay()+"_"+invoice.getMoth()+"_"+invoice.getYear() + "_"  + invoice.getHour() + "_"+invoice.getMinutes() +"_"+invoice.getSeconds()+ ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font2 = FontFactory.getFont(FontFactory.TIMES_BOLD, 20, BaseColor.BLACK);
            Paragraph p = new Paragraph("Faktura:"+ invoice.generateNumber(), font2);
            document.add(p);
            Font font3 = FontFactory.getFont(FontFactory.TIMES, 14, BaseColor.BLACK);
            Paragraph p1 = new Paragraph("Nabywca: "+ invoice.getBuyer() + ", Firma sprzedająca auta"+invoice.getSeller(),font3);
            document.add(p1);
            Font font4 = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.RED);
            Paragraph p2 = new Paragraph(invoice.getTitle(),font4);
            document.add(p2);
            Font enter = FontFactory.getFont(FontFactory.TIMES_BOLD, 50, BaseColor.WHITE);
            Paragraph enterR = new Paragraph(".",enter);
            document.add(enterR);
            DecimalFormat df = new DecimalFormat("0.00");
            PdfPTable table = new PdfPTable(5);
            Font fonttable = FontFactory.getFont(FontFactory.TIMES, 10, BaseColor.BLACK);
            PdfPCell th1 = new PdfPCell(new Phrase("LP.", fonttable));
            PdfPCell th5 = new PdfPCell(new Phrase("Rok", fonttable));
            PdfPCell th2 = new PdfPCell(new Phrase("Cena", fonttable));
            PdfPCell th3 = new PdfPCell(new Phrase("VAT", fonttable));
            PdfPCell th4 = new PdfPCell(new Phrase("Wartość", fonttable));
            table.addCell(th1);
            table.addCell(th5);
            table.addCell(th2);
            table.addCell(th3);
            table.addCell(th4);
            double sum = 0;
            int counter = 0;
            for(int i=0;i<cars.size();i++){
                if(cars.get(i).getPrice()>=from && cars.get(i).getPrice()<=to){
                    PdfPCell td1 = new PdfPCell(new Phrase(String.valueOf(counter+1), fonttable));
                    PdfPCell td15 = new PdfPCell(new Phrase(String.valueOf(cars.get(i).getYear()), fonttable));
                    PdfPCell td2 = new PdfPCell(new Phrase(String.valueOf(cars.get(i).getPrice()), fonttable));
                    HashMap<String, Double> vats = new HashMap<String, Double>();
                    vats.put("7%", 0.07);
                    vats.put("0%", 0.0);
                    vats.put("22%", 0.22);
                    Random generator = new Random();
                    Object[] keys = vats.keySet().toArray();
                    Object vat = keys[generator.nextInt(keys.length)];
                    PdfPCell td3 = new PdfPCell(new Phrase((String) vat, fonttable));
                    PdfPCell td4 = new PdfPCell(new Phrase(String.valueOf(df.format(cars.get(i).getPrice() + (cars.get(i).getPrice() * vats.get(vat)))), fonttable));
                    sum += cars.get(i).getPrice() + (cars.get(i).getPrice() * vats.get(vat));
                    table.addCell(td1);
                    table.addCell(td15);
                    table.addCell(td2);
                    table.addCell(td3);
                    table.addCell(td4);
                    counter +=1;
                }
            }
            document.add(table);
            Font font6 = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.BLACK);
            Paragraph p6 = new Paragraph("Do zapłaty: "+df.format(sum)+" PLN", font6);
            document.add(p6);
            document.close();
            return "byprice"+invoice.getDay()+"_"+invoice.getMoth()+"_"+invoice.getYear() + "_"  + invoice.getHour() + "_"+invoice.getMinutes() +"_"+invoice.getSeconds()+".pdf";
        }catch(Exception x ){
            System.out.println(x.toString());
        }
        return "";
    }
}
