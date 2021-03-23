package ua.ies.pedro93221;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import weather.CityForecast;
import weather.IpmaCityForecast;
import weather.IpmaService;
import java.util.Iterator;
import java.util.Scanner;

import java.util.logging.Logger;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class MyWeatherRadar {

    private static final int CITY_ID_AVEIRO = 1010500;
    /*
    loggers provide a better alternative to System.out.println
    https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
     */
    private static final Logger logger = Logger.getLogger(MyWeatherRadar.class.getName());

    public static void  main(String[] args ) {

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
        Scanner sc = new Scanner(System.in);
        logger.info("Insert desired city code:");
        int cityCode = sc.nextInt();

        IpmaService service = retrofit.create(IpmaService.class);
        Call<IpmaCityForecast> callSync = service.getForecastForACity(cityCode);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                Iterator<CityForecast> cityForIt = forecast.getData().iterator();
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