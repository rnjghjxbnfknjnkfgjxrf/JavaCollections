package collectionsPackage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

//Задание 38
public class Main {

    public static void main(String[] args) throws Exception {
        //Чтение данных и их добавление в коллекцию
        LinkedList<Diamonds> diamondsList = readData();

        DecimalFormat df = new DecimalFormat("#.##");

        //Создание или открытие файла для записи данных
        File output = new File("data_diamond_output.txt");
        PrintWriter writer = new PrintWriter(output);
        if (output.exists()) writer.print("");
        else try {
            output.createNewFile();
        }catch (IOException e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        //Вывод данных на консоль и в файл
        for (String cq : diamondsList.stream().map(Diamonds::getCutQuality).collect(Collectors.toSet())){ //Отбор всех возможных качеств огранки
            System.out.println(cq+":");
            writer.println(cq+":");
            System.out.printf("|%-5s|%-10s|%-10s|%-10s|%-10s|%-10s|%n",
                    "Color","Max Price","Min Price", "Avg Price", "Avg Diam","Most Frequent Clarity");
            writer.printf("|%-5s|%-10s|%-10s|%-10s|%-10s|%-10s|%n",
                    "Color","Max Price","Min Price", "Avg Price", "Avg Diam","Most Frequent Clarity");
            for (char cl: diamondsList.stream().map(Diamonds::getColor).collect(Collectors.toSet())){ //Отбор всех возможных цветов
                if (diamondsList.stream().noneMatch((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)) continue;
                System.out.printf("|%-5s|%-10s|%-10s|%-10s|%-10s|%-21s|%n",
                        cl,
                        /*
                        * Далее идет отбор всех бриллиантов по их качеству огранке и цвету (через filter());
                        * после отбора подходящих объектов, идет поиск требуемых значений (максимальная цена, минимальная цена и т.д.)
                        * */
                        diamondsList.stream()
                                .filter((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .max(new PriceComparator()).orElseThrow().getPrice(),
                        diamondsList.stream()
                                .filter((d)->d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .min(new PriceComparator()).orElseThrow(IllegalStateException::new).getPrice(),
                        df.format(diamondsList.stream()
                                .filter((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .mapToDouble(Diamonds::getPrice)
                                .average().orElseThrow(IllegalStateException::new)),
                        df.format(diamondsList.stream()
                                .filter((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .mapToDouble((d)-> (d.getX() + d.getY()) / 2)
                                .average().orElseThrow(IllegalStateException::new)),
                        diamondsList.stream()
                                .filter((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .collect(Collectors.groupingBy(Diamonds::getClarity, Collectors.counting()))
                                .entrySet().stream()
                                .sorted(Map.Entry.comparingByKey(new ClarityComparator()))
                                .max(Map.Entry.comparingByValue())
                                .orElseThrow(IllegalAccessError::new).getKey());
                writer.printf("|%-5s|%-10s|%-10s|%-10s|%-10s|%-21s|%n", //Тот же самый вывод, но в файл
                        cl,
                        diamondsList.stream()
                                .filter((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .max(new PriceComparator()).orElseThrow().getPrice(),
                        diamondsList.stream()
                                .filter((d)->d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .min(new PriceComparator()).orElseThrow(IllegalStateException::new).getPrice(),
                        df.format(diamondsList.stream()
                                .filter((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .mapToDouble(Diamonds::getPrice)
                                .average().orElseThrow(IllegalStateException::new)),
                        df.format(diamondsList.stream()
                                .filter((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .mapToDouble((d)-> (d.getX() + d.getY()) / 2)
                                .average().orElseThrow(IllegalStateException::new)),
                        diamondsList.stream()
                                .filter((d) -> d.getCutQuality().equals(cq) && d.getColor() == cl)
                                .collect(Collectors.groupingBy(Diamonds::getClarity, Collectors.counting()))
                                .entrySet().stream()
                                .sorted(Map.Entry.comparingByKey(new ClarityComparator()))
                                .max(Map.Entry.comparingByValue())
                                .orElseThrow(IllegalAccessError::new).getKey());
            }
        }
        writer.close();

    }
    //Метод, считывающий данные из файла
    public static LinkedList<Diamonds> readData() throws IOException {
        LinkedList<Diamonds> diamondsData = new LinkedList<>();
        Scanner sc = new Scanner(new File("data_diamond.txt"));
        String[] possibleCutQualities = {"Fair", "Good", "Very Good", "Premium", "Ideal"};
        String[] clarityTypes = {"FL", "IF", "VVS1", "VVS2", "VS1", "VS2", "SI1", "SI2", "I1", "I2", "I3"};

        try{
            while(sc.hasNext()){
                Diamonds tempDiamond = new Diamonds();
                tempDiamond.setIndex(Integer.parseInt(sc.nextLine()));
                tempDiamond.setWeight(Double.parseDouble(sc.nextLine()));
                tempDiamond.setCutQuality(sc.nextLine());
                tempDiamond.setColor(sc.nextLine().charAt(0));
                tempDiamond.setClarity(sc.nextLine());
                tempDiamond.setDepth(Double.parseDouble(sc.nextLine()));
                tempDiamond.setWidth(Double.parseDouble(sc.nextLine()));
                tempDiamond.setPrice(Integer.parseInt(sc.nextLine()));
                tempDiamond.setX(Double.parseDouble(sc.nextLine()));
                tempDiamond.setY(Double.parseDouble(sc.nextLine()));
                diamondsData.add(tempDiamond);
            }
        }catch (DiamondException de){
            System.out.println(de);
        }

        sc.close();
        return diamondsData;
    }
}
//Реализация интерфейса Comparator<T> для возможности сортировки по качеству прозрачности
class ClarityComparator implements Comparator<String>{
    private final List<String> clarityTypes = Arrays.asList("FL", "IF", "VVS1", "VVS2", "VS1", "VS2", "SI1", "SI2", "I1", "I2", "I3");
    public int compare(String s1, String s2){
        return Integer.compare(clarityTypes.indexOf(s1), clarityTypes.indexOf(s2));
    }
}