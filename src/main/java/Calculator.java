import annoations.Get;
import annoations.Param;

public interface Calculator {
    @Get("/serving")
    String callApi(@Param("app") String appName, @Param("token") String b, @Param("lat") String c,@Param("lng") String d);
}
