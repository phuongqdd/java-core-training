package design_pattern.factory_method;

public class CandyFactory {
     public static Candy getCandy(CandyType candyType){
         if(candyType == CandyType.HARD){
             return new HardCandy();
         }else {
             return new MintyCandy();
         }
     }
}
