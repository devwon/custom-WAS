package site.devwon.customwas.controller;

import site.devwon.customwas.constant.HttpMethod;
import site.devwon.customwas.request.HttpRequest;
import site.devwon.customwas.response.HttpResponse;

import java.text.ParseException;

public abstract class AbstractController implements Controller {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws ParseException {
        if (HttpMethod.GET.equals(httpRequest.getMethod())) {
            doGet(httpRequest, httpResponse);
        }

        if (HttpMethod.POST.equals(httpRequest.getMethod())) {
            doPost(httpRequest, httpResponse);
        }
    }

    protected abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws ParseException;

    protected abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse);
}
