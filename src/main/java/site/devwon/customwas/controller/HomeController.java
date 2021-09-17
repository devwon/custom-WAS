package site.devwon.customwas.controller;

import site.devwon.customwas.constant.ContentType;
import site.devwon.customwas.request.HttpRequest;
import site.devwon.customwas.response.HttpResponse;

public class HomeController extends AbstractController {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.addHeader(ContentType.CONTENT_TYPE, ContentType.HTML_CONTENT_TYPE);
        httpResponse.process(httpRequest.getPath());
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }
}
