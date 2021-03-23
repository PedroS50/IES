package ua.ies.pedro93221;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import weather.CityForecast;
import weather.IpmaCityForecast;
import weather.IpmaService;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class MyWeatherRadar {

    private static final int CITY_ID_AVEIRO = 1010500;
    /*
    loggers provide a better alternative to System.out.println
    https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
     */
    private static Logger logger = LogManager.getLogger(MyWeatherRadar.class.getName());

    public static void  main(String[] args ) {
        /*
        Create (City Name, City Code) dictionary
        */
        Map<String, Integer> cityInfo = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        cityInfo.put("Aveiro", 1010500);
        cityInfo.put("Beja", 1020500);
        cityInfo.put("Braga", 1030300);
        cityInfo.put("Bragança", 1040200);
        cityInfo.put("Castelo Branco", 1050200);
        cityInfo.put("Coimbra", 1060300);
        cityInfo.put("Évora", 1070500);
        cityInfo.put("Faro", 1080500);
        cityInfo.put("Guarda", 1090700);
        cityInfo.put("Leiria", 1100900);
        cityInfo.put("Lisboa", 1110600);
        cityInfo.put("Portalegre", 1121400);
        cityInfo.put("Porto", 1131200);
        cityInfo.put("Santarém", 1141600);
        cityInfo.put("Setúbal", 1151200);
        cityInfo.put("Viana do Castelo", 1160900);
        cityInfo.put("Vila Real", 1171400);
        cityInfo.put("Viseu", 1182300);
        cityInfo.put("Funchal", 2310300);
        cityInfo.put("Porto Santo", 2320100);
        cityInfo.put("Vila do Porto", 3410100);
        cityInfo.put("Ponta Delgada", 3420300);
        cityInfo.put("Angra do Heroísmo", 3430100);
        cityInfo.put("Santa Cruz da Graciosa", 3440100);
        cityInfo.put("Velas", 3450200);
        cityInfo.put("Madalena", 3460200);
        cityInfo.put("Horta", 3470100);
        cityInfo.put("Santa Cruz das Flores", 3480200);
        cityInfo.put("Vila do Corvo", 3490100);

        /*
        get a retrofit instance, loaded with the GSon lib to convert JSON into objects
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /*
        Get city code
        */
        int cityCode;

        if (args.length > 0){
            cityCode = cityInfo.get(args[0]);
        } else{
            Scanner sc = new Scanner(System.in);
            logger.info("Insert desired city code:");
            cityCode = sc.nextInt();
        }

        IpmaService service = retrofit.create(IpmaService.class);
        Call<IpmaCityForecast> callSync = service.getForecastForACity(cityCode);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                Iterator<CityForecast> cityForIt = forecast.getData().iterator();
                logger.info("Showing results for city code " + cityCode + "...\n");
                while (cityForIt.hasNext()){
                    CityForecast currForecast = cityForIt.next();
                    logger.info("Forecast for <<" + currForecast.getForecastDate() + ">>:\n\n" +
                                "-- Minimum Temperature: " + currForecast.getTMin() + "\n" + 
                                "-- Max Temperature: " + currForecast.getTMax() + "\n" + 
                                "-- Probability of Precipitation: " + currForecast.getPrecipitaProb() + "\n" +
                                "-- Precipitation Intensity: " + currForecast.getClassPrecInt() + "\n" +
                                "-- Wind Direction: " + currForecast.getPredWindDir() + "\n" + 
                                "-- Wind Intensity: " + currForecast.getClassWindSpeed() + "\n" +
                                "-- Latitude: " + currForecast.getLatitude() + "\n" +
                                "-- Longitude: " + currForecast.getLongitude() + "\n");
                }
            } else {
                logger.info( "No results!");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}