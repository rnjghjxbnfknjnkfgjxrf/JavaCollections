package collectionsPackage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Diamonds {
    private int index;
    private double weight;
    private String cutQuality;
    private char color;
    private String clarity;
    private double depth;
    private double width;
    private int price;
    private double x;
    private double y;

    public Diamonds(){}

    public int getIndex(){return index;}
    public double getWeight(){return weight;}
    public String getCutQuality(){return cutQuality;}
    public char getColor(){return color;}
    public String getClarity(){return clarity;}
    public double getDepth(){return depth;}
    public double getWidth(){return width;}
    public int getPrice(){return price;}
    public double getX(){return x;}
    public double getY(){return y;}

    public void setIndex(int index) throws IntValueException {
        if (index > 0) {
            this.index = index;
        }
        else {
            throw new IntValueException("Index",index);
        }
    }
    public void setWeight(double weight) throws DoubleValueException {
        if (weight > 0) {
            this.weight = weight;
        }
        else {
            throw new DoubleValueException("Weight", weight);
        }
    }
    public void setCutQuality(String cutQuality) throws StringValueException {
        List<String> possibleCutQualities = Arrays.asList("Fair", "Good", "Very Good", "Premium", "Ideal");
        if (possibleCutQualities.contains(cutQuality)) {
            this.cutQuality = cutQuality;
        } else {
            throw new StringValueException("Only allowed: Fair, Good, Very Good, Premium, Ideal", cutQuality, "cut quality");
        }
    }
    public void setColor(char color) throws ColorException{
        if (color > 'C' && color < 'K') {
            this.color = color;
        } else{
            throw new ColorException("It must be a capital letter from D to J", color);
        }
    }
    public void setClarity(String clarity) throws StringValueException{
        List<String> clarityTypes = Arrays.asList("FL", "IF", "VVS1", "VVS2", "VS1", "VS2", "SI1", "SI2", "I1", "I2", "I3") ;
        if (clarityTypes.contains(clarity)){
            this.clarity = clarity;
        } else {
            throw new StringValueException("Only allowed:"+
                    "FL, IF, VVS1, VVS2, VS1, VS2, SI1, SI2, I1, I2, I3",
                    clarity, "clarity");
        }
    }
    public void setDepth(double depth) throws DoubleValueException{
        if (depth > 0) {
            this.depth = depth;
        }
        else {
            throw new DoubleValueException("Depths",depth);
        }
    }
    public void setWidth(double width) throws DoubleValueException{
        if (width > 0) {
            this.width = width;
        }
        else {
            throw new DoubleValueException("Width", width);
        }
    }
    public void setPrice(int price) throws IntValueException{
        if (price > 0) {
            this.price = price;
        }
        else {
            throw new IntValueException("Price", price);
        }
    }
    public void setX(double x) throws DoubleValueException{
        if (x > 0) {
            this.x = x;
        }
        else {
            throw new DoubleValueException("X", x);
        }
    }
    public void setY(double y) throws DoubleValueException{
        if (y > 0) {
            this.y = y;
        }
        else {
            throw new DoubleValueException("Y", y);
        }
    }

}

//Реализация интерфейса Comparator<T> для возможности сортировке по стоимости бриллианта
class PriceComparator implements Comparator<Diamonds>{
    public int compare(Diamonds d1, Diamonds d2){ return Integer.compare(d1.getPrice(), d2.getPrice());}
}


class DiamondException extends Exception{
    public DiamondException(String message){
        super(message);
    }
    @Override
    public String toString(){
        return "Error data occurred" + getMessage();
    }
}

class IntValueException extends DiamondException{
    private final int value;
    public IntValueException(String message, int value){
        super(message);
        this.value = value;
    }
    @Override
    public String toString(){
        return "Invalid integer value:" + value +"\n"+ getMessage()+" can't be less than 1";
    }
}

class DoubleValueException extends DiamondException{
    private final double value;
    public DoubleValueException(String message, double value){
        super(message);
        this.value = value;
    }
    @Override
    public String toString(){
        return "Invalid double value:" + value +"\n"+ getMessage()+" can't be 0 or negative";
    }
}

class StringValueException extends DiamondException{
    private final String value;
    private final String type;
    public StringValueException(String message, String value, String type){
        super(message);
        this.value = value;
        this.type = type;
    }
    @Override
    public String toString(){
        return "Invalid " +type+ " value:"+value +"\n"+ getMessage();
    }
}

class ColorException extends DiamondException{
    private final char color;
    public ColorException(String message, char color){
        super(message);
        this.color = color;
    }
    @Override
    public String toString(){
        return "Invalid color:" + color +"\n"+ getMessage();
    }
}