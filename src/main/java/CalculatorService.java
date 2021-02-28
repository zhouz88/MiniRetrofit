import annoations.Get;
import annoations.Param;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

public class CalculatorService {
    public Calculator cal;
    private OkHttpClient client;
    private static final String BASE_URL = "https://openapi.newsbreak.com";

    public CalculatorService() {
        client = new OkHttpClient();
        cal = (Calculator) Proxy.newProxyInstance(
                Calculator.class.getClassLoader(), new Class[]{Calculator.class}, new InvocationHandler() {
                    public Object invoke(Object proxy, Method ok, Object[] args) throws Throwable {
                        StringBuilder sb = new StringBuilder();
                        sb.append(BASE_URL);
                        if (ok.isAnnotationPresent(Get.class)){
                            Get a = ok.getAnnotation(Get.class);
                            String path = a.value();
                            sb.append(path + "?");
                        }
                        Parameter[] parameters = ok.getParameters();
                        int i = 0;
                        for (Parameter p : parameters) {
                            if (p.isAnnotationPresent(Param.class)) {
                                Param param = p.getAnnotation(Param.class);
                                sb.append(param.value() + "=" + args[i++] + "&");
                            }
                        }
                        System.out.println(ok.getReturnType().getName());
                        Request rq = new Request.Builder().url(sb.toString().substring(0, sb.length() - 1)).build();
                        Response r = client.newCall(rq).execute();
                        return r.body().string();
                    }
                });
    }
}
