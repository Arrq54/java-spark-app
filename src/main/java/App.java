import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.imgscalr.Scalr;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import static spark.Spark.*;
public class App {
    private static ArrayList<Car> carsList = new ArrayList<Car>();
    private static ArrayList<String> allcarsinvoices = new ArrayList<String>();
    private static ArrayList<String> byyearinvoices = new ArrayList<String>();
    private static ArrayList<String> bypriceinvoices = new ArrayList<String>();

    private static String uuidForUpload = "";
    public static void main(String[] args) {
        tempPhotos = new ArrayList<>();
        photosForCars = new HashMap<>();
        String projectDir = System.getProperty("user.dir");
        String staticDir = "/src/main/resources/public";
        staticFiles.externalLocation(projectDir + staticDir);

//        staticFiles.location("/public");
        models.add("Mercedes");
        models.add("Ferrari");
        models.add("Renault");
        models.add("Fiat");
        models.add("Polonez");
        models.add("Opel");
        models.add("Mini");
        post("/add", (req,res)->addCar(req,res) );
        get("/json", (req,res)->getJSON(req,res));
        get("/getYears", (req,res)->getYears(req,res));
        post("/delete", (req,res)->deleteCar(req,res) );
        post("/update", (req,res)->updateCar(req,res) );
        get("/generate", (req,res)->generateCars(req,res));
        post("/invoice", (req,res)->generateInvoice(req,res) );
        post("/invoiceforallcars", (req,res)->generateInvoiceForAlCars(req,res) );
        post("/invoiceforallcarsyear", (req,res)->generateInvoiceForAlCarsFromYear(req,res) );
        post("/invoiceforallcarsprice", (req,res)->generateInvoiceForAlCarsByPrice(req,res) );

        get("/getinvoice", (req,res)->getInvoice(req,res) );
        get("/getAllCarsInvoice", (req,res)->getAllCarsInvoice(req,res) );

        get("/thumb", (req,res)->getThumb(req,res) );


        get("/upload", (req,res)->setUploadingUUID(req,res) );
        get("/gallery", (req,res)->setGalleryUUID(req,res) );

        post("/uploadfiles", (req,res)->saveUploadedFiles(req,res) );

        get("/deletefile", (req,res)->deleteFile(req,res) );
        post("/savecurrentfiles", (req,res)->saveCurrentFiles(req,res) );

        post("/cropimage", (req,res)->cropImage(req,res) );
        post("/rotateimg", (req,res)->rotateImage(req,res) );
        post("/flipH", (req,res)->flipH(req,res) );
        post("/flipV", (req,res)->flipV(req,res) );

        post("/getfiles", (req,res)->getFilesForGallery(req,res) );
        get("/getwidthheight", (req,res)->getWidthHeight(req,res) );
    }
    static String addCar(Request req, Response res){
        Gson gson = new Gson();
        Car car = gson.fromJson(req.body(), Car.class);
        car.setId(carsList.size());
        car.setUuid(Generators.randomBasedGenerator().generate());
        System.out.println(car);
        carsList.add(car);
        return gson.toJson(car, Car.class );
    }

    static HashMap<String, BaseColor> colors = new HashMap<>() {{
        put("red", BaseColor.RED);
        put("blue", BaseColor.BLUE);
        put("cyan", BaseColor.CYAN);
        put("black", BaseColor.BLACK);
        put("orange", BaseColor.ORANGE);
        put("pink", BaseColor.PINK);
    }};
    static ArrayList<String> models = new ArrayList<>();
    static String generateCars(Request req, Response res){
        for(int i=0;i<20;i++){
            Car tempCar = new Car();
            tempCar.setModel( models.get((int)(Math.random()*models.size())));
            tempCar.setYear( ((int) (Math.random()*(2010 - 1970))) + 1970);
            tempCar.setPrice( ((int) (Math.random()*(100000 - 10000))) + 10000);
            Random rd = new Random();
            ArrayList<Airbag> tempAirbags = new ArrayList<>();
            tempAirbags.add(new Airbag("kierowca",rd.nextBoolean() ));
            tempAirbags.add(new Airbag("pasazer",rd.nextBoolean() ));
            tempAirbags.add(new Airbag("kanapa",rd.nextBoolean() ));
            tempAirbags.add(new Airbag("boczne",rd.nextBoolean() ));
            tempCar.setAirbags(tempAirbags);
            List<String> keysAsArray = new ArrayList<String>(colors.keySet());
            tempCar.setColor(keysAsArray.get((int)(Math.random()*keysAsArray.size())));
            tempCar.setId(carsList.size());
            carsList.add(tempCar);

        }



        Gson gson = new Gson();
        return gson.toJson(carsList, ArrayList.class );
    }
    static String deleteCar(Request req, Response res){
        Gson gson = new Gson();
        String uuid = (gson.fromJson(req.body(), Car.class).getUuid());
        int i=0;
        int todelete = 0;
        for(Car x : carsList){
            if(Objects.equals(x.getUuid(), uuid)){
                todelete = i;
            }
            i++;
        }
        carsList.remove(todelete);
        return gson.toJson(carsList, ArrayList.class );

    }


    static String updateCar(Request req, Response res){
        Gson gson = new Gson();
        String uuid = (gson.fromJson(req.body(), Car.class).getUuid());
        String model = (gson.fromJson(req.body(), Car.class).getModel());
        int year = (gson.fromJson(req.body(), Car.class).getYear());
        for(Car x : carsList){
            if(Objects.equals(x.getUuid(), uuid)){
                x.setModel(model);
                x.setYear(year);
            }
        }
        return gson.toJson(carsList, ArrayList.class );

    }


    static String generateInvoice(Request req, Response res){
        Gson gson = new Gson();
        String uuid = (gson.fromJson(req.body(), Car.class).getUuid());
        System.out.println(uuid);
        for(Car x : carsList){
            if(Objects.equals(x.getUuid(), uuid)){
                x.setInvoice(true);
                Invoices.basicinvoice(x,models,colors);
            }

        }
        return gson.toJson(carsList, ArrayList.class );
    }




    static String generateInvoiceForAlCars(Request req, Response res){
        Gson gson = new Gson();
        allcarsinvoices.add( Invoices.allcars(carsList));

        return gson.toJson(allcarsinvoices, ArrayList.class );
    }


    static String generateInvoiceForAlCarsFromYear(Request req, Response res){
        Gson gson = new Gson();
        int year = (gson.fromJson(req.body(), Car.class).getYear());
        byyearinvoices.add( Invoices.byyear(carsList, year));
        return gson.toJson(byyearinvoices, ArrayList.class );
    }

    static String getWidthHeight(Request req, Response res) throws IOException {
        String name =  req.queryParams("name");
        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = ImageIO.read(sourceFile);

        Gson gson = new Gson();
        HashMap<String, Integer> response = new HashMap<>();
        response.put("width", originalImage.getWidth());
        response.put("height", originalImage.getHeight());

        return gson.toJson(response, HashMap.class );
    }

    static String uuidforGallery;
    static String setGalleryUUID(Request req, Response res){
        Gson gson = new Gson();
        String uuid =  req.queryParams("uuid");
        uuidforGallery = uuid;
        res.redirect("/Gallery.html");
        return "";
    }
    static String setUploadingUUID(Request req, Response res){
        Gson gson = new Gson();
        String uuid =  req.queryParams("uuid");
        uuidForUpload = uuid;
        System.out.println(uuidForUpload);
        tempPhotos.removeAll(tempPhotos);
        res.redirect("/Upload.html");
        return "";
    }
    static String getThumb(Request req, Response res) throws IOException {
        String path = req.queryParams("id");
        File file = new File("images/"+path);

        if(path.contains(".jpeg") || path.contains((".jpg"))){
            res.type("image/jpeg");
        } else{
            res.type("image/png");
        }


        OutputStream outputStream = null;
        outputStream = res.raw().getOutputStream();

        outputStream.write(Files.readAllBytes(Path.of("images/"+path)));
        outputStream.flush();
        return "";
    }



    static String generateInvoiceForAlCarsByPrice(Request req, Response res){
        Gson gson = new Gson();
        int from = (gson.fromJson(req.body(), Searchbyprice.class).getFrom());
        int to = (gson.fromJson(req.body(), Searchbyprice.class).getTo());

        bypriceinvoices.add(  Invoices.byprice(carsList, from, to));
        return gson.toJson(bypriceinvoices, ArrayList.class );
    }


    static String getAllCarsInvoice(Request req, Response res){
        System.out.println();
        String filename = req.queryParams("link");
        res.type("application/octet-stream"); //
        res.header("Content-Disposition", "attachment; filename="+filename); // nagłówek

        try{
            OutputStream outputStream = res.raw().getOutputStream();
            outputStream.write(Files.readAllBytes(Path.of("invoices/" + filename))); // response pliku do przeglądarki
        }catch(Exception x){
            System.out.println(x);
        }
        return "";
    }
    static String getInvoice(Request req, Response res){
        System.out.println();
        String filename = req.queryParams("uuid")+".pdf";
        res.type("application/octet-stream"); //
        res.header("Content-Disposition", "attachment; filename="+filename); // nagłówek

        try{
            OutputStream outputStream = res.raw().getOutputStream();
            outputStream.write(Files.readAllBytes(Path.of("invoices/" + filename))); // response pliku do przeglądarki
        }catch(Exception x){
            System.out.println(x);
        }
        return "";
    }

    static String getJSON(Request req, Response res){
        Gson gson = new Gson();
        return gson.toJson(carsList, ArrayList.class );
    }
    static String getYears(Request req, Response res){
        Gson gson = new Gson();
        Helpers h = new Helpers();
        return gson.toJson(h.getYearsList(carsList), ArrayList.class );
    }
    static HashMap<String,ArrayList<String>> photosForCars;
    static ArrayList<String> tempPhotos;
    static String deleteFile(Request req, Response res)  {
        Gson gson = new Gson();
        String filenametodelete = req.queryParams("name");
        tempPhotos.remove(filenametodelete);
        return gson.toJson(tempPhotos, ArrayList.class );
    }


    static String saveCurrentFiles(Request req, Response res) {
        ArrayList<String> temp = new ArrayList<>();
        if(photosForCars.get(uuidForUpload)!=null){
            temp =new ArrayList<String>(photosForCars.get(uuidForUpload));
            temp.addAll(tempPhotos);
            photosForCars.put(uuidForUpload,new ArrayList<String>(temp));
        }else{
            photosForCars.put(uuidForUpload,new ArrayList<String>(tempPhotos));
        }

        tempPhotos.removeAll(tempPhotos);
        return "";
    }
    static String cropImage(Request req, Response res) throws IOException {
        Gson gson = new Gson();
        int x = (gson.fromJson(req.body(), CropArea.class).getX());
        int y = (gson.fromJson(req.body(), CropArea.class).getY());
        int w = (gson.fromJson(req.body(), CropArea.class).getW());
        int h = (gson.fromJson(req.body(), CropArea.class).getH());

        String name = (gson.fromJson(req.body(), CropArea.class).getFilename());

        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = ImageIO.read(sourceFile);

        BufferedImage targetImage = Scalr.crop(originalImage, x, y, w, h); // x,y,w,h
        File targetFile = new File("images/"+name);

        ImageIO.write(targetImage, "jpg", targetFile);

        originalImage.flush();
        targetImage.flush();
        return "";
    }

    static String rotateImage(Request req, Response res) throws IOException {
        Gson gson = new Gson();
        String name = (gson.fromJson(req.body(), CropArea.class).getFilename());

        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = ImageIO.read(sourceFile);

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.CW_90);
        File targetFile = new File("images/"+name);

        ImageIO.write(targetImage, "jpg", targetFile);

        originalImage.flush();
        targetImage.flush();
        return "";
    }
    static String flipH(Request req, Response res) throws IOException {
        Gson gson = new Gson();
        String name = (gson.fromJson(req.body(), CropArea.class).getFilename());

        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = ImageIO.read(sourceFile);

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.FLIP_HORZ);
        File targetFile = new File("images/"+name);

        ImageIO.write(targetImage, "jpg", targetFile);

        originalImage.flush();
        targetImage.flush();
        return "";
    }
    static String flipV(Request req, Response res) throws IOException {
        Gson gson = new Gson();
        String name = (gson.fromJson(req.body(), CropArea.class).getFilename());

        File sourceFile = new File("images/"+name);
        BufferedImage originalImage = ImageIO.read(sourceFile);

        BufferedImage targetImage = Scalr.rotate(originalImage, Scalr.Rotation.FLIP_VERT);
        File targetFile = new File("images/"+name);

        ImageIO.write(targetImage, "jpg", targetFile);

        originalImage.flush();
        targetImage.flush();
        return "";
    }
    static String getFilesForGallery(Request req, Response res)  {
        Gson gson = new Gson();
        System.out.println(photosForCars);
        return gson.toJson(photosForCars.get(uuidforGallery), ArrayList.class );

    }

    static String saveUploadedFiles(Request req, Response res) throws IOException, ServletException {
        Gson gson = new Gson();
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/images"));
        for(Part p : req.raw().getParts()){
            InputStream inputStream;
            inputStream = p.getInputStream();
            String extension = p.getContentType().split("/")[1];
            byte[] bytes = inputStream.readAllBytes();
            Helpers h = new Helpers();
            String fileName = h.uniqueName();
            FileOutputStream fos = new FileOutputStream("images/" + fileName+"."+extension);
            fos.write(bytes);
            fos.close();
            tempPhotos.add(fileName+"."+extension);
            // dodaj do Arraylist z nazwami aut do odesłania do przeglądarki
        }
        System.out.println(tempPhotos);
        return gson.toJson(tempPhotos, ArrayList.class );
    }
}

class Car{
    private String model,color;
    private int year,id, price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private UUID uuid;
    private boolean invoice;
    public boolean isInvoice() {
        return invoice;
    }

    public void setInvoice(boolean invoice) {
        this.invoice = invoice;
    }

    public Car() {
        this.uuid = Generators.randomBasedGenerator().generate();
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setAirbags(ArrayList<Airbag> airbags) {
        this.airbags = airbags;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Airbag> getAirbags() {
        return airbags;
    }

    public int getId() {
        return id;
    }

    private ArrayList<Airbag> airbags;

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid.toString();
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Car(String model, String color, int year, ArrayList<Airbag> airbags) {
        this.model = model;
        this.color = color;
        this.year = year;
        this.airbags = airbags;
        uuid = Generators.randomBasedGenerator().generate();
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", year=" + year +
                ", id=" + id +
                ", uuid=" + uuid +
                ", invoice=" + invoice +
                ", airbags=" + airbags +
                '}';
    }
}

class Airbag{
    String description;
    boolean value;

    public Airbag(String desc, boolean value) {
        this.description = desc;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Airbag{" +
                "description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}
