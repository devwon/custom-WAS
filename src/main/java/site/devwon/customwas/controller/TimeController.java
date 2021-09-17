package site.devwon.customwas.controller;

import site.devwon.customwas.constant.ContentType;
import site.devwon.customwas.constant.TimeFormat;
import site.devwon.customwas.request.HttpRequest;
import site.devwon.customwas.response.HttpResponse;
import site.devwon.customwas.util.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class TimeController extends AbstractController {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws ParseException {
        httpResponse.addHeader(ContentType.CONTENT_TYPE, ContentType.HTML_CONTENT_TYPE);
        httpResponse.processBody(String.format("<h3>Path = %s</h3><h3>Now Time = %s </h3> ",
                httpRequest.getPath(), DateUtils.getDateTimeFormat(new Date(), TimeFormat.FORMAT_1)));
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }
}
