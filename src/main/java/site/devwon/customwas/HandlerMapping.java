package site.devwon.customwas;

import site.devwon.customwas.controller.Controller;
import site.devwon.customwas.controller.DefaultController;
import site.devwon.customwas.controller.HomeController;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class HandlerMapping {
    private static Map<String, Controller> controllers = new HashMap<>();
    private static DefaultController defaultController = new DefaultController();
    private static String controllerName = defaultController.getClass().getCanonicalName().replace(defaultController.getClass().getSimpleName(),"");
    private final static String controller = "Controller";

    static {
        controllers.put("/index", new HomeController());

        // CONTROLLER_MAPPER 기반으로 Path Controller Mapping
        if (HttpServer.ControllerMapper != null) {
            Iterator<String> iterator = HttpServer.ControllerMapper.iterator();
            while (iterator.hasNext()) {
                String nextItem = iterator.next();
                try {
                    controllers.put("/" + nextItem, (Controller) Class.forName(controllerName + nextItem + controller).getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    static Controller findController(String url) {
        for (String key : controllers.keySet()) {
            if (url.equals(key)) {
                return controllers.get(key);
            }
        }
        return defaultController;
    }
}
