package site.devwon.customwas.controller;

import site.devwon.customwas.request.HttpRequest;
import site.devwon.customwas.response.HttpResponse;

import java.text.ParseException;

public interface Controller {
    void service(HttpRequest httpRequest, HttpResponse httpResponse) throws ParseException;
}
