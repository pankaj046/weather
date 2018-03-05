package pankaj.sharma.weather;

/*
 *   pankaj sharma
 */
public class IconChecker {

    int iconLocation;
   public String getIconLocation(String iconname, String iconDescription) {
       Integer[] imgid={
               R.drawable.clear,
               R.drawable.clearnight,
               R.drawable.partlysunny,
               R.drawable.partlycloundynight,
               R.drawable.cloudy,
               R.drawable.scatteredclouds,
               R.drawable.flurries,
               R.drawable.partysunnyrain,
               R.drawable.rainnight,
               R.drawable.storms,
               R.drawable.cloundysnow,
               R.drawable.fog,
               R.drawable.unknown,
       };
       switch (iconname) {
           case "01d":
               iconLocation = imgid[0];
               break;
           case "01n":
               iconLocation = imgid[1];
               break;
           case "02d":
               iconLocation = imgid[2];
               break;
           case "02n":
               iconLocation = imgid[3];
               break;
           case "03d":
               iconLocation = imgid[4];
               break;
           case "03n":
               iconLocation = imgid[4];
               break;
           case "04d":
               iconLocation = imgid[5];
               break;
           case "04n":
               iconLocation = imgid[5];
               break;
           case "09d":
               iconLocation = imgid[6];
               break;
           case "09n":
               iconLocation = imgid[6];
               break;
           case "10d":
               iconLocation = imgid[7];

               break;
           case "10n":
               iconLocation = imgid[8];

               break;
           case "11d":
               iconLocation = imgid[9];
               break;
           case "11n":
               iconLocation = imgid[9];
               break;
           case "13d":
               iconLocation = imgid[10];
               break;
           case "13n":
               iconLocation = imgid[10];
               break;
           case "50d":
               iconLocation = imgid[11];
               break;
           case "50n":
               iconLocation = imgid[11];
               break;
           default: iconLocation = imgid[12];
       }
       return null;
   }
}
