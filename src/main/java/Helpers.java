import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Helpers {
    public ArrayList<Integer> getYearsList(ArrayList<Car> carsList){
        ArrayList<Integer> temp = new ArrayList<>();
        for(Car car : carsList){
            if(!temp.contains(car.getYear())){
                temp.add(car.getYear());
            }
        }
        return temp;
    }
    public String uniqueName(){
        return (ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ) +"_"+ ThreadLocalRandom.current().nextInt(0, 1000000 + 1)).replace('.','-').replace(':','_');
    }

}
